package team.combinatorics.shuwashuwa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;

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
        volunteerApplicationDao.insert(1, VolunteerApplicationDTO.builder()
                .reasonForApplication("我就要当志愿者")
                .build());
        volunteerApplicationDao.insert(1, VolunteerApplicationDTO.builder()
                .reasonForApplication("我还要当志愿者")
                .build());
        volunteerApplicationDao.insert(1, VolunteerApplicationDTO.builder()
                .reasonForApplication("我非要当志愿者")
                .build());
        volunteerApplicationDao.insert(1, VolunteerApplicationDTO.builder()
                .reasonForApplication("就算是死我也要当志愿者")
                .build());
        volunteerApplicationDao.insert(233, VolunteerApplicationDTO.builder()
                .reasonForApplication("我就要当志愿者").build());
        System.out.println(volunteerApplicationDao.selectByFormId(1));
        System.out.println(volunteerApplicationDao.selectByUserId(233));
        // 测试select多个申请单
        System.out.println(volunteerApplicationDao.selectByUserId(1));
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
        System.out.println(volunteerApplicationDao.selectByFormId(1));
        // 测试条件查询
        System.out.println(volunteerApplicationDao.selectByCondition(
                SelectApplicationCO.builder()
                        .userID(1)
                        .build()
        ));
        System.out.println(volunteerApplicationDao.selectByCondition(
                SelectApplicationCO.builder()
                        .userID(1)
                        .adminID(1)
                        .build()
        ));
        System.out.println(new Timestamp(new Date().getTime()));
        System.out.println(volunteerApplicationDao.selectByCondition(
                SelectApplicationCO.builder()
                        .endTime(new Timestamp(new Date().getTime()))
                        .build()
        ));
    }
}
