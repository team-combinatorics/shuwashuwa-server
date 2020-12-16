package team.combinatorics.shuwashuwa.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.MethodsOfTesting;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.UserPO;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class SuperAdministratorServiceTest {

    @Autowired
    SuperAdministratorService superAdministratorService;

    @Autowired
    UserDao userDao;

    @Autowired
    MethodsOfTesting methodsOfTesting;


    @Test
    public void simpleTest() {
        methodsOfTesting.truncateAllTables();

        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 2"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 3"));
        Assert.assertEquals(1, userDao.insertUserByOpenid("fake openid 4"));

        int userID = userDao.getUserByOpenid("fake openid 3").getId();
        // 测试超管添加管理员的服务
        AdminDTO adminDTO = AdminDTO.builder()
                .userid(userID)
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
        Assert.assertTrue(userPO.getAdmin());

        // 测试超管获取单个管理员信息的服务
        AdminDTO fetchAdminDTO = superAdministratorService.getAdministratorInfo(userID);
        Assert.assertEquals(adminDTO, fetchAdminDTO);

        // 测试超管获取管理员列表的服务
        List<AdminDTO> allAdmins = superAdministratorService.getAdministratorList();
        Assert.assertEquals(1, allAdmins.size());
        fetchAdminDTO = AdminDTO.builder()
                .userid(allAdmins.get(0).getUserid())
                .userName(allAdmins.get(0).getUserName())
                .phoneNumber(allAdmins.get(0).getPhoneNumber())
                .email(allAdmins.get(0).getEmail())
                .identity(allAdmins.get(0).getIdentity())
                .department(allAdmins.get(0).getDepartment())
                .studentId(allAdmins.get(0).getStudentId())
                .build();
        Assert.assertEquals(adminDTO, fetchAdminDTO);

        // 测试超管修改管理员信息的服务
        AdminDTO updateInfo = AdminDTO.builder()
                .userid(userID)
                .userName("kinami")
                .identity("misaki")
                .build();
        Assert.assertEquals(1, superAdministratorService.updateAdministratorInfo(updateInfo));

        // 测试修改信息后对列表中信息的更新
        allAdmins = superAdministratorService.getAdministratorList();
        Assert.assertEquals(1, allAdmins.size());
        adminDTO.setUserName(updateInfo.getUserName());
        adminDTO.setIdentity(updateInfo.getIdentity());
        fetchAdminDTO = AdminDTO.builder()
                .userid(allAdmins.get(0).getUserid())
                .userName(allAdmins.get(0).getUserName())
                .phoneNumber(allAdmins.get(0).getPhoneNumber())
                .email(allAdmins.get(0).getEmail())
                .identity(allAdmins.get(0).getIdentity())
                .department(allAdmins.get(0).getDepartment())
                .studentId(allAdmins.get(0).getStudentId())
                .build();
        Assert.assertEquals(adminDTO, fetchAdminDTO);

        // 测试新增管理员后对列表的更新
        int userID1 = userDao.getUserByOpenid("fake openid 4").getId();
        AdminDTO adminDTO1 = AdminDTO.builder()
                .userid(userID1)
                .userName("leesou")
                .phoneNumber("1145141919810")
                .email("114514.1919810.inm")
                .identity("chairman")
                .department("eecs")
                .studentId("114514")
                .build();
        Assert.assertEquals(1, superAdministratorService.addAdministrator(adminDTO1));
        UserPO userPO1 = userDao.getUserByUserid(userID1);
        Assert.assertTrue(userPO1.getAdmin());
        allAdmins = superAdministratorService.getAdministratorList();
        Assert.assertEquals(2, allAdmins.size());
        fetchAdminDTO = AdminDTO.builder()
                .userid(allAdmins.get(1).getUserid())
                .userName(allAdmins.get(1).getUserName())
                .phoneNumber(allAdmins.get(1).getPhoneNumber())
                .email(allAdmins.get(1).getEmail())
                .identity(allAdmins.get(1).getIdentity())
                .department(allAdmins.get(1).getDepartment())
                .studentId(allAdmins.get(1).getStudentId())
                .build();
        Assert.assertEquals(adminDTO1, fetchAdminDTO);

        // 测试删除管理员的服务
        Assert.assertEquals(1, superAdministratorService.deleteAdministrator(userID));

        // 测试删除管理员后对列表的更新
        allAdmins = superAdministratorService.getAdministratorList();
        Assert.assertEquals(1, allAdmins.size());
        fetchAdminDTO = AdminDTO.builder()
                .userid(allAdmins.get(0).getUserid())
                .userName(allAdmins.get(0).getUserName())
                .phoneNumber(allAdmins.get(0).getPhoneNumber())
                .email(allAdmins.get(0).getEmail())
                .identity(allAdmins.get(0).getIdentity())
                .department(allAdmins.get(0).getDepartment())
                .studentId(allAdmins.get(0).getStudentId())
                .build();
        Assert.assertEquals(adminDTO1, fetchAdminDTO);

        userDao.deleteAllUsers();

    }
}
