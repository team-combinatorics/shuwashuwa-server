package team.combinatorics.shuwashuwa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.pojo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    public void insertUserTest() {

    }

    @Test
    // 这个测试方法中随便编openid就行，并不向tx服务器验证
    public void addUserOpenidTest() {
        System.out.println(userDao.deleteUserByUserid(1));
        userDao.update(new User(1, null, "misaki"
                , "粉红裸熊", null, null
                , null, null, null, null, null
                , false, false, false));
    }

//    @Test
//    public void deleteUserByUseridTest() {
//        System.out.println(userDao.deleteUserByUserid(1));
//    }
}

