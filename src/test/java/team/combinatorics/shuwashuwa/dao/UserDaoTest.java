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
        // 插入单个用户的测试
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 1"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 2"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 3"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 4"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 5"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 6"));

        // 更改一个用户信息的测试，同时测试利用openid和userid来查找这个被修改的用户，并获取被修改的信息
        // 更改userName
        userDao.updateUserInfo(1, UpdateUserInfoDTO.builder()
                .userName("misaki")
                .build());
        Assert.assertEquals("misaki", userDao.getUserByUserid(1).getUserName());
        Assert.assertEquals("misaki", userDao.getUserByOpenid("fake openid 1").getUserName());

        // 更改comment
        userDao.updateUserInfo(1, UpdateUserInfoDTO.builder()
                .comment("Happy! Lucky! Smile! Yeah!")
                .build());
        Assert.assertEquals("Happy! Lucky! Smile! Yeah!", userDao.getUserByUserid(1).getComment());
        Assert.assertEquals("Happy! Lucky! Smile! Yeah!", userDao.getUserByOpenid("fake openid 1").getComment());

        // 同时更改多个属性
        userDao.updateUserInfo(2, UpdateUserInfoDTO.builder()
                .email("114514@1919.810")
                .comment("24 years old, a student.")
                .build());
        Assert.assertEquals("114514@1919.810", userDao.getUserByUserid(2).getEmail());
        Assert.assertEquals("114514@1919.810", userDao.getUserByOpenid("fake openid 2").getEmail());
        Assert.assertEquals("24 years old, a student.", userDao.getUserByUserid(2).getComment());
        Assert.assertEquals("24 years old, a student.", userDao.getUserByOpenid("fake openid 2").getComment());

        // 更改用户权限的测试--志愿者
        userDao.updateUserVolunteerAuthority(6, true);
        Assert.assertTrue(userDao.getUserByUserid(6).isVolunteer());
        Assert.assertTrue(userDao.getUserByOpenid("fake opnid 6").isVolunteer());

        // 更改用户权限的测试--管理员
        userDao.updateUserAdminAuthority(6, true);
        Assert.assertTrue(userDao.getUserByUserid(6).isAdmin());
        Assert.assertTrue(userDao.getUserByOpenid("fake opnid 6").isAdmin());

        // 删除单个用户的测试--使用userid
        Assert.assertEquals(1, userDao.deleteUserByUserid(1)); //删除一个已存在的用户
        Assert.assertEquals(0, userDao.deleteUserByUserid(1)); //删除一个不存在（已被删除过）的用户

        // 删除单个用户的测试--使用openid
        Assert.assertEquals(1, userDao.deleteUserByOpenid("fake openid 2"));
        Assert.assertEquals(0, userDao.deleteUserByOpenid("114514 1919810"));

        //删除所有用户的测试
        Assert.assertEquals(1, userDao.deleteAllUsers());
    }



}

