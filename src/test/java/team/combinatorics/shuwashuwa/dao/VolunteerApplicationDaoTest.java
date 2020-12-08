package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;

import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class VolunteerApplicationDaoTest {
    @Autowired
    VolunteerApplicationDao volunteerApplicationDao;

    @Test
    // 这个测试方法中随便编openid就行，并不向tx服务器验证
    public void insertTest() {
        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(1)
                .cardPicLocation("Location1")
                .reasonForApplication("我就要当志愿者")
                .build());
        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(1)
                .cardPicLocation("Location1")
                .reasonForApplication("我还要当志愿者")
                .build());
        volunteerApplicationDao.insert( VolunteerApplicationPO.builder()
                .userId(1)
                .cardPicLocation("Location1")
                .reasonForApplication("我非要当志愿者")
                .build());
        volunteerApplicationDao.insert( VolunteerApplicationPO.builder()
                .userId(233)
                .cardPicLocation("Location1")
                .reasonForApplication("就算是死我也要当志愿者")
                .build());

        // 测试获取申请单
        Assert.assertEquals(Integer.valueOf(1), volunteerApplicationDao.getApplicationByFormId(1).getId());


        System.out.println(volunteerApplicationDao.listApplicationsByUserId(233));
        // 测试select多个申请单
        System.out.println(volunteerApplicationDao.listApplicationsByUserId(1));
        // 测试管理员回复
        volunteerApplicationDao.updateApplicationByAdmin(1,
                VolunteerApplicationUpdateDTO.builder()
                        .formID(1)
                        .replyByAdmin("不给过，爬")
                        .status(1)
                        .build());
        volunteerApplicationDao.updateApplicationByAdmin(2,
                VolunteerApplicationUpdateDTO.builder()
                        .formID(1)
                        .replyByAdmin("不给过，爬")
                        .status(1)
                        .build());
        System.out.println(volunteerApplicationDao.getApplicationByFormId(1));
        // 测试条件查询
        System.out.println(volunteerApplicationDao.listApplicationsByCondition(
                SelectApplicationCO.builder()
                        .userID(1)
                        .build()
        ));
        System.out.println(volunteerApplicationDao.listApplicationsByCondition(
                SelectApplicationCO.builder()
                        .userID(1)
                        .adminID(1)
                        .build()
        ));
        System.out.println(new Timestamp(new Date().getTime()));
        System.out.println(volunteerApplicationDao.listApplicationsByCondition(
                SelectApplicationCO.builder()
                        .endTime(new Timestamp(new Date().getTime()))
                        .build()
        ));
    }
}
