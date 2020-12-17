package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationAbstractBO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationDetailBO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAdditionDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAuditDTO;
import team.combinatorics.shuwashuwa.model.dto.CommonResult;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.service.VolunteerService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

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
    @ApiOperation(value = "当前用户上传一个志愿者申请", notes = "已经是管理员的用户不能提交申请")
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
        System.out.println(userid + "提交了志愿者申请");
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
    public CommonResult<List<VolunteerApplicationAbstractDTO>> listVolunteerApplicationByCondition(
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
            @ApiParam(value = "目标申请表的状态，0为待审核，1为通过，2为不通过",allowableValues = "0,1,2")
                    Integer status
    ) {
        // 对普通用户的查询做强制限制
        int currentUserId = TokenUtil.extractUserid(token);
        if(userService.isPlainUser(currentUserId))
            targetUserID = currentUserId;

        // 构造查询条件
        SelectApplicationCO selectApplicationCO = SelectApplicationCO.builder()
                .status(status)
                .userId(targetUserID)
                .adminId(adminID)
                .build();
        // 查询目标列表
        List<VolunteerApplicationAbstractBO> boList =
                volunteerService.listVolunteerApplicationByCondition(selectApplicationCO);
        //日期转化
        List<VolunteerApplicationAbstractDTO> dtoList = boList.stream()
                        .map(x -> (VolunteerApplicationAbstractDTO)
                                DTOUtil.convert(x,VolunteerApplicationDetailDTO.class))
                        .collect(Collectors.toList());
        return new CommonResult<>(200, "请求成功", dtoList);
    }


    @ApiOperation(value = "获取志愿者申请的详细信息")
    @RequestMapping(value = "/application/detail", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<VolunteerApplicationDetailDTO> getVolunteerApplicationDetail(
            @RequestParam("id") @ApiParam(value = "要查看的申请表id",required = true) Integer formId
    ) {
        VolunteerApplicationDetailBO bo = volunteerService.getApplicationDetailByFormId(formId);
        VolunteerApplicationDetailDTO dto =
                (VolunteerApplicationDetailDTO) DTOUtil.convert(bo,VolunteerApplicationDetailDTO.class);
        return new CommonResult<>(200,"请求成功",dto);
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
            @RequestBody @ApiParam(value = "审核结果", required = true) VolunteerApplicationAuditDTO updateDTO
    ) {
        // 得到当前管理员的用户id
        int adminUserid = TokenUtil.extractUserid(token);
        System.out.println(adminUserid + "审核了编号为" + updateDTO.getFormID() + "的申请");
        int volunteerId = volunteerService.completeApplicationByAdmin(adminUserid, updateDTO);

        return new CommonResult<>(200, "请求成功", volunteerId);
    }

}
