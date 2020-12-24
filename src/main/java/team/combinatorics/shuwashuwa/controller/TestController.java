package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.MethodsOfTesting;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.dao.VolunteerDao;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.dto.CommonResult;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerPO;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Api(value = "测试用接口", hidden = true)
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final UserDao userDao;
    private final SuperAdministratorService suService;
    private final VolunteerDao volunteerDao;
    private final UserService userService;
    private final MethodsOfTesting methodsOfTesting;
    @Value("${spring.datasource.dbcp2.url}")
    String mysqlUrl;
    @Value("${spring.datasource.dbcp2.username}")
    String mysqlUsername;
    @Value("${spring.datasource.dbcp2.password}")
    String mysqlPassword;

    @ApiOperation("获取当前数据库地址、用户名、密码，用于查看环境变量是否注入成功")
    @GetMapping("/env")
    public String testEnv() {
        return "url = " + mysqlUrl + "\n"
                + "username = " + mysqlUsername + "\n"
                + "password = " + mysqlPassword + "\n";
    }

    @GetMapping("/exception")
    public void unknownException() {
        throw new RuntimeException("这是一个刻意引发的exception");
    }

    @ApiOperation("hello,world")
    @GetMapping("")
    @NoToken
    public String ordinaryHelloworld() {
        return "Hello, world";
    }

    @ApiOperation(value = "测试token合法性",notes = "token合法才能访问")
    @GetMapping("/auth/default")
    @AllAccess
    public String clientHelloworld() {
        return "Welcome, client!";
    }

    @ApiOperation(value = "测试是否有志愿者权限", notes = "权限正确才能访问")
    @GetMapping("/auth/volunteer")
    @VolunteerAccess
    public String volunteerHelloworld() {
        return "Welcome, volunteer!";
    }

    @ApiOperation(value = "测试是否有管理员权限", notes = "权限正确才能访问")
    @GetMapping("/auth/admin")
    @AdminAccess
    public String adminHelloworld() {
        return "Welcome, admin!";
    }

    @ApiOperation("添加假用户，返回token")
    @GetMapping("/fakeUser")
    @NoToken
    public String addFakeUsers(
            @RequestParam("openid") String openid,
            @RequestParam("volunteer") Boolean volunteer,
            @RequestParam("admin") Boolean admin
    ) {
        userDao.insertUserByOpenid(openid);
        final UserPO user = userDao.getUserByOpenid(openid);
        int userid = user.getId();
        if (volunteer) {
            userDao.updateUserVolunteerAuthority(userid, true);
            volunteerDao.insert(VolunteerPO.builder()
                    .userid(userid)
                    .userName("FAKE")
                    .phoneNumber("1111-1111")
                    .email("fake@shuwa.shuwa")
                    .identity("fake")
                    .studentId("0")
                    .department("nowhere")
                    .build());
        }
        if (admin) {
            suService.addAdministrator(
                    AdminDTO.builder()
                            .userid(userid)
                            .userName("FAKE")
                            .phoneNumber("1111-1111")
                            .email("fake@shuwa.shuwa")
                            .identity("fake")
                            .studentId("0")
                            .department("nowhere")
                            .build()
            );
        }

        return TokenUtil.createToken(userid);
    }

    @ApiOperation("改变当前用户权限")
    @PutMapping("/auth")
    @NoToken
    public String changeAuthority(
            @RequestHeader("token") String token,
            @RequestParam("volunteer") Boolean volunteer,
            @RequestParam("admin") Boolean admin
    ) {
        int userid = TokenUtil.extractUserid(token);
        final UserPO userPO = userDao.getUserByUserid(userid);
        if (volunteer && !userPO.getVolunteer()) {
            userDao.updateUserVolunteerAuthority(userid, true);
            volunteerDao.insert(VolunteerPO.builder()
                    .userid(userid)
                    .userName("FAKE")
                    .phoneNumber("1111-1111")
                    .email("fake@shuwa.shuwa")
                    .identity("fake")
                    .studentId("0")
                    .department("nowhere")
                    .build());
        }
        if (!volunteer && userPO.getVolunteer()) {
            userDao.updateUserVolunteerAuthority(userid,false);
            volunteerDao.deleteByID(volunteerDao.getVolunteerIDByUserID(userid));
        }
        if (admin && !userPO.getAdmin()) {
            suService.addAdministrator(
                    AdminDTO.builder()
                            .userid(userid)
                            .userName("FAKE")
                            .phoneNumber("1111-1111")
                            .email("fake@shuwa.shuwa")
                            .identity("fake")
                            .studentId("0")
                            .department("nowhere")
                            .build()
            );
        }
        if(!admin && userPO.getAdmin()) {
            suService.deleteAdministrator(userid);
        }

        return TokenUtil.createToken(userid);
    }

    /**
     * 删除单个用户，测试用
     */
    @ApiOperation(value = "销号", notes = "并不会删除相关的维修单等数据")
    @RequestMapping(value = "/myself", method = RequestMethod.DELETE)
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

    @ApiOperation(value = "清空数据库")
    @NoToken
    @RequestMapping(value = "/database", method = RequestMethod.DELETE)
    public CommonResult<String> deleteAllUser() {
        System.out.println("即将清空数据库");
        methodsOfTesting.truncateAllTables();
        return new CommonResult<>(200, "删除成功", "Database has been reset.");
    }

}
