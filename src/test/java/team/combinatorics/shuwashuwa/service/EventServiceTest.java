package team.combinatorics.shuwashuwa.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.MethodsOfTesting;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.ServiceFormDao;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormDTO;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class EventServiceTest {

    @Autowired
    EventService eventService;

    @Autowired
    MethodsOfTesting methodsOfTesting;

    @Autowired
    ServiceEventDao serviceEventDao;

    @Autowired
    ServiceFormDao serviceFormDao;

    @Autowired
    UserDao userDao;

    @Before
    public void beforeTest() {
        methodsOfTesting.truncateAllTables();
        // 插入用户，id应当为2
        userDao.insertUserByOpenid("openid user 2");
        // 插入一个event
        serviceEventDao.insert(ServiceEventPO.builder()
                .userId(2)
                .build());
        // 再插入一个event
        serviceEventDao.insert(ServiceEventPO.builder()
                .userId(2)
                .build());
        serviceEventDao.updateStatus(2, 4);
    }


    @Test
    public void submitFormTest() {
        ServiceEventPO serviceEventPO;
        ServiceFormDTO serviceFormDTO;

        // 首先测试输入为草稿的情况

        // 测试事件为空
        try {
            eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                    .activityId(1)
                    .timeSlot(2)
                    .build(), true);
        } catch (KnownException e) {
            Assert.assertEquals(40010, e.getErrCode().intValue());
        }
        // 测试用户id不对
        try {
            eventService.submitForm(3, ServiceFormSubmitDTO.builder()
                    .serviceEventId(1)
                    .activityId(1)
                    .timeSlot(2)
                    .build(), true);
        } catch (KnownException e) {
            Assert.assertEquals(40009, e.getErrCode().intValue());
        }
        // 测试状态不对
        try {
            eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                    .serviceEventId(2)
                    .activityId(2)
                    .timeSlot(2)
                    .build(), true);
        } catch (KnownException e) {
            Assert.assertEquals(40013, e.getErrCode().intValue());
        }
        // 测试初次添加草稿
        eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                .serviceEventId(1)
                .activityId(2)
                .timeSlot(2)
                .build(), true);
        serviceEventPO = serviceEventDao.getPOByID(1);
        // 检查草稿标记
        Assert.assertEquals(true, serviceEventPO.getDraft());
        // 检查草稿id
        Assert.assertEquals(1, serviceFormDao.getLastFormIDByEventID(1).intValue());
        // 获取草稿form
        serviceFormDTO = serviceFormDao.getServiceFormByFormID(1);
        // 检查其他数据
        Assert.assertEquals(2, serviceFormDTO.getActivityId().intValue());
        // 测试第二次添加草稿
        eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                .serviceEventId(1)
                .activityId(3)
                .timeSlot(2)
                .build(), true);
        serviceEventPO = serviceEventDao.getPOByID(1);
        // 检查草稿标记
        Assert.assertEquals(true, serviceEventPO.getDraft());
        // 检查草稿id
        Assert.assertEquals(1, serviceFormDao.getLastFormIDByEventID(1).intValue());
        // 获取草稿form
        serviceFormDTO = serviceFormDao.getServiceFormByFormID(1);
        // 检查其他数据
        Assert.assertEquals(3, serviceFormDTO.getActivityId().intValue());

        // 下来测试输入为非草稿的情况
        // 测试数据未填完时的情况
        try {
            eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                    .serviceEventId(1)
                    .activityId(1)
                    .timeSlot(2)
                    .build(), false);
            Assert.fail();
        } catch (KnownException e) {
            Assert.assertEquals(40010, e.getErrCode().intValue());
        }
        // 测试覆盖草稿
        eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                .serviceEventId(1)
                .activityId(1)
                .timeSlot(2)
                .brand("美帝良心想")
                .computerModel("小新pro13")
                .cpuModel("Inter i7-10710u")
                .hasDiscreteGraphics(true)
                .graphicsModel("MX250")
                .laptopType("轻薄本")
                .boughtTime("2019-12-1")
                .underWarranty(false)
                .problemDescription("呜呜呜，你们欺负人")
                .problemType("米歇尔问题")
                .imageList(List.of("picture 1", "picture 2"))
                .build(), false);
        // 取出
        serviceEventPO = serviceEventDao.getPOByID(1);
        Assert.assertEquals(false, serviceEventPO.getDraft());
        Assert.assertEquals(1, serviceEventPO.getStatus().intValue());
        Assert.assertEquals(1, serviceEventPO.getValidFormId().intValue());
        Assert.assertEquals(1, serviceEventPO.getActivityId().intValue());
        Assert.assertEquals(2, serviceEventPO.getTimeSlot().intValue());

        // 测试再添加一个草稿
        eventService.submitForm(2, ServiceFormSubmitDTO.builder()
                .serviceEventId(1)
                .activityId(2)
                .timeSlot(2)
                .build(), true);
        serviceEventPO = serviceEventDao.getPOByID(1);
        // 检查草稿标记
        Assert.assertEquals(true, serviceEventPO.getDraft());
        // 检查草稿id
        Assert.assertEquals(2, serviceFormDao.getLastFormIDByEventID(1).intValue());
        // 获取草稿form
        serviceFormDTO = serviceFormDao.getServiceFormByFormID(2);
        // 检查其他数据
        Assert.assertEquals(2, serviceFormDTO.getActivityId().intValue());

    }

}
