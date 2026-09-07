package com.snvec.student.modules.user.dto;

public class LoginVO {

    private String token;
    private String tokenType;
    private Long expiresIn;
    private Long userId;
    private String username;
    private String realName;
    private String role;
    private String avatar;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public Long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public static LoginVOBuilder builder() {
        return new LoginVOBuilder();
    }

    public static class LoginVOBuilder {
        private final LoginVO vo = new LoginVO();

        public LoginVOBuilder token(String token) { vo.setToken(token); return this; }
        public LoginVOBuilder tokenType(String tokenType) { vo.setTokenType(tokenType); return this; }
        public LoginVOBuilder expiresIn(Long expiresIn) { vo.setExpiresIn(expiresIn); return this; }
        public LoginVOBuilder userId(Long userId) { vo.setUserId(userId); return this; }
        public LoginVOBuilder username(String username) { vo.setUsername(username); return this; }
        public LoginVOBuilder realName(String realName) { vo.setRealName(realName); return this; }
        public LoginVOBuilder role(String role) { vo.setRole(role); return this; }
        public LoginVOBuilder avatar(String avatar) { vo.setAvatar(avatar); return this; }
        public LoginVO build() { return vo; }
    }
}
