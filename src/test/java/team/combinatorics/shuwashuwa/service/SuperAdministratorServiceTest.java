package team.combinatorics.shuwashuwa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;

/*TODO: 完成对超管服务层功能的测试*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class SuperAdministratorServiceTest {

    @Autowired
    SuperAdministratorService superAdministratorService;

    @Test
    public void simpleTest() {

    }
}
