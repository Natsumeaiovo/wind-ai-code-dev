package com.na.windaicodedev.model.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户更新请求封装类
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;   

     /**
      * 用户id
      */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

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
