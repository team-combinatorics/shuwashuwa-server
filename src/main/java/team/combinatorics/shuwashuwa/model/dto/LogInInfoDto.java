package team.combinatorics.shuwashuwa.model.dto;

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
public class LogInInfoDto {
    /** 临时登录凭证 */
    private String code;

    /** 用户非敏感信息 */
    private String rawData;

    /** 签名 */
    private String signature;

    /** 用户敏感信息 */
    private String encryptData;

    /** 解密算法的向量 */
    private String iv;

}
