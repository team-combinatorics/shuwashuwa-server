package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.combinatorics.shuwashuwa.model.dto.NoticeMessage;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("用于向微信发送活动通知请求所传输的DTO对象")
public class WechatNoticeDTO {

    @ApiModelProperty("用户的openid")
    private String touser;

    @ApiModelProperty("通知模板id")
    private String template_id;

    @ApiModelProperty("点击通知后要跳转到的页面的url")
    private String page;

    @ApiModelProperty("模板内容")
    private Map<String, NoticeMessage> data;

    @ApiModelProperty("跳转小程序类型，默认正式版")
    private String miniprogram_state;

    @ApiModelProperty("进入小程序查看的语言类型")
    private String lang;
}