package team.combinatorics.shuwashuwa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import team.combinatorics.shuwashuwa.utils.MD5Util;

@SpringBootTest
class ShuwashuwaApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(MD5Util.getMD5("Tsugudaisuki"));
        System.out.println(MD5Util.getMD5("mskcrawl"));

    }

}
