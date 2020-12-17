package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDTO;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDTO;
import team.combinatorics.shuwashuwa.model.dto.UserInfoResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.UserInfoUpdateDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Api(value = "User相关接口说明")
@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    UserService userService;

    /**
     * 注册
     */
    @ApiOperation(value = "[无需token]登录", notes = "通过微信提供的临时登录凭证进行登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")

    })
    @NoToken
    public CommonResult<LogInSuccessDTO> loginHandler(
            @ApiParam(value = "微信登录信息", required = true) LogInInfoDTO logInInfoDto
    ) throws Exception {
        System.out.println("用户登录 @Controller");
        System.out.println("Code:" + logInInfoDto.getCode());
        LogInSuccessDTO logInSuccessDto = userService.wechatLogin(logInInfoDto);
        System.out.println("ID: " + TokenUtil.extractUserid(logInSuccessDto.getToken()));
        System.out.println("Token: " + logInSuccessDto.getToken());
        return new CommonResult<>(200, "登录成功", logInSuccessDto);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation(value = "更新用户信息", notes = "根据传入的数据结构对数据库中用户的相应表项进行更新")
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功")
    })
    @AllAccess
    public CommonResult<String> updateUserInfo(@RequestHeader("token") @ApiParam(hidden = true) String token,
                                               @RequestBody @ApiParam(value = "用户信息", required = true) UserInfoUpdateDTO userInfoUpdateDto
    ) throws Exception {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("更新" + userid + "的用户信息");
        System.out.println(userInfoUpdateDto.toString());
        userService.updateUserInfo(userid, userInfoUpdateDto);
        return new CommonResult<>(200, "更新成功", "User's information has been updated!");
    }


    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取当前用户信息", notes = "根据当前token中的userid获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<UserInfoResponseDTO> getUserInfo(
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) throws Exception {
        int userid = TokenUtil.extractUserid(token);
        UserInfoResponseDTO responseDTO = userService.getUserInfo(userid);
        System.out.println(userid + "请求个人信息");
        return new CommonResult<>(200, "请求成功", responseDTO);
    }


    /**
     * 删除单个用户，测试用
     * todo 真的还用吗
     */
    @ApiOperation(value = "[测试用]删除单个用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 40001, message = "不存在的用户ID")
    })
    @RequestMapping(value = "/deleteOne", method = RequestMethod.DELETE)
    @NoToken
    public CommonResult<String> deleteOneUser(
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) throws Exception {
        int cnt = userService.deleteOneUser(TokenUtil.extractUserid(token));
        if (cnt > 0) {
            return new CommonResult<>(200, "删除成功", "If success, you can receive this message.");
        }
        return new CommonResult<>(40001, "不存在的用户ID", "You have deleted a ghost user!");
    }

    /**
     * 删除所有用户，测试用
     * todo 真的还用吗
     */
    @ApiOperation(value = "[测试用]删除所有用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
    })
    @NoToken
    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public CommonResult<String> deleteAllUser() {
        System.out.println("即将删除所有用户信息");
        userService.deleteAllUsers();
        return new CommonResult<>(200, "删除成功", "All users have been deleted.");
    }

}