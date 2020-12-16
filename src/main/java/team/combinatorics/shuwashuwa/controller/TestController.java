package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Api(value = "测试用接口", hidden = true)
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final UserDao userDao;
    private final SuperAdministratorService suService;
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
        throw new RuntimeException();
    }

    @ApiOperation("hello,world")
    @GetMapping("/auth")
    @NoToken
    public String ordinaryHelloworld() {
        return "Hello, world";
    }

    @ApiOperation("测试身份：所有用户")
    @GetMapping("/auth/default")
    @AllAccess
    public String clientHelloworld() {
        return "Welcome, client!";
    }

    @ApiOperation("测试身份：志愿者")
    @GetMapping("/auth/volunteer")
    @VolunteerAccess
    public String volunteerHelloworld() {
        return "Welcome, volunteer!";
    }

    @ApiOperation("测试身份：管理员")
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
}
