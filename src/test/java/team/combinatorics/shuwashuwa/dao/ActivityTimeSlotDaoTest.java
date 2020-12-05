package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;

import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ActivityTimeSlotDaoTest {

    @Autowired
    ActivityTimeSlotDao activityTimeSlotDao;

    @Test
    public void simpleTest() {
        ActivityTimeSlotPO activityTimeSlotPO = ActivityTimeSlotPO.builder()
                .activityId(1)
                .timeSlot(1)
                .startTime(new Timestamp(new Date().getTime()))
                .endTime(new Timestamp(new Date().getTime()))
                .build();
        activityTimeSlotDao.insert(activityTimeSlotPO);
        Assert.assertEquals(1, activityTimeSlotPO.getId().intValue());
    }
}
