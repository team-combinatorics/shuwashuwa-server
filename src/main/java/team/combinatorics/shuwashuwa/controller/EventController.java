package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.bo.ServiceAbstractBO;
import team.combinatorics.shuwashuwa.model.bo.ServiceEventDetailBO;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Api("维修事件接口说明")
@RestController
@RequestMapping("api/service")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    private final UserService userService;

    @ApiOperation(value = "创建维修事件", notes = "返回仅包含一个空白草稿的维修事件")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @AllAccess
    public CommonResult<ServiceEventDetailBO> handleServiceEventCreation(
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        ServiceEventDetailBO createdEvent = eventService.createNewEvent(userid);
        System.out.println("用户" + userid + "创建了维修事件，编号" + createdEvent.getId());
        return new CommonResult<>(200, "请求成功", createdEvent);
    }

    @ApiOperation(value = "提交维修单", notes = "需要预先创建维修事件")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @AllAccess
    public CommonResult<String> handleFormCommit(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件的id，以及维修单的信息", required = true)
                    ServiceFormSubmitDTO serviceFormSubmitDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("用户" + userid + "在维修事件" + serviceFormSubmitDTO.getServiceEventId() + "提交了维修单");
        eventService.submitForm(userid, serviceFormSubmitDTO, false);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation(value = "保存维修单草稿", notes = "需要预先创建维修事件")
    @RequestMapping(value = "/draft", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFormSaving(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件的id，以及保存的维修单信息", required = true)
                    ServiceFormSubmitDTO serviceFormSubmitDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("用户" + userid + "在维修事件" + serviceFormSubmitDTO.getServiceEventId() + "保存了草稿");
        eventService.submitForm(userid, serviceFormSubmitDTO, true);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation(value = "[管理员]审核维修单")
    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    @AdminAccess
    public CommonResult<String> handleFormAcceptance(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "审核结果结构", required = true) ServiceEventAuditDTO auditDTO
    ) throws Exception {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("管理员(UID" + userid + ")审核了维修事件" + auditDTO.getServiceEventId() + "，结果为" + auditDTO.getResult());
        eventService.auditForm(userid, auditDTO);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation("[志愿者]接单")
    @RequestMapping(value = "/work", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleOrderTaking(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam("eventID") @ApiParam(value = "维修事件Id", required = true) Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("志愿者(UID" + userid + ")接单了维修事件" + serviceEventId);
        eventService.takeOrder(userid, serviceEventId);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation("[志愿者]退回已接维修单")
    @RequestMapping(value = "/work", method = RequestMethod.DELETE)
    @VolunteerAccess
    public CommonResult<String> handleOrderGiveUp(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam("eventID") @ApiParam(value = "维修事件Id", required = true) Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("志愿者(UID" + userid + ")退单了维修事件" + serviceEventId);
        eventService.giveUpOrder(userid, serviceEventId);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation("[志愿者]完成维修单并填写结果")
    @RequestMapping(value = "/complete", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleServiceCompletion(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "更新维修结果的结构", required = true) ServiceSimpleUpdateDTO resultDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("志愿者(UID" + userid + ")完成了维修事件" + resultDTO.getServiceEventId());
        eventService.completeOrder(userid, resultDTO);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation(value = "用户对维修进行反馈", notes = "会覆盖之前的反馈")
    @RequestMapping(value = "/feedback", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFeedback(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "更新反馈内容的结构", required = true) ServiceSimpleUpdateDTO feedbackDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("用户" + userid + "更新了维修" + feedbackDTO.getServiceEventId() + "的反馈");
        eventService.updateFeedback(userid, feedbackDTO);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation(value = "中止维修事件，取消预订", notes = "会将维修事件的closed标记置为true")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @AllAccess
    public CommonResult<String> handleShutdown(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam("eventID") @ApiParam(value = "维修事件Id", required = true) Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("用户" + userid + "关闭了维修" + serviceEventId);
        eventService.shutdownService(userid, serviceEventId);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation(value = "列出满足指定筛选条件的维修事件", notes = "不需要筛选的条件无需赋值")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ServiceAbstractDTO>> getServiceEventList(
            @RequestHeader(value = "token")
            @ApiParam(hidden = true)
                    String token,
            @RequestParam(value = "client", required = false)
            @ApiParam("创建维修事件的用户id，若发起请求的用户无特殊权限，该项被强制赋值为本人id")
                    Integer clientId,
            @RequestParam(value = "volunteer", required = false)
            @ApiParam("接单的志愿者id")
                    Integer volunteerId,
            @RequestParam(value = "activity", required = false)
            @ApiParam("报名的活动id")
                    Integer activityId,
            @RequestParam(value = "status", required = false)
            @ApiParam(value = "该次维修处于的状态,可能状态如下:\n" +
                    "0:等待用户编辑\n" +
                    "1:等待管理员审核\n" +
                    "2:审核通过（待签到）\n" +
                    "3:等待志愿者接单\n" +
                    "4:维修中\n" +
                    "5:维修完成\n",
                    allowableValues = "0,1,2,3,4,5")
                    Integer status,
            @RequestParam(value = "draft", required = false)
            @ApiParam("是否有云端保存的草稿")
                    Boolean draftSaved,
            @RequestParam(value = "closed", required = false)
            @ApiParam("维修事件是否关闭")
                    Boolean closed,
            @RequestParam(value = "createLower", required = false)
            @ApiParam(value = "创建时间下界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String createTimeLowerBound,
            @RequestParam(value = "createUpper", required = false)
            @ApiParam(value = "创建时间上界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String createTimeUpperBound
    ) {
        System.out.println("请求维修事件列表");
        //对普通用户的查询做强制限制
        int currentUserId = TokenUtil.extractUserid(token);
        if (userService.isPlainUser(currentUserId))
            clientId = currentUserId;

        SelectServiceEventCO serviceEventCO = SelectServiceEventCO
                .builder()
                .userId(clientId)
                .volunteerId(volunteerId)
                .activityId(activityId)
                .closed(closed)
                .draft(draftSaved)
                .status(status)
                .build();
        if (createTimeLowerBound != null) serviceEventCO.setBeginTime(Timestamp.valueOf(createTimeLowerBound));
        if (createTimeUpperBound != null) serviceEventCO.setEndTime(Timestamp.valueOf(createTimeUpperBound));
        final List<ServiceAbstractBO> boList = eventService.listServiceEvents(serviceEventCO);
        final List<ServiceAbstractDTO> dtoList = boList.stream()
                .map(x -> DTOUtil.convert(x, ServiceAbstractDTO.class))
                .collect(Collectors.toList());
        return new CommonResult<>(200, "请求成功", dtoList);
    }

    @ApiOperation(value = "统计满足条件的维修事件数量", notes = "不需要筛选的条件无需赋值(暂时没有限制调用权限)")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<Integer> getServiceEventNumber(
            @RequestParam(value = "client", required = false)
            @ApiParam("创建维修事件的用户id")
                    Integer clientId,
            @RequestParam(value = "volunteer", required = false)
            @ApiParam("接单的志愿者id")
                    Integer volunteerId,
            @RequestParam(value = "activity", required = false)
            @ApiParam("报名的活动id")
                    Integer activityId,
            @RequestParam(value = "status", required = false)
            @ApiParam(value = "该次维修处于的状态,可能状态如下:\n" +
                    "0:等待用户编辑\n" +
                    "1:等待管理员审核\n" +
                    "2:审核通过（待签到）\n" +
                    "3:等待志愿者接单\n" +
                    "4:维修中\n" +
                    "5:维修完成\n",
                    allowableValues = "0,1,2,3,4,5")
                    Integer status,
            @RequestParam(value = "draft", required = false)
            @ApiParam("是否有云端保存的草稿")
                    Boolean draftSaved,
            @RequestParam(value = "closed", required = false)
            @ApiParam("维修事件是否关闭")
                    Boolean closed,
            @RequestParam(value = "createLower", required = false)
            @ApiParam(value = "创建时间下界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String createTimeLowerBound,
            @RequestParam(value = "createUpper", required = false)
            @ApiParam(value = "创建时间上界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String createTimeUpperBound
    ) {
        System.out.println("请求维修事件数量");
        SelectServiceEventCO serviceEventCO = SelectServiceEventCO
                .builder()
                .userId(clientId)
                .volunteerId(volunteerId)
                .activityId(activityId)
                .closed(closed)
                .draft(draftSaved)
                .status(status)
                .build();
        if (createTimeLowerBound != null) serviceEventCO.setBeginTime(Timestamp.valueOf(createTimeLowerBound));
        if (createTimeUpperBound != null) serviceEventCO.setEndTime(Timestamp.valueOf(createTimeUpperBound));
        return new CommonResult<>(200, "请求成功", eventService.countServiceEvents(serviceEventCO));
    }

    @ApiOperation("获取一次维修事件的详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<ServiceEventDetailDTO> getServiceDetail(
            @RequestParam("id") @ApiParam(value = "要查询的维修事件", required = true) Integer eventId
    ) {
        ServiceEventDetailBO bo = eventService.getServiceDetail(eventId);
        System.out.println("请求了维修事件" + eventId + "的详情");
        ServiceEventDetailDTO dto = DTOUtil.convert(bo, ServiceEventDetailDTO.class);
        return new CommonResult<>(200, "请求成功", dto);
    }

}
