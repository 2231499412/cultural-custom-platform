package com.custom.service;

import com.custom.dto.LoginDTO;
import com.custom.dto.RegisterDTO;
import com.custom.entity.User;
import com.custom.vo.LoginVO;

public interface UserService {

    LoginVO login(LoginDTO dto);

    void register(RegisterDTO dto);

    User getUserByUsername(String username);
}
