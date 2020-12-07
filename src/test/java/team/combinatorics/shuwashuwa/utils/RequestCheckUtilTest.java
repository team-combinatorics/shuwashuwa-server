package team.combinatorics.shuwashuwa.utils;

import lombok.AllArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;

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

    @Test
    void testAdminDTOChecking()
    {
        AdminDTO allNull = AdminDTO.builder()
                .build();
        Assert.assertTrue(RequestCheckUtil.fieldAllNull(allNull));

        AdminDTO partNull = AdminDTO.builder()
                .email("114514@1919.810")
                .studentId("1919810")
                .build();
        Assert.assertFalse(RequestCheckUtil.fieldAllNull(partNull));

        AdminDTO exceptUseridNull = AdminDTO.builder()
                .userid("114")
                .build();
        Assert.assertTrue(RequestCheckUtil.fieldAllNull(exceptUseridNull));
    }
}
