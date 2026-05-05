package com.custom.vo;

import lombok.Data;

@Data
public class LoginVO {

    private String token;
    private UserVO user;

    @Data
    public static class UserVO {
        private Long id;
        private String username;
        private String nickname;
        private String avatar;
        private String role;
    }
}
