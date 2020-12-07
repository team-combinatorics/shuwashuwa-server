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
    void testAdminDTOChecking() {
        // 什么也不填，肯定不行，爪巴
        AdminDTO allNull = AdminDTO.builder()
                .build();
        Assert.assertTrue(RequestCheckUtil.fieldAllNull(allNull));

        // 只填更新信息，我更新谁呢，爪巴
        AdminDTO partNull = AdminDTO.builder()
                .email("114514@1919.810")
                .studentId("1919810")
                .build();
        Assert.assertTrue(RequestCheckUtil.fieldAllNull(partNull));

        // 只填用户id，更新了个寂寞，爪巴
        AdminDTO exceptUseridNull = AdminDTO.builder()
                .userid("114")
                .build();
        Assert.assertTrue(RequestCheckUtil.fieldAllNull(exceptUseridNull));

        // 填了用户id，也填了更新的信息，daisuki！
        AdminDTO correctFormat = AdminDTO.builder()
                .userid("114")
                .email("115514@1919.810")
                .studentId("1919810")
                .build();
        Assert.assertFalse(RequestCheckUtil.fieldAllNull(correctFormat));
    }
}
