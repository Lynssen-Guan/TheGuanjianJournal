package cn.lynssen.guanjianjournal.dao;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Lynssen
 * @Create 2026/6/12 20:42
 */
@Data
@Builder
public class UserDO {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    /**
     * 状态：0禁用 1启用
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 逻辑删除：0未删除 1已删除
     */
    private Integer deleted;
}
