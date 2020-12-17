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
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerPO;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

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
    private UserDao userDao;

    @Autowired
    private VolunteerDao volunteerDao;

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
        // 插入三个事件以及用户信息
        for (int i = 2; i <= 4; i++) {
            userDao.insertUserByOpenid("openid" + i);
            userDao.updateUserInfo(i, UserInfoUpdateDTO.builder()
                    .userName("name " + i)
                    .build());
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
                        .computerModel("米歇尔电脑")
                        .serviceEventId(j - 1)
                        .build();
                serviceFormDao.insert(serviceFormPO);
                Assert.assertEquals(i * 3 + (j - 1), serviceFormPO.getId().intValue());
            }
        // 为维修单插入图片
        ServicePicPO servicePicPO;
        // 为维修单1插入图片
        servicePicPO = ServicePicPO.builder()
                .picLocation("在你心里")
                .serviceFormId(1)
                .build();
        servicePicDao.insert(servicePicPO);
        Assert.assertEquals(1, servicePicPO.getId().intValue());
        servicePicPO = ServicePicPO.builder()
                .picLocation("在米歇尔心里")
                .serviceFormId(1)
                .build();
        servicePicDao.insert(servicePicPO);
        Assert.assertEquals(2, servicePicPO.getId().intValue());
        servicePicPO = ServicePicPO.builder()
                .picLocation("在粉红裸熊心里")
                .serviceFormId(1)
                .build();
        servicePicDao.insert(servicePicPO);
        Assert.assertEquals(3, servicePicPO.getId().intValue());
        // 为维修单4插入图片
        servicePicPO = ServicePicPO.builder()
                .picLocation("不在你心里")
                .serviceFormId(4)
                .build();
        servicePicDao.insert(servicePicPO);
        Assert.assertEquals(4, servicePicPO.getId().intValue());
        servicePicPO = ServicePicPO.builder()
                .picLocation("不在米歇尔心里")
                .serviceFormId(4)
                .build();
        servicePicDao.insert(servicePicPO);
        Assert.assertEquals(5, servicePicPO.getId().intValue());
        servicePicPO = ServicePicPO.builder()
                .picLocation("不在粉红裸熊心里")
                .serviceFormId(4)
                .build();
        servicePicDao.insert(servicePicPO);
        Assert.assertEquals(6, servicePicPO.getId().intValue());
        // 插入一个志愿者信息
        volunteerDao.insert(VolunteerPO.builder()
                .userid(5)
                .userName("rinrin开花")
                .build());
        serviceEventDao.updateVolunteerInfo(1, 1);
    }

    /*
        测试条件：
        Timestamp beginTime;
        Timestamp endTime;
        private Integer userId;
        private Integer volunteerId;
        private Integer status;
        private Integer activityId;
        private Integer timeSlot;
        private Boolean draft;
        private Boolean closed;

     */
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
        // 测试一个通用条件
        // 只要没出错应该还好，就简单测试下
        // 构造时间
        Calendar begin = (Calendar.getInstance());
        begin.setTimeInMillis(0);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(0);
        begin.set(2020, Calendar.DECEMBER, 21, 0, 0, 0);
        end.set(2020, Calendar.DECEMBER, 25, 23, 59, 59);
        // 构造条件
        selectServiceEventCO = SelectServiceEventCO.builder()
                .beginTime(new Timestamp(begin.getTimeInMillis()))
                .endTime(new Timestamp(end.getTimeInMillis()))
                .userId(2)
                .volunteerId(2)
                .status(1)
                .activityId(1)
                .timeSlot(1)
                .draft(false)
                .closed(true)
                .status(0)
                .build();
        Assert.assertEquals(0, serviceEventDao.countServiceEventsByCondition(selectServiceEventCO));
    }

    @Test
    public void getServiceEventByIDTest() {
        ServiceEventDetailDTO serviceEventDetailDTO = serviceEventDao.getServiceEventByID(1);
        Assert.assertEquals("name 2", serviceEventDetailDTO.getUserName());
        serviceEventDetailDTO = serviceEventDao.getServiceEventByID(1);
        Assert.assertEquals("rinrin开花", serviceEventDetailDTO.getVolunteerName());

    }

    @Test
    public void updateTest() {
        ServiceEventPO serviceEventPO = ServiceEventPO.builder()
                .id(1)
                .volunteerId(233)
                .repairingResult("result")
                .feedback("233")
                .activityId(1)
                .timeSlot(3)
                .problemSummary("3")
                .validFormId(3)
                .status(6)
                .draft(true)
                .closed(true)
                .build();
        int returnValue = serviceEventDao.update(serviceEventPO);
        Assert.assertEquals(1, returnValue);
        ServiceEventPO modifiedPO = serviceEventDao.getPOByID(1);
        serviceEventPO.setCreateTime(modifiedPO.getCreateTime());
        serviceEventPO.setUpdatedTime(modifiedPO.getUpdatedTime());
        serviceEventPO.setUserId(modifiedPO.getUserId());
        Assert.assertEquals(serviceEventPO, modifiedPO);
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
        serviceEventDao.updateByVolunteer(1, "没救了，爬");
        Assert.assertEquals("没救了，爬", serviceEventDao.getServiceEventByID(1).getRepairingResult());

        serviceEventDao.updateByVolunteer(2, "我修不好，我爬");
        Assert.assertEquals("我修不好，我爬", serviceEventDao.getServiceEventByID(2).getRepairingResult());

    }

    @Test
    public void updateVolunteerInfo() {
        serviceEventDao.updateVolunteerInfo(1, 3);
        Assert.assertEquals(3, serviceEventDao.getServiceEventByID(1).getVolunteerId().intValue());
        serviceEventDao.updateVolunteerInfo(2, 6);
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

    @Test
    public void updateValidFormIDTest() {
        Assert.assertNull(serviceEventDao.getPOByID(1).getValidFormId());
        int returnValue = serviceEventDao.updateValidFormID(1, 4);
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals(4, serviceEventDao.getPOByID(1).getValidFormId().intValue());
    }

    @Test
    public void updateActivityIDAndTimeSlotTest() {
        int returnValue;
        Assert.assertNull(serviceEventDao.getPOByID(1).getActivityId());
        Assert.assertNull(serviceEventDao.getPOByID(1).getTimeSlot());
        returnValue = serviceEventDao.updateActivityIDAndTimeSlot(1, 1, 1);
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals(1, serviceEventDao.getPOByID(1).getActivityId().intValue());
        Assert.assertEquals(1, serviceEventDao.getPOByID(1).getTimeSlot().intValue());
    }

    @Test
    public void updateProblemSummaryTest() {
        int returnValue;
        returnValue = serviceEventDao.updateProblemSummary(1, "没救了");
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals("没救了", serviceEventDao.getPOByID(1).getProblemSummary());
    }

    @Test
    public void getServiceEventForUpdateTest(){
        ServiceEventPO serviceEventPO = serviceEventDao.getServiceEventForUpdate(1);
        Assert.assertNull(serviceEventPO.getActivityId());
        Assert.assertNull(serviceEventPO.getValidFormId());
    }

    @Test
    public void listAbstractServiceEventsByCondition() {
        serviceEventDao.updateValidFormID(1, 4);
        List<ServiceAbstractDTO> serviceAbstractDTOList = serviceEventDao.listAbstractServiceEventsByCondition(
                SelectServiceEventCO.builder()
                        .userId(2)
                        .build()
        );
        Assert.assertEquals(1, serviceAbstractDTOList.size());
        ServiceAbstractDTO serviceAbstractDTO = serviceAbstractDTOList.get(0);
        Assert.assertEquals("米歇尔电脑", serviceAbstractDTO.getComputerModel());
        Assert.assertEquals("name 2", serviceAbstractDTO.getUserName());
        Assert.assertEquals("rinrin开花", serviceAbstractDTO.getVolunteerName());

        serviceAbstractDTOList = serviceEventDao.listAbstractServiceEventsByCondition(
                SelectServiceEventCO.builder()
                        .userId(3)
                        .build()
        );
        Assert.assertNull(serviceAbstractDTOList.get(0).getComputerModel());

        SelectServiceEventCO selectServiceEventCO;
        // 测试一个通用条件
        // 只要没出错应该还好，就简单测试下
        // 构造时间
        Calendar begin = (Calendar.getInstance());
        begin.setTimeInMillis(0);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(0);
        begin.set(2020, Calendar.DECEMBER, 21, 0, 0, 0);
        end.set(2020, Calendar.DECEMBER, 25, 23, 59, 59);
        // 构造条件
        selectServiceEventCO = SelectServiceEventCO.builder()
                .beginTime(new Timestamp(begin.getTimeInMillis()))
                .endTime(new Timestamp(end.getTimeInMillis()))
                .userId(2)
                .volunteerId(2)
                .status(1)
                .activityId(1)
                .timeSlot(1)
                .draft(false)
                .closed(true)
                .status(0)
                .build();
        Assert.assertEquals(0, serviceEventDao.listAbstractServiceEventsByCondition(selectServiceEventCO).size());
    }

    /**
     * 该方法暂时保留，用于查看输出结果
     * 想查看输出结果时请先注释掉@Ignore后再运行
     */
    //@Ignore
    @Test
    public void simpleTest() {
        // 输出维修事件1的详细信息
        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.getServiceEventByID(1));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 输出维修事件的摘要列表
        try {
            String json = new ObjectMapper()
                    .writeValueAsString(serviceEventDao.listAbstractServiceEventsByCondition(new SelectServiceEventCO()));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        serviceEventDao.updateClosed(1, true);
        // 输出条件筛选摘要列表
        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.listAbstractServiceEventsByCondition(
                    SelectServiceEventCO.builder()
                            .closed(true)
                            .build()
            ));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            String json = new ObjectMapper().writeValueAsString(serviceEventDao.listServiceEventsByCondition(
//                    SelectServiceEventCO.builder()
//                            .volunteerId(1)
//                            .build()
//            ));
//            System.out.println(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
