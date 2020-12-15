package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.service.ImageStorageService;

@Api(value = "测试用接口",hidden = true)
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${spring.datasource.dbcp2.url}")
    String mysqlUrl;

    @Value("${spring.datasource.dbcp2.username}")
    String mysqlUsername;

    @Value("${spring.datasource.dbcp2.password}")
    String mysqlPassword;

    @Autowired
    UserDao userDao;

    @Autowired
    ImageStorageService imageStorageService;

    @ApiOperation("获取当前数据库地址、用户名、密码，用于查看环境变量是否注入成功")
    @GetMapping("/env")
    public String testEnv() {
        return "url = " + mysqlUrl + "\n"
                + "username = " + mysqlUsername + "\n"
                + "password = " + mysqlPassword + "\n";
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

    @ApiOperation("添加4个假身份，测试用")
    @GetMapping("/addUser")
    @NoToken
    public String addFakeUsers() {
        userDao.insertUserByOpenid("fake openid 1");
        userDao.insertUserByOpenid("fake openid 2");
        userDao.insertUserByOpenid("fake openid 3");
        userDao.insertUserByOpenid("fake openid 4");
        return "dokidoki";
    }

    @ApiOperation("返回已存储的图片列表")
    @GetMapping("/image")
    @NoToken
    public String[] listImages() {
        return imageStorageService.listImages();
    }
}
