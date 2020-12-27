package team.combinatorics.shuwashuwa.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
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
            TmpPO po = DTOUtil.convert(dto,TmpPO.class);
            po.id=i;
            Assert.assertEquals(po.data,Integer.valueOf(10-i).toString());
            Assert.assertEquals(po.timestamp,Timestamp.valueOf(dto.timestamp));
            list.add(po);
        }
        List<TmpDTO> dtoList = list.stream().map(x -> DTOUtil.convert(x,TmpDTO.class)).collect(Collectors.toList());
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