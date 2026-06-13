package cn.lynssen.guanjianjournal.common;

import lombok.Getter;

/**
 * @Author Lynssen
 * @Create 2026/6/12 23:36
 */
@Getter
public enum UserStatus {

    ENABLE(1),
    DISABLE(0),
    LOGIC_EXIST(0),
    LOGIC_DELETE(1);

    private Integer code;

    private UserStatus(Integer code) {
        this.code = code;
    }
}
