package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAdditionDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.VolunteerService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.util.List;

@Api(value = "志愿者申请相关接口说明")
@RestController
@AllArgsConstructor
@RequestMapping("/api/volunteer")
public class VolunteerController {

    VolunteerService volunteerService;

    /**
     * 接收志愿者申请
     */
    @ApiOperation(value = "接收当前用户的申请", notes = "已经是管理员的用户不能提交申请")
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
     * TODO 这里暂时没支持时间搜索，不知道是否用得到，反正之后加也好加
     *
     * @param token        申请操作的用户的token
     * @param targetUserID 目标申请表中的用户id
     * @param adminID      目标申请表中的管理员id
     * @param status       目标申请表的当前状态
     * @return 符合条件的维修单列表
     */
    @ApiOperation(value = "条件查询申请表的摘要列表", notes = "用户身份下，targetUserID会被强行设置为用户自己的userid")
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "申请完成")
    })
    @AllAccess
    public CommonResult<List<VolunteerApplicationAbstractDTO>> listVolunteerApplicationByCondition(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam("userId") @ApiParam(value = "目标申请表中的用户id") Integer targetUserID,
            @RequestParam("adminId") @ApiParam(value = "目标申请表中的管理员id") Integer adminID,
            @RequestParam("status") @ApiParam(value = "目标申请的状态") Integer status
    ) {
        // 提取当前用户的id
        int currentUserId = TokenUtil.extractUserid(token);
        // 构造查询条件
        SelectApplicationCO selectApplicationCO = SelectApplicationCO.builder()
                .status(status)
                .userId(targetUserID)
                .adminId(adminID)
                .build();
        // 查询目标列表
        List<VolunteerApplicationAbstractDTO> volunteerApplicationAbstractDTOList =
                volunteerService.listVolunteerApplicationByCondition(currentUserId, selectApplicationCO);
        return new CommonResult<>(200, "请求成功", volunteerApplicationAbstractDTOList);
    }


    /**
     * 处理志愿者申请的审核
     */
    @ApiOperation(value = "[管理员]审核志愿者申请")
    @RequestMapping(value = "/application", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AdminAccess
    public CommonResult<String> receiveApplicationAudition(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "审核结果", required = true) VolunteerApplicationUpdateDTO updateDTO
    ) {
        // 得到当前管理员的用户id
        int adminUserid = TokenUtil.extractUserid(token);
        System.out.println(adminUserid + "审核了编号为" + updateDTO.getFormID() + "的申请");
        volunteerService.completeApplicationByAdmin(adminUserid, updateDTO);

        return new CommonResult<>(200, "请求成功", "success");
    }

}
