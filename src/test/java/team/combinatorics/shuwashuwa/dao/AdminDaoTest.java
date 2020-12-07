package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class AdminDaoTest {

    @Autowired
    AdminDao adminDao;

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

        AdminPO adminPO = AdminPO.builder()
                .userid("3")
                .userName("leesou")
                .phoneNumber("1145141919810")
                .email("114514.1919810.inm")
                .identity("chairman")
                .department("eecs")
                .studentId("114514")
                .build();
        // 测试新建管理员与获取管理员信息，Dao层不进行新建管理员时信息是否完整的判断
        Assert.assertEquals(1, adminDao.insert(adminPO));

        // 测试把UserID转换为管理员id
        int adminID = adminDao.getAdminIDByUserID(3);
        Assert.assertEquals(1, adminID);

        // 测试从使用管理员id获取数据库中获取信息
        AdminPO fetch = adminDao.getByID(adminID);
        Assert.assertEquals(adminPO.getUserid(), fetch.getUserid());
        Assert.assertEquals(adminPO.getUserName(), fetch.getUserName());
        Assert.assertEquals(adminPO.getPhoneNumber(), fetch.getPhoneNumber());
        Assert.assertEquals(adminPO.getEmail(), fetch.getEmail());
        Assert.assertEquals(adminPO.getIdentity(), fetch.getIdentity());
        Assert.assertEquals(adminPO.getDepartment(), fetch.getDepartment());
        Assert.assertEquals(adminPO.getStudentId(), fetch.getStudentId());

        // 测试更新管理员信息
        AdminPO update = AdminPO.builder()
                .id(adminID)
                .userName("kinami")
                .email("kinami@misaki.cc")
                .build();
        Assert.assertEquals(1, adminDao.update(update));
        AdminPO fetchUpdate = adminDao.getByID(adminID);
        Assert.assertEquals(update.getId(), fetchUpdate.getId());
        Assert.assertEquals(update.getUserName(), fetchUpdate.getUserName());
        Assert.assertEquals(update.getEmail(), fetchUpdate.getEmail());

        // 测试获取管理员列表

        // 测试删除管理员

    }
}
