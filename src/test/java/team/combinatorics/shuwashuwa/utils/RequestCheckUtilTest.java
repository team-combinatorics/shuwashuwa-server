package team.combinatorics.shuwashuwa.utils;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class RequestCheckUtilTest {

    @Test
    void testChecking() {
        @AllArgsConstructor
        class TestingClass {
            final Integer a;
            final Integer b;
        }
        TestingClass notNull = new TestingClass(1,1);
        TestingClass partlyNull = new TestingClass(1,null);
        TestingClass allNull = new TestingClass(null,null);

        assert RequestCheckUtil.fieldAllNull(allNull);
        assert RequestCheckUtil.fieldExistNull(allNull);
        assert !RequestCheckUtil.fieldAllNull(partlyNull);
        assert RequestCheckUtil.fieldExistNull(partlyNull);
        assert !RequestCheckUtil.fieldAllNull(notNull);
        assert !RequestCheckUtil.fieldExistNull(notNull);

    }
}
