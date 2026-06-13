package cn.lynssen.guanjianjournal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author Lynssen
 * @Create 2026/6/12 19:29
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
