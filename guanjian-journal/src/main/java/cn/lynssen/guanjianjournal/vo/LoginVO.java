package cn.lynssen.guanjianjournal.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author Lynssen
 * @Create 2026/6/12 19:38
 */
@Data
@Builder
public class LoginVO {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 过期时间
     */
    private Long expiresIn;

    private UserInfoVO user;

}
