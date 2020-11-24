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
import team.combinatorics.shuwashuwa.model.pojo.User;
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
            @ApiResponse(code = 200, message = "请求成功")
    })
    public CommonResult<LogInSuccessDto> addUser(LogInInfoDto logInInfoDto) throws Exception {
        System.out.println("新增了一个用户");
        System.out.println(logInInfoDto.getCode());
        LogInSuccessDto logInSuccessDto = userService.wechatLogin(logInInfoDto);
        return new CommonResult<>(200, "注册成功", logInSuccessDto);
    }

    /**
     * 删除单个用户，测试用
     */
    @ApiOperation(value = "删除单个用户", notes = "删除单个用户，测试用", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 204, message = "用户不存在")
    })
    @RequestMapping(value = "/deleteOneUser", method = RequestMethod.DELETE)
    public CommonResult<String> deleteOneUser(@RequestBody String openID) {
        System.out.println("删除用户，openid为：" + openID);
        int cnt = userService.getUserDao().deleteUserByOpenid(openID);
        if(cnt > 0)
        {
            return new CommonResult<>(200, "删除成功", "If success, you can receive this message.");
        }
        return new CommonResult<>(204, "用户不存在", "You have deleted a ghost user!");
    }

    /**
     * 删除所有用户，测试用
     */
    @ApiOperation(value = "删除所有用户", notes = "删除所有用户，测试用", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
    })
    @RequestMapping(value = "/deleteAllUsers", method = RequestMethod.DELETE)
    public CommonResult<String> deleteAllUser() {
        System.out.println("即将删除所有用户信息");
        userService.getUserDao().deleteAllUsers();
        return new CommonResult<>(200, "删除成功", "All users have been deleted.");
    }


}
