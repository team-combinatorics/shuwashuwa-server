package team.combinatorics.shuwashuwa.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorInfoEnum {
    USER_NOT_EXISTING(40001, "不存在的用户ID"),
    TOKEN_LOST(40002, "当前操作需要token"),
    AUTHORITY_UNMATCHED(40003, "当前账号不具有执行指定操作的权限"),
    WECHAT_SERVER_CONNECTION_FAILURE(40004, "微信服务器连接错误"),
    CODE2SESSION_FAILURE(40005, "微信code校验api出错"),
    TOKEN_EXPIRED(40006, "token过期"),
    TOKEN_INVALID(40007, "token无效"),
    STORAGE_FAILURE(40008, "文件存储失败"),
    DATA_NOT_YOURS(40009, "试图访问其他用户的数据"),
    PARAMETER_LACKING(40010, "请求结构中缺少必需的参数"),
    WRONG_SUPER_ADMINISTRATOR_INFO(40011, "超级管理员用户名/密码错误"),
    DUPLICATED_PROMOTION(40012, "用户已具有希望获得的身份"),
    STATUS_UNMATCHED(40013, "当前状态下不允许执行指定操作"),
    ACCESS_TOKEN_FAILURE(40014, "获取微信Access token 出错"),
    SERVICE_TIMEOUT(40015, "服务超时，请重试"),
    FARMING(40016,"禁止刷单"),
    WECHAT_ACCESS_TOKEN_ERROR(40017, "获取微信access token时出错"),
    WECHAT_TEMPLATE_ERROR(40018, "获取通知模板列表时出错"),
    WECHAT_NOTICE_FAILURE(40019, "发送微信通知时出错"),
    WECHAT_QRCODE_FAILURE(40020, "获取微信二维码时出错");

    private final Integer errCode;
    private final String errMsg;
}
