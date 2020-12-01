package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.model.bean.CommonResult;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDto;
import team.combinatorics.shuwashuwa.model.pojo.User;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Api(value = "User相关接口说明")
@RestController
@RequestMapping("/api/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 注册
     */
    @ApiOperation(value = "登录", notes = "通过微信提供的临时登录凭证进行登录", httpMethod = "GET")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 40001, message = "lbl出错了") /*TODO: update ApiResponses*/

    })
    @NoToken
    public CommonResult<LogInSuccessDto> loginHandler(LogInInfoDto logInInfoDto) throws Exception {
        System.out.println("用户登录");
        System.out.println("Code:" + logInInfoDto.getCode());
        LogInSuccessDto logInSuccessDto = userService.wechatLogin(logInInfoDto);
        return new CommonResult<>(200, "登录成功", logInSuccessDto);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation(value = "更新用户信息", notes = "根据传入的数据结构对数据库中用户的相应表项进行更新", httpMethod = "PUT")
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 40000, message = "请求成功")
    })
    @AllAccess
    public CommonResult<String> updateUserInfo(@RequestHeader("token") String token, @RequestBody UpdateUserInfoDto updateUserInfoDto) throws Exception {
        userService.updateUserInfo(TokenUtil.extractUserid(token), updateUserInfoDto);
        return new CommonResult<>(40000, "请求成功", "User's information has been updated!");
    }


    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取当前用户信息", notes = "根据当前token中的userid获取用户信息", httpMethod = "GET")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 40000, message = "请求成功")
    })
    @AllAccess
    public CommonResult<User> getUserInfo(@RequestHeader("token") String token) throws Exception {
        User user = userService.getUserInfo(TokenUtil.extractUserid(token));
        return new CommonResult<>(40000, "请求成功", user);
    }

    /**
     * 删除单个用户，测试用
     */
    @ApiOperation(value = "删除单个用户", notes = "删除单个用户，测试用", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 40000, message = "请求成功"),
            @ApiResponse(code = 40001, message = "不存在的用户ID")
    })
    @RequestMapping(value = "/deleteOne", method = RequestMethod.DELETE)
    public CommonResult<String> deleteOneUser(@RequestHeader("token") String token) throws Exception {
        int cnt = userService.deleteOneUser(TokenUtil.extractUserid(token));
        if (cnt > 0) {
            return new CommonResult<>(40000, "请求成功", "If success, you can receive this message.");
        }
        return new CommonResult<>(40001, "不存在的用户ID", "You have deleted a ghost user!");
    }

    /**
     * 删除所有用户，测试用
     */
    @ApiOperation(value = "删除所有用户", notes = "删除所有用户，测试用", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 40000, message = "请求成功"),
    })
    @NoToken
    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public CommonResult<String> deleteAllUser() {
        System.out.println("即将删除所有用户信息");
        userService.deleteAllUsers();
        return new CommonResult<>(40000, "删除成功", "All users have been deleted.");
    }


}
