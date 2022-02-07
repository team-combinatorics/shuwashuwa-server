package team.combinatorics.shuwashuwa.utils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.NoticeMessage;
import team.combinatorics.shuwashuwa.model.dto.WechatNoticeDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static team.combinatorics.shuwashuwa.utils.WechatUtil.getTemplateListAutomatically;

// 为了节省微信接口的调用次数，这个测试应该被忽略掉，只在本地按需测试
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
@Ignore
public class WechatUtilTest {
    // 填你的OpenID
    private final static String openID = "";

    @Test
    public void testGetTemplateId() throws Exception {
        List<String> IDs = WechatUtil.getTemplateID();
        for(String ID:IDs) {
            System.out.println(ID);
        }
    }

    @Test
    public void testGetQRCode() throws Exception {
        WechatUtil.generateAppCode(1);
    }

    @Test
    public void testGetOpenID() throws Exception {
        String rescode = "";
        try {
            String openID = WechatUtil.getOpenID(rescode);
        } catch (KnownException e) {
            Assert.assertEquals(e.getErrCode(), Integer.valueOf(40005));
        }
    }

    @Test
    public void testSendActivityNotice() throws Exception {
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
        WechatNoticeDTO wechatNoticeDTO = WechatNoticeDTO.builder()
                .touser(openID)
                .data(data)
                .build();

        WechatUtil.sendNotice(wechatNoticeDTO, WechatUtil.WechatNoticeType.ACTIVITY_NOTICE);
    }

    @Test
    public void testSendAuditionNotice() throws Exception {
        Map<String, NoticeMessage> data = new HashMap<>();
        data.put("phrase5", NoticeMessage.builder()
                .value("猜猜看啊")
                .build());
        data.put("thing8", NoticeMessage.builder()
                .value("我也不知道你通没通过")
                .build());
        data.put("thing13", NoticeMessage.builder()
                .value("电脑坏了")
                .build());
        WechatNoticeDTO wechatNoticeDTO = WechatNoticeDTO.builder()
                .touser(openID)
                .data(data)
                .build();

        WechatUtil.sendNotice(wechatNoticeDTO, WechatUtil.WechatNoticeType.AUDIT_NOTICE);
    }

    @Test
    public void testSendVolunteerNotice() throws Exception {
        Map<String, NoticeMessage> data = new HashMap<>();
        data.put("phrase5", NoticeMessage.builder()
                .value("猜猜看啊")
                .build());
        data.put("thing8", NoticeMessage.builder()
                .value("我想草草")
                .build());
        data.put("thing13", NoticeMessage.builder()
                .value("我觉得很好")
                .build());
        WechatNoticeDTO wechatNoticeDTO = WechatNoticeDTO.builder()
                .touser(openID)
                .data(data)
                .build();

        WechatUtil.sendNotice(wechatNoticeDTO, WechatUtil.WechatNoticeType.VOLUNTEER_NOTICE);
    }

    @Test
    public void testSendTakeOrderNotice() throws Exception {
        Map<String, NoticeMessage> data = new HashMap<>();
        data.put("thing2", NoticeMessage.builder()
                .value("草草")
                .build());
        data.put("thing3", NoticeMessage.builder()
                .value("要温柔的摸茨菇哦")
                .build());
        WechatNoticeDTO wechatNoticeDTO = WechatNoticeDTO.builder()
                .touser(openID)
                .data(data)
                .build();

        WechatUtil.sendNotice(wechatNoticeDTO, WechatUtil.WechatNoticeType.TAKE_NOTICE);
    }
}
