package team.combinatorics.shuwashuwa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUpdateByVolunteerDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormUpdateDTO;
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

    @Test
    public void simpleTest() {
        ServiceEventPO serviceEventPO = ServiceEventPO.builder()
                .activityId(1)
                .timeSlot(1).build();
        serviceEventDao.insertServiceEvent(1, serviceEventPO);
        System.out.println(serviceEventPO.getId());
        serviceEventDao.updateByVolunteer(1,
                ServiceEventUpdateByVolunteerDTO.builder()
                        .eventID(1)
                        .repairingResult("修不了，告辞")
                        .status(2)
                        .build());
        serviceEventDao.updateAppointment(1, 2, 3);
        serviceEventDao.updateFeedback(1, "志愿者太菜了，建议开除");

    }

    @Test
    public void testFullStruct(){
        // 建立一个维修事件
        ServiceEventPO serviceEventPO = ServiceEventPO.builder()
                .activityId(1)
                .timeSlot(1).build();
        serviceEventDao.insertServiceEvent(1, serviceEventPO);
        System.out.println(serviceEventPO.getId());
        serviceEventDao.updateByVolunteer(1,
                ServiceEventUpdateByVolunteerDTO.builder()
                        .eventID(1)
                        .repairingResult("修不了，告辞")
                        .status(2)
                        .build());
        serviceEventDao.updateAppointment(1, 2, 3);
        serviceEventDao.updateFeedback(1, "志愿者太菜了，建议开除");
        // 建立一个维修单1
        ServiceFormPO serviceFormPO = ServiceFormPO.builder()
                .brand("美帝良心想")
                .computerModel("小新pro13")
                .cpuModel("Inter i7-10710u")
                .hasDiscreteGraphics(true)
                .graphicsModel("MX250")
                .laptopType("轻薄本")
                .boughtTime(Date.valueOf("2019-12-1"))
                .underWarranty(false)
                .problemDescription("没图像，啥都亮")
                .problemType("硬件问题")
                .serviceEventId(1)
                .build();
        serviceFormDao.insertServiceForm(serviceFormPO);
        System.out.println(serviceFormPO.getId());

        // 给维修单1回复
        serviceFormDao.updateAdvice(233, ServiceFormUpdateDTO.builder()
                .advice("没救了，换电脑吧")
                .formID(1)
                .status(1)
                .build());

        // 给维修单1分配图片
        for (int i = 1; i <= 4; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(1).build();
            servicePicDao.insertServicePic(servicePicPO);
        }
        // 建立一个维修单2
        serviceFormPO = ServiceFormPO.builder()
                .brand("美帝良心想")
                .computerModel("小新pro13")
                .cpuModel("Inter i7-10710u")
                .hasDiscreteGraphics(true)
                .graphicsModel("MX250")
                .laptopType("轻薄本")
                .boughtTime(Date.valueOf("2019-12-1"))
                .underWarranty(false)
                .problemDescription("为什么不给我过？爬爬爬爬爬")
                .problemType("管理员有问题")
                .serviceEventId(1)
                .build();
        serviceFormDao.insertServiceForm(serviceFormPO);
        System.out.println(serviceFormPO.getId());

        // 给维修单2回复
        serviceFormDao.updateAdvice(114514, ServiceFormUpdateDTO.builder()
                .advice("就是不给你过，爬爬爬")
                .formID(2)
                .status(1)
                .build());
        // 给维修单2分配图片
        for (int i = 1; i <= 2; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(2).build();
            servicePicDao.insertServicePic(servicePicPO);
        }

        // 建立一个维修单3
        serviceFormPO = ServiceFormPO.builder()
                .brand("美帝良心想")
                .computerModel("小新pro13")
                .cpuModel("Inter i7-10710u")
                .hasDiscreteGraphics(true)
                .graphicsModel("MX250")
                .laptopType("轻薄本")
                .boughtTime(Date.valueOf("2019-12-1"))
                .underWarranty(false)
                .problemDescription("我错了，救救我")
                .problemType("misaki有问题")
                .serviceEventId(1)
                .build();
        serviceFormDao.insertServiceForm(serviceFormPO);
        System.out.println(serviceFormPO.getId());

        // 给维修单2回复
        serviceFormDao.updateAdvice(1919810, ServiceFormUpdateDTO.builder()
                .advice("算了，给你过了")
                .formID(3)
                .status(1)
                .build());
        // 给维修单3分配图片
        for (int i = 1; i <= 3; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(3).build();
            servicePicDao.insertServicePic(servicePicPO);
        }

        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.selectByServiceEventID(1));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.selectByCondition(
                    SelectServiceEventCO.builder()
                            .activityId(2)
                            .build()
            ));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json = new ObjectMapper().writeValueAsString(serviceEventDao.selectByCondition(
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
