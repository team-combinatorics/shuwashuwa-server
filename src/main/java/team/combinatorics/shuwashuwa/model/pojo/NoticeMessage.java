package team.combinatorics.shuwashuwa.model.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("向微信传输通知消息的服务类")
public class NoticeMessage {
    private String value;
}
