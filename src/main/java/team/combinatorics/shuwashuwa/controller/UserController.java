package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.util.List;

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
            @ApiResponse(code = 200, message = "请求成功")

    })
    @NoToken
    public CommonResult<LogInSuccessDTO> loginHandler(LogInInfoDTO logInInfoDto) throws Exception {
        System.out.println("用户登录");
        System.out.println("Code:" + logInInfoDto.getCode());
        LogInSuccessDTO logInSuccessDto = userService.wechatLogin(logInInfoDto);
        System.out.println("ID: " + TokenUtil.extractUserid(logInSuccessDto.getToken()));
        System.out.println("Token: " + logInSuccessDto.getToken());
        return new CommonResult<>(200, "登录成功", logInSuccessDto);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation(value = "更新用户信息", notes = "根据传入的数据结构对数据库中用户的相应表项进行更新", httpMethod = "PUT")
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功")
    })
    @AllAccess
    public CommonResult<String> updateUserInfo(@RequestHeader("token") String token,
                                               @RequestBody UpdateUserInfoDTO updateUserInfoDto) throws Exception {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("更新" + userid + "的用户信息");
        System.out.println(updateUserInfoDto.toString());
        userService.updateUserInfo(userid, updateUserInfoDto);
        return new CommonResult<>(200, "更新成功", "User's information has been updated!");
    }


    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取当前用户信息", notes = "根据当前token中的userid获取用户信息", httpMethod = "GET")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<UserPO> getUserInfo(@RequestHeader("token") String token) throws Exception {
        int userid = TokenUtil.extractUserid(token);
        UserPO userPO = userService.getUserInfo(userid);
        System.out.println(userid + "请求个人信息");
        return new CommonResult<>(200, "请求成功", userPO);
    }

    /**
     * 接收志愿者申请
     */
    @ApiOperation(value = "接收当前用户的申请", notes = "根据传入的token解析userid，再储存申请表", httpMethod = "POST")
    /*
     * 接口尽量RESTful一些！
     * 接口尽量RESTful一些！
     * 接口尽量RESTful一些！
     * 比如这里用/application + post方法来表示添加数据
     */
    @RequestMapping(value = "/application", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<String> receiveApplicationInfo(@RequestHeader("token") String token,
                                                       @RequestBody VolunteerApplicationDTO application) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println(userid+"提交了志愿者申请");
        userService.addVolunteerApplication(userid,application);
        return new CommonResult<>(200,"申请完成","posted");
    }

    /**
     * 获取待审核志愿者申请
     */
    @ApiOperation(value = "获取待审核志愿者申请", notes = "限管理员调用，返回List", httpMethod = "GET")
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AdminAccess
    public CommonResult<List<VolunteerApplicationPO>> getUnauditedApplicationList() {
        return new CommonResult<>(200,"请求成功",userService.getUnauditedVolunteerApplicationList());
    }

    /**
     * 处理志愿者申请的审核
     */
    @ApiOperation(value = "审核志愿者申请", notes = "限管理员调用", httpMethod = "PATCH")
    @RequestMapping(value = "/application", method = RequestMethod.PATCH)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AdminAccess
    public CommonResult<String> receiveApplicationAudition(@RequestHeader("token") String token,
                                                           @RequestBody VolunteerApplicationUpdateDTO updateDTO) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println(userid+"审核了编号为"+updateDTO.getFormID()+"的申请");
        userService.completeApplicationAudition(userid,updateDTO);
        return new CommonResult<>(200,"请求成功","success");
    }


    /**
     * 删除单个用户，测试用
     */
    @ApiOperation(value = "删除单个用户", notes = "删除单个用户，测试用", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 40001, message = "用户不存在")
    })
    @RequestMapping(value = "/deleteOne", method = RequestMethod.DELETE)
    @NoToken
    public CommonResult<String> deleteOneUser(@RequestHeader("token") String token) throws Exception {
        int cnt = userService.deleteOneUser(TokenUtil.extractUserid(token));
        if (cnt > 0) {
            return new CommonResult<>(200, "删除成功", "If success, you can receive this message.");
        }
        return new CommonResult<>(40001, "不存在的用户ID", "You have deleted a ghost user!");
    }

    /**
     * 删除所有用户，测试用
     */
    @ApiOperation(value = "删除所有用户", notes = "删除所有用户，测试用", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
    })
    @NoToken
    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public CommonResult<String> deleteAllUser() {
        System.out.println("即将删除所有用户信息");
        userService.deleteAllUsers();
        return new CommonResult<>(200, "删除成功", "All users have been deleted.");
    }

}