package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.SUAccess;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationDetailBO;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.service.VolunteerService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "志愿者相关操作接口说明")
@RestController
@AllArgsConstructor
/*
 * 这里用/volunteer作为根目录是考虑到以后可能有志愿者独享的一些操作
 * 例如fxs老师说的志愿者自己申请一个什么志愿服务证明……
 * 总之先预留上
 */
@RequestMapping("/api/volunteer")
public class VolunteerController {

    VolunteerService volunteerService;
    UserService userService;

    /**
     * 接收志愿者申请
     */
    @ApiOperation(value = "当前用户上传一个志愿者申请", notes = "已经是志愿者的用户不能提交申请")
    @RequestMapping(value = "/application", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "申请完成")
    })
    @AllAccess
    public CommonResult<String> receiveApplicationInfo(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "新增的申请表内容", required = true) VolunteerApplicationAdditionDTO application
    ) {
        // 提取当前用户的id
        int userid = TokenUtil.extractUserid(token);
        volunteerService.addVolunteerApplication(userid, application);
        return new CommonResult<>(200, "申请完成", "posted");
    }


    /**
     * 条件查询志愿者申请列表
     *
     * @param token        申请操作的用户的token
     * @param targetUserID 目标申请表中的用户id
     * @param adminID      目标申请表中的管理员id
     * @param status       目标申请表的当前状态
     * @return 符合条件的维修单列表
     */
    @ApiOperation(value = "条件查询志愿者申请的摘要列表", notes = "用户身份下，targetUserID会被强行设置为用户自己的userid")
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<VolunteerApplicationDetailDTO>> listVolunteerApplicationByCondition(
            @RequestHeader("token")
            @ApiParam(hidden = true)
                    String token,
            @RequestParam(value = "userId", required = false)
            @ApiParam(value = "目标申请表中的申请者用户id，若发起请求的用户无特殊权限，该项被强制赋值为本人id")
                    Integer targetUserID,
            @RequestParam(value = "adminId", required = false)
            @ApiParam(value = "目标申请表中的管理员id")
                    Integer adminID,
            @RequestParam(value = "status", required = false)
            @ApiParam(value = "目标申请表的状态，0为待审核，1为通过，2为不通过", allowableValues = "0,1,2")
                    Integer status
    ) {
        // 对普通用户的查询做强制限制
        int currentUserId = TokenUtil.extractUserid(token);
        if (userService.isPlainUser(currentUserId))
            targetUserID = currentUserId;

        // 构造查询条件
        SelectApplicationCO selectApplicationCO = SelectApplicationCO.builder()
                .status(status)
                .userId(targetUserID)
                .adminId(adminID)
                .build();
        // 查询目标列表
        List<VolunteerApplicationDetailBO> boList =
                volunteerService.listVolunteerApplicationByCondition(selectApplicationCO);
        //日期转化
        List<VolunteerApplicationDetailDTO> dtoList = boList.stream()
                .map(x -> DTOUtil.convert(x, VolunteerApplicationDetailDTO.class))
                .collect(Collectors.toList());
        return new CommonResult<>(200, "请求成功", dtoList);
    }


    //    @ApiOperation(value = "获取志愿者申请的详细信息")
