package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "测试用接口")
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${spring.datasource.dbcp2.url}")
    String mysqlUrl;

    @Value("${spring.datasource.dbcp2.username}")
    String mysqlUsername;

    @Value("${spring.datasource.dbcp2.password}")
    String mysqlPassword;

    @GetMapping("/env")
    public String testEnv() {
        return "url = " + mysqlUrl + "\n"
                + "username = " + mysqlUsername + "\n"
                + "password = " + mysqlPassword + "\n";
    }

}
