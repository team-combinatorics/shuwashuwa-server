package team.combinatorics.shuwashuwa.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.AdminDao;
import team.combinatorics.shuwashuwa.dao.MethodsOfTesting;
import team.combinatorics.shuwashuwa.dao.VolunteerApplicationDao;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;

/*TODO: 完成对用户服务层的测试*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class UserServiceTest {

    @Autowired
    MethodsOfTesting methodsOfTesting;

    @Autowired
    VolunteerApplicationDao volunteerApplicationDao;

    @Autowired
    UserService userService;

    @Autowired
    AdminDao adminDao;

    @Before
    public void beforeTestMethod() {
        methodsOfTesting.truncateAllTables();

    }

    @Test
    public void completeApplicationAuditionTest() {
        // 插入一个申请
        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(3)
                .cardPicLocation("我没有哈哈哈")
                .reasonForApplication("我就是想当")
                .build());
        // 插入管理员信息
        adminDao.insert(AdminPO.builder()
                .userid(4)
                .department("1")
                .email("1")
                .identity("1")
                .phoneNumber("1")
                .studentId("1")
                .userName("1")
                .build());

        userService.completeApplicationAudition(4, VolunteerApplicationUpdateDTO.builder()
                .formID(1)
                .replyByAdmin("爬")
                .status(2)
                .build());
        try {
            userService.completeApplicationAudition(4, VolunteerApplicationUpdateDTO.builder()
                    .formID(1)
                    .replyByAdmin("爬")
                    .status(2)
                    .build());
        } catch (KnownException ke) {
            Assert.assertEquals(40013, ke.getErrCode().intValue());
        }

    }

    @Test
    public void simpleTest() {

    }
}
