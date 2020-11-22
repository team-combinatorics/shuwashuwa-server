package team.combinatorics.shuwashuwa.controller;

import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.model.bean.CommonResult;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 注册
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public CommonResult<LogInSuccessDto> addUser(@RequestBody LogInInfoDto logInInfoDto) throws Exception {
        System.out.println("新增了一个用户");
        LogInSuccessDto logInSuccessDto = userService.wechatLogin(logInInfoDto);
        return new CommonResult<>(200, "注册成功", logInSuccessDto);
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
