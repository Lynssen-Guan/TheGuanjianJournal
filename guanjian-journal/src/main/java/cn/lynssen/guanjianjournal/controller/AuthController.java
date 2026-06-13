package cn.lynssen.guanjianjournal.controller;

import cn.lynssen.guanjianjournal.common.Result;
import cn.lynssen.guanjianjournal.dto.LoginDTO;
import cn.lynssen.guanjianjournal.dto.RegisterDTO;
import cn.lynssen.guanjianjournal.service.AuthService;
import cn.lynssen.guanjianjournal.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Lynssen
 * @Create 2026/6/12 19:18
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid RegisterDTO registerDTO) { // @Valid配合RegisterDTO中的@NotBlank一起使用才可以，没有@Valid的话，那么@NotBLank不会生效
        authService.register(registerDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success(loginVO);
    }
}
