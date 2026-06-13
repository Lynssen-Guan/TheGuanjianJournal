package cn.lynssen.guanjianjournal.service;

import cn.lynssen.guanjianjournal.dto.LoginDTO;
import cn.lynssen.guanjianjournal.dto.RegisterDTO;
import cn.lynssen.guanjianjournal.vo.LoginVO;

/**
 * @Author Lynssen
 * @Create 2026/6/12 20:26
 */

public interface AuthService {

    void register(RegisterDTO registerDTO);

    LoginVO login(LoginDTO loginDTO);
}
