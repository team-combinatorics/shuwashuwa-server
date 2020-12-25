package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    // 注释掉的是用于生成小程序码的，目前的实现是生成一个永久可用的二维码，但每次携带参数不同
    // 还需要再去向前端确认

    // @ApiModelProperty("小程序码的附加信息[必填]")
    // private String scene;

    // @ApiModelProperty("扫码后跳转的路径，不可带参数")
    // private String page;

    // @ApiModelProperty("二维码尺寸[选填]")
    // private int width;

    // @ApiModelProperty("是否上色")
    // private boolean auto_color;

    // @ApiModelProperty("RGB值")
    // private Map<String, Integer> line_color;

    // @ApiModelProperty("是否需要透明底色")
    // private boolean is_hyalane;

    @ApiModelProperty("扫码后跳转的路径，可带参数[必填]")
    private String path;

    @ApiModelProperty("二维码尺寸[选填]")
    private int width;
}