//    @RequestMapping(value = "/application/detail", method = RequestMethod.GET)
//    @AllAccess
    public CommonResult<VolunteerApplicationDetailDTO> getVolunteerApplicationDetail(
            @RequestParam("id") @ApiParam(value = "要查看的申请表id", required = true) Integer formId
    ) {
        VolunteerApplicationDetailBO bo = volunteerService.getApplicationDetailByFormId(formId);
        VolunteerApplicationDetailDTO dto = DTOUtil.convert(bo, VolunteerApplicationDetailDTO.class);
        return new CommonResult<>(200, "请求成功", dto);
    }


    /**
     * 处理志愿者申请的审核
     */
    @ApiOperation(value = "[管理员]审核志愿者申请，返回新志愿者的ID")
    @RequestMapping(value = "/application", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AdminAccess
    public CommonResult<Integer> receiveApplicationAudition(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "审核结果", required = true) VolunteerApplicationAuditDTO auditDTO
    ) {
        // 得到当前管理员的用户id
        int adminUserid = TokenUtil.extractUserid(token);
        System.out.println("审核编号为" + auditDTO.getFormId() + "的申请");
        int volunteerId = volunteerService.completeApplicationByAdmin(adminUserid, auditDTO);

        return new CommonResult<>(200, "请求成功", volunteerId);
    }

    @ApiOperation("[志愿者]查询自己的志愿者ID")
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    @VolunteerAccess
    public CommonResult<Integer> getMyVolunteerId(
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        int volunteerId = volunteerService.getVolunteerIdByUserid(userid);
        System.out.println("志愿者ID是" + volunteerId);
        return new CommonResult<>(200, "请求成功", volunteerId);
    }

    /**
     * 添加志愿者
     */
    @ApiOperation(value = "根据输入的信息添加新的志愿者")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功"),
            @ApiResponse(code = 40010, message = "添加失败，信息不完整")
    })
    @SUAccess
    @AdminAccess
    public CommonResult<String> addNewVolunteer(
            @RequestBody @NotNull(message = "管理员信息不能为空") @ApiParam("管理员信息") VolunteerDTO volunteerDTO
    ) {
        if (DTOUtil.fieldExistNull(volunteerDTO)) {
            return new CommonResult<>(40010, "添加失败，信息不完整", "You need to fill all information");
        }
        System.out.println(volunteerDTO.getUserid() + "将被添加为志愿者");
        int cnt = volunteerService.addVolunteer(volunteerDTO);
        if (cnt == 1) {
            return new CommonResult<>(200, "添加成功", "success");
        }
        //todo 这为啥是数据库异常
        return new CommonResult<>(40000, "数据库出现异常，请检查数据库", "database failure");
    }

    /**
     * 获取志愿者列表
     */
    @ApiOperation(value = "（超级）管理员获取所有志愿者的列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
    })
    @SUAccess
    @AdminAccess
    public CommonResult<List<VolunteerDTO>> getVolunteerList() {

        return new CommonResult<>(200, "获取成功", volunteerService.getVolunteerList());
    }

    /**
     * 删除志愿者
     */
    @ApiOperation(value = "根据输入的信息删除对应的志愿者", notes = "超管&管理员专属")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
    })
    @SUAccess
    @AdminAccess
    public CommonResult<String> deleteVolunteer(
            @RequestParam @NotNull(message = "用户id不能为空") @ApiParam("要删除志愿者权限的用户id") int userID
    ) {
        System.out.println("删除志愿者(UID=" + userID + ")");
        int cnt = volunteerService.deleteVolunteer(userID);
        if (cnt == 1)
            return new CommonResult<>(200, "删除成功", "success");
        return new CommonResult<>(40000, "数据库异常，请检查数据库", "You need to check database");
    }

    /**
     * 获取单个志愿者的信息
     */
    @ApiOperation(value = "（超级）管理员获取单个志愿者的详细信息")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
    })
    @SUAccess
    public CommonResult<VolunteerDTO> getVolunteerInfo(
            @NotNull(message = "用户id不能为空") @ApiParam("要获取志愿者信息的用户id") int userID
    ) {
        System.out.println("获取志愿者信息(UID=" + userID + ")");
        return new CommonResult<>(200, "获取成功", volunteerService.getVolunteerInfo(userID));
    }

    /**
     * 修改志愿者信息
     */
    @ApiOperation(value = "根据输入修改志愿者信息")
    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 40010, message = "更新失败，信息不能全为空")
    })
    @SUAccess
    @AdminAccess
    public CommonResult<String> updateVolunteerInfo(
            @RequestBody @NotNull(message = "志愿者信息不能为空") @ApiParam("志愿者信息") VolunteerDTO volunteerDTO
    ) {
        if (volunteerDTO.getUserid()==null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        int backup = volunteerDTO.getUserid();
        volunteerDTO.setUserid(null);
        if (DTOUtil.fieldAllNull(volunteerDTO))
            return new CommonResult<>(40010, "更新失败，信息不能全为空", "You should fill volunteer info!");
        volunteerDTO.setUserid(backup);
        System.out.println("即将更新用户id为" + volunteerDTO.getUserid() + "的志愿者的信息");
        int cnt = volunteerService.updateVolunteerInfo(volunteerDTO);
        if (cnt > 0)
            return new CommonResult<>(200, "更新成功", "success");
        return new CommonResult<>(40000, "数据库异常，请检查数据库", "You should check your database!");
    }
}
