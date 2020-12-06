package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.SUAccess;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;

import javax.validation.constraints.NotNull;

@Api(value = "超级管理员相关接口说明")
@RestController
@Validated
@RequestMapping("/api/super")
@AllArgsConstructor
public class SuperAdministratorController {
    private final SuperAdministratorService superAdministratorService;

    @ApiOperation(value = "超级管理员登录", notes = "通过输入内置的超级管理员用户名和密码进行登录", httpMethod = "GET")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 40011, message = "用户名或密码错误")
    })
    @NoToken
    public CommonResult<String> loginHandler(@NotNull(message = "用户名不能为空") String userName,
                                             @NotNull(message = "密码不能为空") String password)
    {

        String token = superAdministratorService.checkInfo(userName, password);

        if(token != null)
        {
            return new CommonResult<>(200, "登录成功", token);
        }
        else
        {
            return new CommonResult<>(40011, "用户名或密码错误", "");
        }

    }
}
