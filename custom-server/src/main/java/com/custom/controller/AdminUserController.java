package com.custom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.custom.common.PageResult;
import com.custom.common.Result;
import com.custom.entity.User;
import com.custom.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Tag(name = "后台用户管理")
public class AdminUserController {

    private final UserMapper userMapper;

    @GetMapping("/list")
    @Operation(summary = "用户列表")
    public Result<PageResult<User>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<User> page = userMapper.selectPage(
                new Page<>(current, size),
                new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime)
        );
        // 清除密码
        page.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize()));
    }
}
