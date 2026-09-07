package com.snvec.student.modules.user.service;

import com.snvec.student.common.result.ResultCode;
import com.snvec.student.common.exception.BusinessException;
import com.snvec.student.modules.user.dto.LoginDTO;
import com.snvec.student.modules.user.dto.LoginVO;
import com.snvec.student.modules.user.entity.User;
import com.snvec.student.modules.user.mapper.UserMapper;
import com.snvec.student.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    public LoginVO login(LoginDTO loginDTO) {
        // 1. 查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 3. 检查状态
        if (user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 4. 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().getCode());

        // 5. 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 6. 返回结果
        return LoginVO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .role(user.getRole().getCode())
                .avatar(user.getAvatar())
                .build();
    }

    /**
     * 刷新Token
     */
    public LoginVO refreshToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                String newToken = jwtUtil.generateToken(userId, username, role);
                User user = userMapper.selectById(userId);
                return LoginVO.builder()
                        .token(newToken)
                        .tokenType("Bearer")
                        .expiresIn(86400L)
                        .userId(userId)
                        .username(username)
                        .realName(user != null ? user.getRealName() : "")
                        .role(role)
                        .avatar(user != null ? user.getAvatar() : null)
                        .build();
            }
        }
        throw new BusinessException(ResultCode.TOKEN_INVALID);
    }
}
