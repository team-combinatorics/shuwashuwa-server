package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kinami
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录成功的响应数据")
public class LogInSuccessDto {
    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("是否为第一次使用小程序")
    private boolean isFirstLogin;
}
