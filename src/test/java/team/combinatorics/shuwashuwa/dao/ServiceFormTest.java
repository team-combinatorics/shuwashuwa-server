package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;
import team.combinatorics.shuwashuwa.model.so.ServiceForm;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ServiceFormTest {

    @Autowired
    ServiceFormDao serviceFormDao;

    @Autowired
    ServicePicDao servicePicDao;

    @Autowired
    MethodsOfTesting methodsOfTesting;

    @Before
    public void insertTest() {
        methodsOfTesting.truncateAllTables();
        // 插入三个测试用例
        ServiceFormPO serviceFormPO;
        // 第一条
        serviceFormPO = ServiceFormPO.builder()
                .brand("美帝良心想")
                .computerModel("小新pro13")
                .cpuModel("Inter i7-10710u")
                .hasDiscreteGraphics(true)
                .graphicsModel("MX250")
                .laptopType("轻薄本")
                .boughtTime("2019-12-1")
                .underWarranty(false)
                .problemDescription("没图像，啥都亮。")
                .problemType("硬件问题")
                .activityId(1)
                .timeSlot(1)
                .serviceEventId(1)
                .build();
        serviceFormDao.insert(serviceFormPO);
        Assert.assertEquals(1, serviceFormPO.getId().intValue());
        // 第二条
        serviceFormPO = ServiceFormPO.builder()
                .brand("美帝良心想")
                .computerModel("小新pro13")
                .cpuModel("Inter i7-10710u")
                .hasDiscreteGraphics(true)
                .graphicsModel("MX250")
                .laptopType("轻薄本")
                .boughtTime("2019-12-1")
                .underWarranty(false)
                .problemDescription("快给我过，爬爬爬爬爬")
                .problemType("硬件问题")
                .activityId(1)
                .timeSlot(2)
                .serviceEventId(1)
                .build();
        serviceFormDao.insert(serviceFormPO);
        Assert.assertEquals(2, serviceFormPO.getId().intValue());
        // 第三条
        serviceFormPO = ServiceFormPO.builder()
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
                .activityId(1)
                .timeSlot(3)
                .serviceEventId(1)
                .build();
        serviceFormDao.insert(serviceFormPO);
        Assert.assertEquals(3, serviceFormPO.getId().intValue());
        // 插入一些图片
        ServicePicPO servicePicPO;
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
    }

    @Test
    public void selectTest() {
        ServiceForm serviceForm = serviceFormDao.getServiceFormByFormID(1);
        Assert.assertEquals(1, serviceForm.getTimeSlot().intValue());
        serviceForm = serviceFormDao.getServiceFormByFormID(3);
        Assert.assertEquals(3, serviceForm.getTimeSlot().intValue());
        List<ServiceForm> serviceFormList = serviceFormDao.listServiceFormsByServiceEventID(1);
        Assert.assertEquals(3, serviceFormList.size());
        Assert.assertEquals(3, serviceFormDao.getLastFormIDByEventID(1).intValue());
    }

    @Test
    public void updateTest() {
        ServiceForm oldForm = serviceFormDao.getServiceFormByFormID(1);
        ServiceFormPO serviceFormPO = ServiceFormPO.builder()
                .id(1)
                .brand("联想")
                .computerModel("小新pro13")
                .boughtTime("2012-1-1")
                .activityId(1)
                .timeSlot(3)
                .build();
        serviceFormDao.update(serviceFormPO);
        ServiceForm newForm = serviceFormDao.getServiceFormByFormID(1);
        Assert.assertEquals(1, oldForm.getTimeSlot().intValue());
        Assert.assertEquals(3, newForm.getTimeSlot().intValue());
        Assert.assertEquals("联想", newForm.getBrand());
        Assert.assertEquals("2012-1-1", newForm.getBoughtTime());
    }

    @Test
    public void updateAdviceTest() {
        serviceFormDao.updateAdvice(1, 2, "爬爬爬");
        ServiceForm newForm = serviceFormDao.getServiceFormByFormID(1);
        Assert.assertEquals(2, newForm.getReplyAdminId().intValue());
        Assert.assertEquals("爬爬爬", newForm.getDescriptionAdvice());
    }

    /**
     * 下面这个测试用于print出来进行观察，用的时候请去掉ignore
     */
    @Test
    @Ignore
    public void simpleTest() {
        System.out.println(serviceFormDao.getServiceFormByFormID(1));
    }

}
