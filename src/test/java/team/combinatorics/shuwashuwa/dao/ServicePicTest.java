package team.combinatorics.shuwashuwa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ServicePicTest {

    @Autowired
    private ServicePicDao servicePicDao;

    @Test
    public void simpleTest() {
        for (int i = 1; i <= 4; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(i).build();
            System.out.println(servicePicPO.getId());
            servicePicDao.insertServicePic(servicePicPO);
            System.out.println(servicePicPO.getId());
        }
        System.out.println(servicePicDao.selectByPicId(2));
        for (int i = 1; i <= 4; i++) {
            ServicePicPO servicePicPO = ServicePicPO.builder().picLocation("location" + i).serviceFormId(5).build();
            servicePicDao.insertServicePic(servicePicPO);
        }
        System.out.println(servicePicDao.selectByServiceFormId(5));

    }
}
