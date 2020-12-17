package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;
import team.combinatorics.shuwashuwa.model.bo.ActivityTimeSlotBO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ActivityTimeSlotDaoTest {

    @Autowired
    ActivityTimeSlotDao activityTimeSlotDao;

    @Autowired
    MethodsOfTesting methodsOfTesting;

    @Test
    public void simpleTest() {
        methodsOfTesting.truncateAllTables();

        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        List<ActivityTimeSlotBO> activityTimeSlotBOList1 = new ArrayList<>();
        List<ActivityTimeSlotBO> activityTimeSlotBOList2 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            begin.setTimeInMillis(0);
            begin.set(2020, Calendar.DECEMBER, 25, 14 + i, 0, 0);
            end.setTimeInMillis(0);
            end.set(2020, Calendar.DECEMBER, 25, 15 + i, 0, 0);
            ActivityTimeSlotPO activityTimeSlotPO = ActivityTimeSlotPO.builder()
                    .activityId(1)
                    .timeSlot(i)
                    .startTime(new Timestamp(begin.getTimeInMillis()))
                    .endTime(new Timestamp(end.getTimeInMillis()))
                    .build();
            activityTimeSlotDao.insert(activityTimeSlotPO);
            Assert.assertEquals(i, activityTimeSlotPO.getId().intValue());
            ActivityTimeSlotBO activityTimeSlotBO = ActivityTimeSlotBO.builder()
                    .startTime(new Timestamp(begin.getTimeInMillis()))
                    .endTime(new Timestamp(end.getTimeInMillis()))
                    .timeSlot(i)
                    .build();
            activityTimeSlotBOList1.add(activityTimeSlotBO);
        }
        for (int i = 1; i <= 4; i++) {
            begin.setTimeInMillis(0);
            begin.set(2020, Calendar.DECEMBER, 25, 12 + i, 0, 0);
            end.setTimeInMillis(0);
            end.set(2020, Calendar.DECEMBER, 25, 13 + i, 0, 0);
            ActivityTimeSlotPO activityTimeSlotPO = ActivityTimeSlotPO.builder()
                    .activityId(2)
                    .timeSlot(i)
                    .startTime(new Timestamp(begin.getTimeInMillis()))
                    .endTime(new Timestamp(end.getTimeInMillis()))
                    .build();
            activityTimeSlotDao.insert(activityTimeSlotPO);
            Assert.assertEquals(i + 5, activityTimeSlotPO.getId().intValue());
            ActivityTimeSlotBO activityTimeSlotBO = ActivityTimeSlotBO.builder()
                    .startTime(new Timestamp(begin.getTimeInMillis()))
                    .endTime(new Timestamp(end.getTimeInMillis()))
                    .timeSlot(i)
                    .build();
            activityTimeSlotBOList2.add(activityTimeSlotBO);
        }
        Assert.assertArrayEquals(activityTimeSlotBOList1.toArray()
                , activityTimeSlotDao.listTimeSlotsByActivityID(1).toArray());
        Assert.assertArrayEquals(activityTimeSlotBOList2.toArray()
                , activityTimeSlotDao.listTimeSlotsByActivityID(2).toArray());
        Assert.assertEquals(activityTimeSlotBOList1.get(0), activityTimeSlotDao.getTimeSlot(1, 1));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2020, Calendar.DECEMBER, 30, 14, 0, 0);
        // 测试更新
        Assert.assertEquals(1, activityTimeSlotDao.update(1, ActivityTimeSlotBO.builder()
                .timeSlot(2)
                .startTime(new Timestamp(calendar.getTimeInMillis()))
                .endTime(new Timestamp(calendar.getTimeInMillis()))
                .build()));
        Assert.assertEquals(calendar.getTimeInMillis()
                , activityTimeSlotDao.getTimeSlot(1, 2).getEndTime().getTime());
        // 测试删除
        Assert.assertEquals(1, activityTimeSlotDao.deleteOne(1, 1));
        Assert.assertEquals(4, activityTimeSlotDao.deleteByActivityID(1));
        Assert.assertEquals(0, activityTimeSlotDao.deleteByActivityID(1));

    }
}
