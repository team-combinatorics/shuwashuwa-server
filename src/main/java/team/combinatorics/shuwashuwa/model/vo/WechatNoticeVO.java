package team.combinatorics.shuwashuwa.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.combinatorics.shuwashuwa.model.pojo.NoticeMessage;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("用于向微信发送活动通知请求所传输的VO对象")
public class WechatNoticeVO {
    private String touser;
    private String template_id;
    private String page;
    private Map<String, NoticeMessage> data;
    private String miniprogram_state;
    private String lang;
}
