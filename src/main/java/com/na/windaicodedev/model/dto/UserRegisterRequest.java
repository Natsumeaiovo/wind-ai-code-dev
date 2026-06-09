package com.na.windaicodedev.model.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户注册请求封装类
 */
@Data
public class UserRegisterRequest implements Serializable {
    
    private static final long serialVersionUID = 20230824150000L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
