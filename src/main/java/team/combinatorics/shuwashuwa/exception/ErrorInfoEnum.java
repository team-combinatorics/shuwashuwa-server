package team.combinatorics.shuwashuwa.exception;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorInfoEnum {
    USER_NOT_EXISTING(40001,"不存在的用户ID"),
    TOKEN_LOST(40002,"当前操作需要token"),
    AUTHORITY_UNMATCHED(40003,"当前账号不具有执行指定操作的权限"),
    WECHAT_SERVER_CONNECTION_FAILURE(40004,"微信服务器连接错误"),
    CODE2SESSION_FAILURE(40005,"微信code校验api出错"),
    TOKEN_EXPIRED(40006,"token过期"),
    TOKEN_INVALID(40007,"token无效"),
    STORAGE_FAILURE(40008,"文件存储失败"),
    IMAGE_NOT_CACHED(40009,"指定的图片路径不在用户上传记录中");

    private final Integer errCode;
    private final String errMsg;
}
