package com.na.windaicodedev.model.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户登录请求封装类
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 98701238901L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;




}
