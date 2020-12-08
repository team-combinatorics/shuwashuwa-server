package team.combinatorics.shuwashuwa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ShuwashuwaApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    public void contextLoads() {
        assert userDao.getUserByUserid(1)!=null;
    }

}
