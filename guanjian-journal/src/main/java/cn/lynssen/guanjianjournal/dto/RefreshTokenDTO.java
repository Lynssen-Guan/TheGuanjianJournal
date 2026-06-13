package cn.lynssen.guanjianjournal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author Lynssen
 * @Create 2026/6/12 19:59
 */
@Data
public class RefreshTokenDTO {

    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
