package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.vo.WechatNoticeVO;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class WechatUtilTest {

    @Test
    public void testSendNotice() throws Exception {
        String rescode = "061QdCFa123e9A0bdAGa1K7hZ70QdCFb";
        String openID = WechatUtil.getOpenID(rescode);

        Map<String, String> data = new HashMap<>();
        data.put("thing4", "45甲331");
        data.put("date2", "114年514日8:10");
        data.put("date8", "114年514日19:19");
        data.put("thing11", "tsugu!");
        WechatNoticeVO wechatNoticeVO = WechatNoticeVO.builder()
                .touser(openID)
                .data(data)
                .build();

        WechatUtil.sendActivityNotice(wechatNoticeVO);
    }
}
