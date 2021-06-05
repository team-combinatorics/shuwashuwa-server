package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.util.List;

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
        System.out.println("Code:" + logInInfoDto.getCode());
        LogInSuccessDTO logInSuccessDto = userService.wechatLogin(logInInfoDto);
        System.out.println("UID: " + TokenUtil.extractUserid(logInSuccessDto.getToken()));
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
        return new CommonResult<>(200, "请求成功", responseDTO);
    }


    /**
     * 获取通知模板id列表
     */
    @ApiOperation(value = "获取所有的通知模板ID", notes = "获取所有的通知模板ID")
    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<List<String>> getNoticeFormID() {
        return new CommonResult<>(200, "获取成功", WechatUtil.getTemplateID());
    }
}
