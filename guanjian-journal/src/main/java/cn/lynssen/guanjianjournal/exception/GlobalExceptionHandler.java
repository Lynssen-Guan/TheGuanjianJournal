package cn.lynssen.guanjianjournal.exception;

import cn.lynssen.guanjianjournal.common.Result;
import cn.lynssen.guanjianjournal.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Lynssen
 * @Create 2026/6/11 19:50
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleException(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }
}
