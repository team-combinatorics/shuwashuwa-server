package team.combinatorics.shuwashuwa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceCompleteDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormRejectionDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;

import java.sql.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ServiceEventDaoTest {

    @Autowired
    private ServiceEventDao serviceEventDao;

    @Autowired
    private ServicePicDao servicePicDao;

    @Autowired
    private ServiceFormDao serviceFormDao;

    @Autowired
    private MethodsOfTesting methodsOfTesting;

    /**
     * 测试插入并且添加用于之后测试的数据
     */
    @Before
    @Test
    public void insertTest() {
        methodsOfTesting.truncateAllTables();
        int returnValue;
        // 插入三个事件
        for (int i = 2; i <= 4; i++) {
            returnValue = serviceEventDao.insertByUserID(i);
            Assert.assertEquals(1, returnValue);
        }
        ServiceFormPO serviceFormPO;
        // 插入维修单信息，每个事件插入三个
        // 用户2的维修单id为1 4 7
        // 用户3的维修单id为2 5 8
        // 用户4的维修单id为3 6 9
        for (int i = 0; i < 3; i++)
            for (int j = 2; j <= 4; j++) {
                serviceFormPO = ServiceFormPO.builder()
                        .activityId(1)
                        .timeSlot(1)
                        .serviceEventId(j - 1)
                        .build();
                serviceFormDao.insert(serviceFormPO);
                Assert.assertEquals(i * 3 + (j - 1), serviceFormPO.getId().intValue());
            }
        // TODO 这里可以考虑加上图片的初始信息
    }

    @Test
    public void countServiceEventsByConditionTest() {
        SelectServiceEventCO selectServiceEventCO;
        // 设置条件为用户id
        selectServiceEventCO = SelectServiceEventCO.builder()
                .userId(2)
                .build();
        Assert.assertEquals(1, serviceEventDao.countServiceEventsByCondition(selectServiceEventCO));
        // 设置条件为状态
        selectServiceEventCO = SelectServiceEventCO.builder()
                .status(0)
                .build();
        Assert.assertEquals(3, serviceEventDao.countServiceEventsByCondition(selectServiceEventCO));

    }

    @Test
    public void updateFeedbackTest() {
        serviceEventDao.updateFeedback(1, "志愿者太菜了");
        serviceEventDao.updateFeedback(3, "哈哈哈");
        Assert.assertEquals("志愿者太菜了", serviceEventDao.getServiceEventByID(1).getFeedback());
        Assert.assertEquals("哈哈哈", serviceEventDao.getServiceEventByID(3).getFeedback());
    }

    @Test
    public void updateByVolunteerTest() {
        serviceEventDao.updateByVolunteer(1, 3, "没救了，爬");
        Assert.assertEquals("没救了，爬", serviceEventDao.getServiceEventByID(1).getRepairingResult());
        Assert.assertEquals(3, serviceEventDao.getServiceEventByID(1).getVolunteerId().intValue());
        serviceEventDao.updateByVolunteer(2, 6, "我修不好，我爬");
        Assert.assertEquals("我修不好，我爬", serviceEventDao.getServiceEventByID(2).getRepairingResult());
        Assert.assertEquals(6, serviceEventDao.getServiceEventByID(2).getVolunteerId().intValue());
    }

    @Test
    public void updateStatusTest() {
        int returnValue = serviceEventDao.updateStatus(1, 3);
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals(3, serviceEventDao.getServiceEventByID(1).getStatus().intValue());
    }

    @Test
    public void updateDraftTest() {
        int returnValue = serviceEventDao.updateDraft(2, true);
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals(true, serviceEventDao.getServiceEventByID(2).getDraft());
    }

    @Test
    public void updateClosedTest() {
        int returnValue = serviceEventDao.updateClosed(3, true);
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals(true, serviceEventDao.getServiceEventByID(3).getClosed());
    }


    /**
     * 该方法暂时保留，用于查看输出结果
     * 想查看输出结果时请先注释掉@Ignore后再运行
     */
    @Ignore
    @Test
    public void testFullStruct() {

        // 给维修单2分配图片
        for (int i = 1; i <= 2; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(2).build();
            servicePicDao.insert(servicePicPO);
        }


        // 给维修单3分配图片
        for (int i = 1; i <= 3; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(3).build();
            servicePicDao.insert(servicePicPO);
        }

        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.getServiceEventByID(1));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.listServiceEventsByCondition(
                    SelectServiceEventCO.builder()
                            .activityId(2)
                            .build()
            ));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.listServiceEventsByCondition(
                    SelectServiceEventCO.builder()
                            .volunteerId(1)
                            .build()
            ));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
