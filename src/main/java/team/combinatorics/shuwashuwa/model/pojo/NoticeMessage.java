package team.combinatorics.shuwashuwa.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("向微信传输通知消息的服务类")
public class NoticeMessage {

    @ApiModelProperty("详细内容")
    private String value;
    //todo: @leesou 检查这个是否应该是dto
}
