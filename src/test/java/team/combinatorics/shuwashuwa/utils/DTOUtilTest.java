package team.combinatorics.shuwashuwa.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;

import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class DTOUtilTest {

    @Test
    public void testConvert() {
        List<TmpPO> list = new Vector<>();
        for (int i = 0; i < 10; i++) {
            TmpDTO dto = new TmpDTO("2020-12-08 07:30:0"+i,Integer.valueOf(10-i).toString());
            TmpPO po = (TmpPO) DTOUtil.convert(dto,TmpPO.class);
            po.id=i;
            Assert.assertEquals(po.data,Integer.valueOf(10-i).toString());
            Assert.assertEquals(po.timestamp,Timestamp.valueOf(dto.timestamp));
            list.add(po);
        }
        List<TmpDTO> dtoList = list.stream().map(x -> (TmpDTO)DTOUtil.convert(x,TmpDTO.class)).collect(Collectors.toList());
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(dtoList.get(i).data,Integer.valueOf(10-i).toString());
            Assert.assertEquals(dtoList.get(i).timestamp,"2020-12-08 07:30:0"+i);
        }
    }

    @Test
    public void testChecking() {
        @AllArgsConstructor
        class TestingClass {
            final Integer a;
            final Integer b;
        }
        TestingClass notNull = new TestingClass(1,1);
        TestingClass partlyNull = new TestingClass(1,null);
        TestingClass allNull = new TestingClass(null,null);

        assert DTOUtil.fieldAllNull(allNull);
        assert DTOUtil.fieldExistNull(allNull);
        assert !DTOUtil.fieldAllNull(partlyNull);
        assert DTOUtil.fieldExistNull(partlyNull);
        assert !DTOUtil.fieldAllNull(notNull);
        assert !DTOUtil.fieldExistNull(notNull);

    }

    @Test
    public void testAdminDTOChecking() {
        // 什么也不填，肯定不行，爪巴
        AdminDTO allNull = AdminDTO.builder()
                .build();
        Assert.assertTrue(DTOUtil.fieldAllNull(allNull));

        // 只填更新信息，我更新谁呢，爪巴
        AdminDTO partNull = AdminDTO.builder()
                .email("114514@1919.810")
                .studentId("1919810")
                .build();
        Assert.assertTrue(DTOUtil.fieldAllNull(partNull));

        // 只填用户id，更新了个寂寞，爪巴
        AdminDTO exceptUseridNull = AdminDTO.builder()
                .userid(114)
                .build();
        Assert.assertTrue(DTOUtil.fieldAllNull(exceptUseridNull));

        // 填了用户id，也填了更新的信息，daisuki！
        AdminDTO correctFormat = AdminDTO.builder()
                .userid(114)
                .email("115514@1919.810")
                .studentId("1919810")
                .build();
        Assert.assertFalse(DTOUtil.fieldAllNull(correctFormat));
    }
}

@NoArgsConstructor
@Data
class TmpPO {
    Integer id;
    Timestamp timestamp;
    String data;
}
@AllArgsConstructor
@NoArgsConstructor
@Data
class TmpDTO {
    String timestamp;
    String data;
}