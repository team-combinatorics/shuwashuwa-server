package team.combinatorics.shuwashuwa.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.pojo.NoticeMessage;
import team.combinatorics.shuwashuwa.model.vo.WechatNoticeVO;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class WechatUtilTest {

    @Test
    public void testSendNotice() throws Exception {
        String rescode = "081xGa000FwKOK1ak9200jEE8l4xGa0h";
        String openID = WechatUtil.getOpenID(rescode);

        Map<String, NoticeMessage> data = new HashMap<>();
        data.put("thing4", NoticeMessage.builder()
                .value("45甲331")
                .build());
        data.put("date2", NoticeMessage.builder()
                .value("114年514日8:10")
                .build());
        data.put("date8", NoticeMessage.builder()
                .value("114年514日19:19")
                .build());
        data.put("thing11", NoticeMessage.builder()
                .value("Tsugu")
                .build());
        WechatNoticeVO wechatNoticeVO = WechatNoticeVO.builder()
                .touser(openID)
                .data(data)
                .build();

        WechatUtil.sendActivityNotice(wechatNoticeVO);
    }
}
