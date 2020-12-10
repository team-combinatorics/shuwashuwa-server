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

    @ApiOperation(value = "创建维修事件", httpMethod = "POST",
            notes = "创建并返回一个空的维修事件，返回结构与查询一致")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "请求成功")})
    @AllAccess
    public CommonResult<ServiceEventDetailDTO> handleServiceEventCreation(
            @RequestHeader @NotNull(message = "用户Token不能为空") String token) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.createNewEvent(userid));
    }

    @ApiOperation(value = "处理维修单提交", notes = "需要预先创建维修事件", httpMethod = "POST")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "请求成功")})
    @AllAccess
    public CommonResult<String> handleFormCommit(
            @RequestHeader @NotNull(message = "用户Token不能为空") String token,
            @RequestBody @NotNull(message = "维修单信息不能为空") ServiceFormSubmitDTO serviceFormSubmitDTO) {
        int userid = TokenUtil.extractUserid(token);
        eventService.submitForm(userid, serviceFormSubmitDTO,false);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "处理维修单草稿保存", notes = "需要预先创建维修事件", httpMethod = "PUT")
    @RequestMapping(value = "/draft", method = RequestMethod.PUT)
    @ApiResponses({@ApiResponse(code = 200, message = "请求成功")})
    @AllAccess
    public CommonResult<String> handleFormSaving(
            @RequestHeader @NotNull(message = "用户Token不能为空") String token,
            @RequestBody @NotNull(message = "维修单信息不能为空") ServiceFormSubmitDTO serviceFormSubmitDTO) {
        int userid = TokenUtil.extractUserid(token);
        eventService.submitForm(userid, serviceFormSubmitDTO,true);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.DELETE)
    @AdminAccess
    public CommonResult<String> handleFormAcceptance(
            @RequestHeader("token") String token,
            @RequestBody ServiceEventUniversalDTO messageDTO
            ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.acceptForm(userid,messageDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    @AdminAccess
    public CommonResult<String> handleFormRejection(
            @RequestHeader("token") String token,
            @RequestBody ServiceEventUniversalDTO reasonDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.rejectForm(userid,reasonDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/work", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleOrderTaking(
            @RequestHeader("token") String token,
            @RequestBody Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.takeOrder(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/work", method = RequestMethod.DELETE)
    @VolunteerAccess
    public CommonResult<String> handleOrderGiveUp(
          @RequestHeader("token") String token,
          @RequestBody Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.giveUpOrder(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/complete", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleServiceCompletion(
          @RequestHeader("token") String token,
          @RequestBody ServiceEventUniversalDTO resultDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.completeOrder(userid,resultDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFeedback(
            @RequestHeader("token") String token,
            @RequestBody ServiceEventUniversalDTO feedbackDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.updateFeedback(userid,feedbackDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @AllAccess
    public CommonResult<String> handleShutdown(
            @RequestHeader("token") String token,
            @RequestBody Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.shutdownService(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    @AdminAccess
    public CommonResult<List<ServiceEventDetailDTO>> getUnaudited() {
        return new CommonResult<>(200,"请求成功",eventService.listUnauditedEvents());
    }

    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    @AdminAccess
    public CommonResult<List<ServiceEventDetailDTO>> getServicesCreated(
            @RequestHeader("token") String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.listServicesCreatedBy(userid));
    }

    @RequestMapping(value = "/draft", method = RequestMethod.GET)
    @AdminAccess
    public CommonResult<List<ServiceEventDetailDTO>> getServicesToEdit(
            @RequestHeader("token") String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.listServiceToEditOf(userid));
    }

}
