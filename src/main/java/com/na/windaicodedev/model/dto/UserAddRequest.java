package com.na.windaicodedev.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加请求封装类
 */
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
    
    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色
     */
    private String userRole;
}
