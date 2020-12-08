package team.combinatorics.shuwashuwa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.CachePicCO;
import team.combinatorics.shuwashuwa.model.po.CachePicPO;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class CachePicDaoTest {

    @Autowired
    CachePicDao cachePicDao;

    @Test
    public void simpleTest() {
        int base = 0;

        assertEquals(0, cachePicDao.countCachePic());

        // 插入十张图片
        for (int i = 1; i <= 5; i++) {
            CachePicPO cachePicPO = CachePicPO.builder()
                    .userId(1)
                    .picLocation("Location" + i)
                    .build();
            cachePicDao.insert(cachePicPO);
            if(i==1)
                base = cachePicPO.getId()-1;
            assertEquals(i + base, cachePicPO.getId().intValue());
        }
        for (int i = 1; i <= 5; i++) {
            CachePicPO cachePicPO = CachePicPO.builder()
                    .userId(2)
                    .picLocation("Location" + i + 5)
                    .build();
            cachePicDao.insert(cachePicPO);
            assertEquals(i + base + 5, cachePicPO.getId().intValue());
        }

        for (int i = 1; i <= 5; i++) {
            assertEquals(1, cachePicDao.getCachePicByLocation("Location" + i).getUserId().intValue());
            assertEquals(2, cachePicDao.getCachePicByLocation("Location" + i + 5).getUserId().intValue());
        }
        // 测试selectUSerIDByCacheID
        assertEquals(1, cachePicDao.getUserIDByCacheID(base+2).intValue());
        assertEquals(1, cachePicDao.getUserIDByCacheID(base+5).intValue());
        assertEquals(2, cachePicDao.getUserIDByCacheID(base+6).intValue());
        assertEquals(2, cachePicDao.getUserIDByCacheID(base+9).intValue());

        assertEquals(1, cachePicDao.getCachePicByID(base+1).getUserId().intValue());
        assertEquals(1, cachePicDao.getCachePicByID(base+5).getUserId().intValue());
        assertEquals(2, cachePicDao.getCachePicByID(base+6).getUserId().intValue());

        assertEquals(5, cachePicDao.listCachePicsByCondition(CachePicCO.builder()
                .userId(2)
                .build()).size());

        assertEquals(1, cachePicDao.deleteByID(base+1));
        assertEquals(4, cachePicDao.listCachePicsByCondition(CachePicCO.builder()
                .userId(1)
                .build()).size());
        assertEquals(9, cachePicDao.countCachePic());
        assertEquals(5, cachePicDao.deleteByCondition(CachePicCO.builder().userId(2).build()));
        assertEquals(0, cachePicDao.deleteByID(base+1));
        assertEquals(4, cachePicDao.countCachePic());
        assertEquals(4, cachePicDao.deleteByCondition(CachePicCO.builder().userId(1).build()));


    }
}
