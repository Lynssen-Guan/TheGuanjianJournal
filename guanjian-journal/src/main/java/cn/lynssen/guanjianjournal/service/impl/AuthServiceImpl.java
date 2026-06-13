package cn.lynssen.guanjianjournal.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.lynssen.guanjianjournal.common.ResultCode;
import cn.lynssen.guanjianjournal.common.UserStatus;
import cn.lynssen.guanjianjournal.dao.UserDO;
import cn.lynssen.guanjianjournal.dto.LoginDTO;
import cn.lynssen.guanjianjournal.dto.RegisterDTO;
import cn.lynssen.guanjianjournal.exception.BizException;
import cn.lynssen.guanjianjournal.mapper.AuthMapper;
import cn.lynssen.guanjianjournal.service.AuthService;
import cn.lynssen.guanjianjournal.vo.LoginVO;
import cn.lynssen.guanjianjournal.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author Lynssen
 * @Create 2026/6/12 20:32
 */

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public void register(RegisterDTO registerDTO) {
        // 1. 根据用户名查找用户信息
        UserDO userDO = authMapper.selectByUsername(registerDTO.getUsername());
        if (userDO != null) {
            // 2. 如果找到则说明用户已经存在
            throw new BizException(ResultCode.USER_EXIST);
        }
        // 3. 如果没找到直接返回则插入一条新数据
        UserDO insertDO = UserDO.builder()
                .username(registerDTO.getUsername())
                .password(registerDTO.getPassword())
                .nickname(registerDTO.getNickname())
                .status(UserStatus.ENABLE.getCode())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(UserStatus.LOGIC_EXIST.getCode())
                .build();
        int row = authMapper.insertUser(insertDO);
        if(row != 1) {
            row = authMapper.insertUser(insertDO);
            if (row != 1) {
                throw new RuntimeException();
            }
        }

    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        UserDO userDO = authMapper.selectByUsername(loginDTO.getUsername());
        if (userDO == null || UserStatus.LOGIC_DELETE.getCode().equals(userDO.getDeleted())) {
            throw new BizException(ResultCode.USER_NOT_EXIST);
        }
        if (!userDO.getPassword().equals(loginDTO.getPassword())) {
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .build();
        BeanUtil.copyProperties(userDO, userInfoVO);
        return LoginVO.builder()
                .user(userInfoVO)
                .build();
    }
}
