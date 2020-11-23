package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.Api;
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
@ApiModel("LogInfoDto: 用于登录的数据传输类")
public class LogInInfoDto {
    /** 临时登录凭证 */
    @ApiModelProperty(value = "小程序中调用wx.login()产生的code", required = true)
    private String code;

    /** 用户非敏感信息 */
    @ApiModelProperty(value = "登录时携带的用户非敏感信息")
    private String rawData;

    /** 签名 */
    @ApiModelProperty(value = "签名")
    private String signature;

    /** 用户敏感信息 */
    @ApiModelProperty(value = "加密后的用户敏感信息")
    private String encryptData;

    /** 解密算法的向量 */
    @ApiModelProperty(value = "解密算法的向量")
    private String iv;

}
