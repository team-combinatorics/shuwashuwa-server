package team.combinatorics.shuwashuwa.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.po.VolunteerPO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class VolunteerDaoTest {

    @Autowired
    VolunteerDao volunteerDao;

    @Autowired
    MethodsOfTesting methodsOfTesting;

    @Before
    @Test
    public void insertTest() {
        methodsOfTesting.truncateAllTables();
        VolunteerPO volunteerPO = VolunteerPO.builder()
                .userid(2)
                .build();
        volunteerDao.insert(volunteerPO);
        Assert.assertEquals(1, volunteerPO.getId().intValue());
    }
}
