package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    public void weakTest() {
        userDao.insertUserByOpenid("fake openid 1");
        userDao.insertUserByOpenid("fake openid 2");
        userDao.insertUserByOpenid("fake openid 3");
        int ans = userDao.insertUserByOpenid("fake openid 4");
        Assert.assertEquals(1, ans);
        // 更改一个用户信息测试
        userDao.updateUserInfo(1, UpdateUserInfoDTO.builder()
                .userName("misaki")
                .build());

        // 根据openid查找测试
        System.out.println(userDao.getUserByOpenid("fake openid 1"));
        // 根据userid查找测试
        System.out.println(userDao.getUserByUserid(1));
        System.out.println(userDao.deleteUserByOpenid("fake openid 2"));
        System.out.println(userDao.deleteUserByUserid(3));
        userDao.insertUserByOpenid("fake openid 5");
        userDao.insertUserByOpenid("fake openid 6");
        userDao.updateUserVolunteerAuthority(4, true);
        userDao.updateUserAdminAuthority(4, true);
        System.out.println(userDao.getUserByUserid(4));
        System.out.println(userDao.deleteAllUsers());

    }



}

