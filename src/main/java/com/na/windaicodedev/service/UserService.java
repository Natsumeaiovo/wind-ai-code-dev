package com.na.windaicodedev.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.na.windaicodedev.model.dto.UserQueryRequest;
import com.na.windaicodedev.model.entity.User;
import com.na.windaicodedev.model.vo.LoginUserVO;

import com.na.windaicodedev.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://natsumeaiovo.github.io">N/A</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * 
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 注册结果，成功返回用户ID，失败返回-1
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * 
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      HTTP request 对象
     * @return 登录用户信息VO
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 根据 User 获取脱敏登录用户信息VO
     * 
     * @param user 用户实体
     * @return 登录用户信息VO
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取登录用户信息
     * 
     * @param request HTTP request 对象
     * @return 登录用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 根据用户实体获取脱敏用户信息VO
     *
     * @param user 用户实体
     * @return 脱敏用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 根据用户列表获取脱敏用户信息VO列表
     *
     * @param userList 用户列表
     * @return 脱敏用户列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 退出登录
     * 
     * @param request HTTP request 对象
     * @return 退出登录结果
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取加密后的密码
     * 
     * @param password 密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String password);

    /**
     * 根据查询请求获取查询条件 QueryWrapper
     *
     * @param userQueryRequest 用户查询条件
     * @return 查询条件 QueryWrapper
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);
}
