package team.combinatorics.shuwashuwa.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.po.UserPO;

/*TODO: 完成对超管服务层功能的测试*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class SuperAdministratorServiceTest {

    @Autowired
    SuperAdministratorService superAdministratorService;

    @Autowired
    UserDao userDao;


    @Test
    public void simpleTest() {
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 2"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 3"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 4"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 5"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 6"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 7"));

        int userID = 3;
        // 测试超管添加管理员的服务
        AdminDTO adminDTO = AdminDTO.builder()
                .userid(Integer.toString(userID))
                .userName("leesou")
                .phoneNumber("1145141919810")
                .email("114514.1919810.inm")
                .identity("chairman")
                .department("eecs")
                .studentId("114514")
                .build();
        Assert.assertEquals(1, superAdministratorService.addAdministrator(adminDTO));
        UserPO userPO = userDao.getUserByUserid(userID);
        // 添加成功，user表中对应的表项也应该被修改
        Assert.assertTrue(userPO.isAdmin());

        // 测试超管获取单个管理员信息的服务
        AdminDTO fetchAdminDTO = superAdministratorService.getAdministratorInfo(userID);
        Assert.assertEquals(adminDTO, fetchAdminDTO);

        // 测试超管获取管理员列表的服务

        // 测试超管修改管理员信息的服务

        // 测试修改信息后对列表的更新

        // 测试新增管理员后对列表的更新

        // 测试删除管理员的服务

        // 测试删除管理员后对列表的更新

    }
}
