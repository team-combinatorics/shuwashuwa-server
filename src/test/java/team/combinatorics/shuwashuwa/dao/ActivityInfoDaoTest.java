package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;

import java.sql.Timestamp;
import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ActivityInfoDaoTest {

    @Autowired
    ActivityInfoDao activityInfoDao;

    @Autowired
    MethodsOfTesting methodsOfTesting;

    Calendar begin;

    Calendar end;

    @Before
    @Test
    public void insertTest() {
        methodsOfTesting.truncateAllTables();
        begin = (Calendar.getInstance());
        begin.setTimeInMillis(0);
        end = Calendar.getInstance();
        end.setTimeInMillis(0);

        ActivityInfoPO activityInfoPo;
        // 插入八个活动：
        for (int i = 0; i < 8; i++) {
            begin.set(2020, Calendar.DECEMBER, 20 + i, 10 + i, 0, 0);
            end.set(2020, Calendar.DECEMBER, 20 + i, 13 + i, 59, 59);
            activityInfoPo = ActivityInfoPO.builder()
                    .startTime(new Timestamp(begin.getTimeInMillis()))
                    .endTime(new Timestamp(end.getTimeInMillis()))
                    .location("二教20" + (i + 1))
                    .activityName("电脑小队2020年秋季活动 Vol." + i)
                    .build();
            activityInfoDao.insert(activityInfoPo);
            Assert.assertEquals(i + 1, activityInfoPo.getId().intValue());
        }
    }

    @Test
    public void listByConditionTest() {
        Calendar begin = (Calendar.getInstance());
        begin.setTimeInMillis(0);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(0);

        SelectActivityCO selectActivityCO;
        // 根据开始时间范围
        begin.set(2020, Calendar.DECEMBER, 21, 0, 0, 0);
        end.set(2020, Calendar.DECEMBER, 25, 23, 59, 59);
        selectActivityCO = SelectActivityCO.builder()
                .startTimeUpperBound(new Timestamp(end.getTimeInMillis()))
                .startTimeLowerBound(new Timestamp(begin.getTimeInMillis()))
                .build();
        Assert.assertEquals(5, activityInfoDao.listByCondition(selectActivityCO).size());
        // 根据结束时间范围查找
        begin.set(2020, Calendar.DECEMBER, 21, 0, 0, 0);
        end.set(2020, Calendar.DECEMBER, 23, 23, 59, 59);
        selectActivityCO = SelectActivityCO.builder()
                .endTimeUpperBound(new Timestamp(end.getTimeInMillis()))
                .endTimeLowerBound(new Timestamp(begin.getTimeInMillis()))
                .build();
        Assert.assertEquals(3, activityInfoDao.listByCondition(selectActivityCO).size());
    }
}
