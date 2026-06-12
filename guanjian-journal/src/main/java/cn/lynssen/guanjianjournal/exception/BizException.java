package cn.lynssen.guanjianjournal.exception;

import cn.lynssen.guanjianjournal.common.ResultCode;
import lombok.Getter;

/**
 * @Author Lynssen
 * @Create 2026/6/11 19:43
 */
@Getter
public class BizException extends RuntimeException{
    private Integer code;
    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.code = code;
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
