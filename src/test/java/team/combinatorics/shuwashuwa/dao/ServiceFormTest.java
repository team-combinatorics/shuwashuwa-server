package team.combinatorics.shuwashuwa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;

import java.sql.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ServiceFormTest {

    @Autowired
    ServiceFormDao serviceFormDao;

    @Autowired
    ServicePicDao servicePicDao;

    @Test
    public void simpleTest() {
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

        serviceFormDao.updateAdvice(233, ServiceFormUpdateDTO.builder()
                .advice("没救了，换电脑吧")
                .formID(1)
                .status(1)
                .build());

        for (int i = 1; i <= 4; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(1).build();
            servicePicDao.insertServicePic(servicePicPO);
        }
        try {
            String json = new ObjectMapper().writeValueAsString(serviceFormDao.selectServiceFormByFormID(1));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
