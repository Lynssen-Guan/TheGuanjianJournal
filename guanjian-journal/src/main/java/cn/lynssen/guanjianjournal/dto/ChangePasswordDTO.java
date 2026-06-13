package cn.lynssen.guanjianjournal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author Lynssen
 * @Create 2026/6/12 19:58
 */
@Data
public class ChangePasswordDTO {

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
