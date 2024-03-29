package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationAbstractBO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationDetailBO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAuditDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class VolunteerApplicationDaoTest {
    @Autowired
    VolunteerApplicationDao volunteerApplicationDao;

    @Autowired
    MethodsOfTesting methodsOfTesting;

    @Autowired
    AdminDao adminDao;

    @Before
    @Test
    // 这个测试方法中随便编openid就行，并不向tx服务器验证
    public void insertTest() {
        methodsOfTesting.truncateAllTables();

        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(1)
                .cardPicLocation("Location1")
                .reasonForApplication("我就要当志愿者")
                .build());
        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(1)
                .cardPicLocation("Location1")
                .reasonForApplication("我还要当志愿者")
                .build());
        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(1)
                .cardPicLocation("Location1")
                .reasonForApplication("我非要当志愿者")
                .build());
        volunteerApplicationDao.insert(VolunteerApplicationPO.builder()
                .userId(233)
                .cardPicLocation("Location1")
                .reasonForApplication("就算是死我也要当志愿者")
                .build());

        // 测试获取申请单
        Assert.assertEquals(Integer.valueOf(1), volunteerApplicationDao.getApplicationByFormId(1).getId());

        // 插入管理员信息
        adminDao.insert(AdminPO.builder()
                .userid(2)
                .userName("hkr")
                .studentId("122")
                .phoneNumber("233")
                .identity("?")
                .email("1@1")
                .department("maimai")
                .build());

//        System.out.println(volunteerApplicationDao.listApplicationsByUserId(233));
//        // 测试select多个申请单
//        System.out.println(volunteerApplicationDao.listApplicationsByUserId(1));

    }

    @Test
    public void getApplicationDetailByFormIdTest() {
        VolunteerApplicationDetailBO volunteerApplicationDetailBO;
        volunteerApplicationDetailBO = volunteerApplicationDao.getApplicationDetailByFormId(1);
        Assert.assertEquals(1, volunteerApplicationDetailBO.getUserId().intValue());
        Assert.assertNull(volunteerApplicationDetailBO.getAdminId());
        Assert.assertNull(volunteerApplicationDetailBO.getAdminName());
        Assert.assertEquals(0, volunteerApplicationDetailBO.getStatus().intValue());
        // 管理员添加审核
        int returnValue = volunteerApplicationDao.updateApplicationByAdmin(1, VolunteerApplicationAuditDTO.builder()
                .status(1)
                .replyByAdmin("123")
                .formId(1)
                .build(), volunteerApplicationDetailBO.getUpdatedTime());
        Assert.assertEquals(1, returnValue);
        volunteerApplicationDetailBO = volunteerApplicationDao.getApplicationDetailByFormId(1);
        Assert.assertEquals(1, volunteerApplicationDetailBO.getAdminId().intValue());
    }

    @Test
    public void listApplicationAbstractByConditionTest() {
        List<VolunteerApplicationAbstractBO> volunteerApplicationAbstractBOList;
        SelectApplicationCO selectApplicationCO;
        selectApplicationCO = SelectApplicationCO.builder()
                .userId(1)
                .build();
        volunteerApplicationAbstractBOList = volunteerApplicationDao.listApplicationAbstractByCondition(selectApplicationCO);
        Assert.assertEquals(3, volunteerApplicationAbstractBOList.size());
        // 构造一个条件
        selectApplicationCO = SelectApplicationCO.builder()
                .adminId(1)
                .userId(1)
                .status(0)
                .build();
        volunteerApplicationAbstractBOList = volunteerApplicationDao.listApplicationAbstractByCondition(selectApplicationCO);
        Assert.assertEquals(0, volunteerApplicationAbstractBOList.size());
        // System.out.println(volunteerApplicationAbstractDTOList);
    }

    @Test
    public void updateApplicationByAdminTest() {

        VolunteerApplicationPO volunteerApplicationPO;
        int returnValue;
        // 首先取出对应维修单
        volunteerApplicationPO = volunteerApplicationDao.getApplicationByFormId(1);
        // 得到第一个时间戳
        long timestamp1 = volunteerApplicationPO.getUpdatedTime().getTime();
        // 测试管理员回复
        returnValue = volunteerApplicationDao.updateApplicationByAdmin(1,
                VolunteerApplicationAuditDTO.builder()
                        .formId(1)
                        .replyByAdmin("不给过，爬")
                        .status(1)
                        .build(),
                volunteerApplicationPO.getUpdatedTime());
        Assert.assertEquals(1, returnValue);

        // 再回复一次试试
        returnValue = volunteerApplicationDao.updateApplicationByAdmin(1,
                VolunteerApplicationAuditDTO.builder()
                        .formId(1)
                        .replyByAdmin("不给过，爬 再放送")
                        .status(1)
                        .build(),
                volunteerApplicationPO.getUpdatedTime());
        // 这次应当测试失败
        Assert.assertEquals(0, returnValue);
        volunteerApplicationPO = volunteerApplicationDao.getApplicationByFormId(1);
        returnValue = volunteerApplicationDao.updateApplicationByAdmin(2,
                VolunteerApplicationAuditDTO.builder()
                        .formId(1)
                        .replyByAdmin("不给过，爬")
                        .status(1)
                        .build(),
                volunteerApplicationPO.getUpdatedTime());
        Assert.assertEquals(1, returnValue);
        System.out.println(volunteerApplicationDao.getApplicationByFormId(1));

//        // 测试条件查询
//        System.out.println(volunteerApplicationDao.listApplicationsByCondition(
//                SelectApplicationCO.builder()
//                        .userID(1)
//                        .build()
//        ));
//        System.out.println(volunteerApplicationDao.listApplicationsByCondition(
//                SelectApplicationCO.builder()
//                        .userID(1)
//                        .adminID(1)
//                        .build()
//        ));
//        System.out.println(new Timestamp(new Date().getTime()));
//        System.out.println(volunteerApplicationDao.listApplicationsByCondition(
//                SelectApplicationCO.builder()
//                        .endTime(new Timestamp(new Date().getTime()))
//                        .build()
//        ));
    }
}
