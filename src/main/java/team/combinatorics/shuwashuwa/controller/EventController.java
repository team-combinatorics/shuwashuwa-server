package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import javax.validation.constraints.NotNull;

@Api("维修事件接口说明")
@RestController
@RequestMapping("api/service")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @ApiOperation(value = "处理维修事件创建", httpMethod = "POST",
            notes = "创建一个空的维修事件并返回，返回结构与查询一致")
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

    @AdminAccess
    public CommonResult<String> handleFormAcceptance() {
        return new CommonResult<>(200,"请求成功","success");
    }

    @AdminAccess
    public CommonResult<String> handleFormRejection() {
        return new CommonResult<>(200,"请求成功","success");
    }

    @VolunteerAccess
    public CommonResult<String> handleOrderTaking() {
        return new CommonResult<>(200,"请求成功","success");
    }

    @VolunteerAccess
    public CommonResult<String> handleVolunteerComment() {
        return new CommonResult<>(200,"请求成功","success");
    }

    @AllAccess
    public CommonResult<String> handleClientComment() {
        return new CommonResult<>(200,"请求成功","success");
    }

}
