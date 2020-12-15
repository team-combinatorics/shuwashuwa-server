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
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ServicePicDaoTest {

    @Autowired
    MethodsOfTesting methodsOfTesting;
    @Autowired
    private ServicePicDao servicePicDao;

    @Before
    @Test
    public void insertTest() {
        methodsOfTesting.truncateAllTables();
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
    public void listServicePicsByFormIdTest(){
        List<String> locationList = servicePicDao.listServicePicsByFormId(1);
        Assert.assertEquals(3, locationList.size());
        Assert.assertEquals("在粉红裸熊心里", locationList.get(2));
    }

    /**
     * 下面这个测试用于print出来进行观察，用的时候请去掉ignore
     */
    @Ignore
    @Test
    public void simpleTest() {
        System.out.println(servicePicDao.listServicePicsByFormId(1));
        System.out.println(servicePicDao.deleteByPicId(1));
        System.out.println(servicePicDao.deleteByPicId(1));
        System.out.println(servicePicDao.deleteByServiceFormId(5));

    }
}
