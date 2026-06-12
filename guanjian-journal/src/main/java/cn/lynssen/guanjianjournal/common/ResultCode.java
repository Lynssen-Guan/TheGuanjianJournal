package cn.lynssen.guanjianjournal.common;

import lombok.Getter;

/**
 * ClassName: ResultCode
 * Package: cn.lynssen.guanjianjournal.common
 * Description:
 *
 * @Author 小管不要跑
 * @Create 2026/6/11 17:11
 * @Version 1.0
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "success"),
    PARAM_ERROR(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    ;

    private final Integer code;

    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
