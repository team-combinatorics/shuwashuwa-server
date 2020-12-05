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
        // 插入十张图片
        for (int i = 1; i <= 5; i++) {
            CachePicPO cachePicPO = CachePicPO.builder()
                    .userId(1)
                    .picLocation("Location" + i)
                    .build();
            cachePicDao.insertCachePic(cachePicPO);
            assertEquals(i, cachePicPO.getId().intValue());
        }
        for (int i = 1; i <= 5; i++) {
            CachePicPO cachePicPO = CachePicPO.builder()
                    .userId(2)
                    .picLocation("Location" + i + 5)
                    .build();
            cachePicDao.insertCachePic(cachePicPO);
            assertEquals(i + 5, cachePicPO.getId().intValue());
        }
        // 测试selectUSerIDByCacheID
        assertEquals(1, cachePicDao.selectUserIDByCacheID(2).intValue());
        assertEquals(1, cachePicDao.selectUserIDByCacheID(5).intValue());
        assertEquals(2, cachePicDao.selectUserIDByCacheID(6).intValue());
        assertEquals(2, cachePicDao.selectUserIDByCacheID(9).intValue());

        assertEquals(1, cachePicDao.selectByID(1).getUserId().intValue());
        assertEquals(1, cachePicDao.selectByID(5).getUserId().intValue());
        assertEquals(2, cachePicDao.selectByID(6).getUserId().intValue());

        assertEquals(5, cachePicDao.selectByUserID(2).size());

        assertEquals(1, cachePicDao.deleteByID(1));
        assertEquals(4, cachePicDao.selectByUserID(1).size());
        assertEquals(5, cachePicDao.deleteByCondition(CachePicCO.builder().userId(2).build()));
        assertEquals(0, cachePicDao.deleteByID(1));
        assertEquals(4, cachePicDao.deleteByCondition(CachePicCO.builder().userId(1).build()));


    }
}
