package com.custom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custom.dto.LoginDTO;
import com.custom.dto.RegisterDTO;
import com.custom.entity.User;
import com.custom.mapper.UserMapper;
import com.custom.service.UserService;
import com.custom.utils.JwtUtil;
import com.custom.utils.PasswordUtil;
import com.custom.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())
        );
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        LoginVO.UserVO userVO = new LoginVO.UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setAvatar(user.getAvatar());
        userVO.setRole(user.getRole());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUser(userVO);
        return vo;
    }

    @Override
    public void register(RegisterDTO dto) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())
        );
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setPassword(PasswordUtil.encode(dto.getPassword()));
        user.setRole("user");
        userMapper.insert(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
    }
}
