package com.custom.controller;

import com.custom.common.Result;
import com.custom.entity.User;
import com.custom.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result<User> getUserInfo(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }
}
