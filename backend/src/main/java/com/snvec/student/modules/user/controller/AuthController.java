package com.snvec.student.modules.user.controller;

import com.snvec.student.common.result.Result;
import com.snvec.student.common.result.ResultCode;
import com.snvec.student.modules.user.dto.LoginDTO;
import com.snvec.student.modules.user.dto.LoginVO;
import com.snvec.student.modules.user.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    @PostMapping("/refresh")
    public Result<LoginVO> refresh(@RequestHeader("Authorization") String token) {
        return Result.success(authService.refreshToken(token));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("退出成功", null);
    }
}
