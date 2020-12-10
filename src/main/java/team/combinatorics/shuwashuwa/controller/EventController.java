package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUniversalDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api("维修事件接口说明")
@RestController
@RequestMapping("api/service")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @ApiOperation(value = "创建维修事件",
            notes = "返回一个空的维修事件")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @AllAccess
    public CommonResult<ServiceEventDetailDTO> handleServiceEventCreation(
            @RequestHeader("token") String token) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.createNewEvent(userid));
    }

    @ApiOperation(value = "处理维修单提交", notes = "需要预先创建维修事件")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @AllAccess
    public CommonResult<String> handleFormCommit(
            @RequestHeader("token") String token,
            @RequestBody @NotNull(message = "请上传维修单信息") @ApiParam("维修事件的id，以及维修单的信息")
                    ServiceFormSubmitDTO serviceFormSubmitDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.submitForm(userid, serviceFormSubmitDTO,false);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "处理维修单草稿保存", notes = "需要预先创建维修事件")
    @RequestMapping(value = "/draft", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFormSaving(
            @RequestHeader("token") String token,
            @RequestBody @NotNull(message = "请上传维修单信息") @ApiParam("维修事件的id，以及维修单的信息")
                    ServiceFormSubmitDTO serviceFormSubmitDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.submitForm(userid, serviceFormSubmitDTO,true);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "[管理员]维修单审核通过")
    @RequestMapping(value = "/audit", method = RequestMethod.DELETE)
    @AdminAccess
    public CommonResult<String> handleFormAcceptance(
            @RequestHeader("token") String token,
            @RequestBody @ApiParam("维修事件Id和留言（可选）") ServiceEventUniversalDTO messageDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.acceptForm(userid,messageDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[管理员]维修单审核不通过")
    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    @AdminAccess
    public CommonResult<String> handleFormRejection(
            @RequestHeader("token") String token,
            @RequestBody @ApiParam("维修事件Id和拒绝理由") ServiceEventUniversalDTO reasonDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.rejectForm(userid,reasonDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[志愿者]接单")
    @RequestMapping(value = "/work", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleOrderTaking(
            @RequestHeader("token") String token,
            @RequestBody @ApiParam("维修事件Id") Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.takeOrder(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[志愿者]退回已接维修单")
    @RequestMapping(value = "/work", method = RequestMethod.DELETE)
    @VolunteerAccess
    public CommonResult<String> handleOrderGiveUp(
          @RequestHeader("token") String token,
          @RequestBody @ApiParam("维修事件Id") Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.giveUpOrder(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[志愿者]完成维修单并填写结果")
    @RequestMapping(value = "/complete", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleServiceCompletion(
          @RequestHeader("token") String token,
          @RequestBody @ApiParam("维修事件Id和维修结果") ServiceEventUniversalDTO resultDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.completeOrder(userid,resultDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "用户对维修进行反馈",notes = "会覆盖之前的反馈")
    @RequestMapping(value = "/feedback", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFeedback(
            @RequestHeader("token") String token,
            @RequestBody @ApiParam("维修事件Id和反馈内容") ServiceEventUniversalDTO feedbackDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.updateFeedback(userid,feedbackDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "中止维修事件，取消预订", notes = "签到后不允许取消")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @AllAccess
    public CommonResult<String> handleShutdown(
            @RequestHeader("token") String token,
            @RequestBody @ApiParam("维修事件Id") Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.shutdownService(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[管理员]获取待审核维修事件列表")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    @AdminAccess
    public CommonResult<List<ServiceEventDetailDTO>> getUnaudited() {
        return new CommonResult<>(200,"请求成功",eventService.listUnauditedEvents());
    }

    @ApiOperation("获取自己发起的维修事件列表")
    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ServiceEventDetailDTO>> getServicesCreated(
            @RequestHeader("token") String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.listServicesCreatedBy(userid));
    }

    @ApiOperation("获取待填的维修事件列表")
    @RequestMapping(value = "/draft", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ServiceEventDetailDTO>> getServicesToEdit(
            @RequestHeader("token") String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.listServiceToEditOf(userid));
    }

    @ApiOperation("[志愿者]获取某活动待接单的维修事件列表")
    @RequestMapping(value = "/work", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ServiceEventDetailDTO>> getAvailableOrder(
            @RequestBody @ApiParam("要查询的活动") Integer activityId
    ) {
        return new CommonResult<>(200,"请求成功",eventService.listPendingEvents(activityId));
    }

}
