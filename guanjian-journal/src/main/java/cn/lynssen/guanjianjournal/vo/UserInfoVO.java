package cn.lynssen.guanjianjournal.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author Lynssen
 * @Create 2026/6/12 19:39
 */

@Data
@Builder
public class UserInfoVO {

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private Integer status;

}
