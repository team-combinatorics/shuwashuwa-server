package team.combinatorics.shuwashuwa.utils;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
