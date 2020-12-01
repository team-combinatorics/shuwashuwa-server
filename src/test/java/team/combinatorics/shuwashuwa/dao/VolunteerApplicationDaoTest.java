package team.combinatorics.shuwashuwa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDTO;

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
        volunteerApplicationDao.insert(233, VolunteerApplicationDTO.builder()
                .reasonForApplication("我就要当志愿者").build());
        System.out.println(volunteerApplicationDao.selectByFormId(1));
        System.out.println(volunteerApplicationDao.selectByUserId(233));
    }
}
