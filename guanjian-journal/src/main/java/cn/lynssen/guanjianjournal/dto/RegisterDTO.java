package cn.lynssen.guanjianjournal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author Lynssen
 * @Create 2026/6/12 20:27
 */
@Data
public class RegisterDTO {

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

    /**
     * 用户昵称
     */
    private String nickname;
}
