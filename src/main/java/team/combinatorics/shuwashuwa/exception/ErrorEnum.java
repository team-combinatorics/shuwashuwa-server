package team.combinatorics.shuwashuwa.exception;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    TOKEN_LOST(40001,"当前操作需要token"),
    AUTHORITY_UNMATCHED(40002,"当前账号不具有执行指定操作的权限"),
    WECHAT_SERVER_CONNECTION_FAILURE(40003,"微信服务器连接错误"),
    CODE2SESSION_FAILURE(40004,"用户身份解析错误"),
    TOKEN_EXPIRED(40005,"token过期"),
    TOKEN_INVALID(40006,"token无效");

    private Integer errCode;
    private String errMsg;
}
