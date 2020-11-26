package team.combinatorics.shuwashuwa.controller;

import com.mysql.cj.log.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.model.bean.CommonResult;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.service.UserService;

@Api(value = "User相关接口说明")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 注册
     */
    @ApiOperation(value = "登录", notes = "通过微信提供的临时登录凭证进行登录", httpMethod = "GET")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 40001, message = "lbl出错了") /*???*/

    })
    public CommonResult<LogInSuccessDto> addUser(LogInInfoDto logInInfoDto) throws Exception {
        System.out.println("新增了一个用户");
        System.out.println(logInInfoDto.getCode());
        LogInSuccessDto logInSuccessDto = userService.wechatLogin(logInInfoDto);
        return new CommonResult<>(200, "注册成功", logInSuccessDto);
    }

    /**
     * 删除用户，测试用
     */
    @ApiOperation(value = "删除所有用户", notes = "删除所有用户，测试用", httpMethod = "POST")
    public CommonResult<String> deleteAllUser() {
        return null;
    }

//    /**
//     * 删除所有用户
//     */
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public CommonResult<LogInSuccessDto> addUser(@RequestBody LogInInfoDto logInInfoDto) throws Exception {
//        System.out.println("新增了一个用户");
//        LogInSuccessDto logInSuccessDto = userService.wechatLogin(logInInfoDto);
//        return new CommonResult<>(200, "注册成功", logInSuccessDto);
//    }


}
