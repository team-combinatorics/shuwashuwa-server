package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventResponseDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;

import java.util.List;

@Api("维修事件接口说明")
@RestController
@RequestMapping("api/service")
@AllArgsConstructor
public class EventController {

    @ApiOperation(value = "处理维修单提交", notes = "若首次提交则创建维修事件，否则归档入指定的", httpMethod = "POST")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "请求成功")})
    @AllAccess
    public CommonResult<String> handleFormCommit() {
        return new CommonResult<>(200,"请求成功","success");
    }

    @AllAccess
    public CommonResult<String> handleFormSaving() {
        return new CommonResult<>(200,"请求成功","success");
    }

    public CommonResult<String> handleFormAcceptance() {
        return new CommonResult<>(200,"请求成功","success");
    }

    public CommonResult<String> handleFormRejection() {
        return new CommonResult<>(200,"请求成功","success");
    }

    public CommonResult<String> handleOrderTaking() {
        return new CommonResult<>(200,"请求成功","success");
    }

    public CommonResult<String> handleVolunteerComment() {
        return new CommonResult<>(200,"请求成功","success");
    }

    public CommonResult<String> handleClientComment() {
        return new CommonResult<>(200,"请求成功","success");
    }

}
