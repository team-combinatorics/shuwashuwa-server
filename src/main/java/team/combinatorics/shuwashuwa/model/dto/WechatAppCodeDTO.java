package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("用于向微信发送生成签到二维码请求所传输的DTO对象")
public class WechatAppCodeDTO {
    //private String scene;
    //private String page;
    private String path;
    private int width;
    //private boolean auto_color;
    //private Map<String, Integer> line_color;
    //private boolean is_hyalane;
}
