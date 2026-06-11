package com.na.windaicodedev.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybatisflex.core.paginate.Page;
import com.na.windaicodedev.annotation.AuthCheck;
import com.na.windaicodedev.common.BaseResponse;
import com.na.windaicodedev.common.DeleteRequest;
import com.na.windaicodedev.common.ResultUtils;
import com.na.windaicodedev.constant.UserConstant;
import com.na.windaicodedev.exception.BusinessException;
import com.na.windaicodedev.exception.ErrorCode;
import com.na.windaicodedev.exception.ThrowUtils;
import com.na.windaicodedev.model.dto.UserAddRequest;
import com.na.windaicodedev.model.dto.UserLoginRequest;
import com.na.windaicodedev.model.dto.UserQueryRequest;
import com.na.windaicodedev.model.dto.UserRegisterRequest;
import com.na.windaicodedev.model.dto.UserUpdateRequest;
import com.na.windaicodedev.model.entity.User;
import com.na.windaicodedev.model.vo.LoginUserVO;
import com.na.windaicodedev.model.vo.UserVO;
import com.na.windaicodedev.service.UserService;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户 控制层。
 *
 * @author <a href="https://natsumeaiovo.github.io">N/A</a>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册。
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        long result = userService.userRegister(request.getUserAccount(), request.getUserPassword(), request.getCheckPassword());
        return ResultUtils.success(result);
    }

    /**
     * 用户登录。
     *
     * @param loginRequest   登录请求
     * @param servletRequest HTTP请求
     * @return 登录用户VO
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest servletRequest) {
        ThrowUtils.throwIf(loginRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        return ResultUtils.success(userService.userLogin(loginRequest.getUserAccount(), loginRequest.getUserPassword(), servletRequest));
    }

    /**
     * 获取登录用户信息
     *
     * @param request HTTP request 对象
     * @return 登录用户信息VO
     */
    @GetMapping("/getLoginUser")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        LoginUserVO loginUserVO = userService.getLoginUserVO(loginUser);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 退出登录
     *
     * @param request HTTP request 对象
     * @return 退出登录结果
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.userLogout(request));
    }

    /**
     * 根据 id 获取脱敏后的用户信息
     *
     * @param id id
     * @return 脱敏后的用户信息
     */
    @GetMapping("/getVO/{id}")
    public BaseResponse<LoginUserVO> getUserVOById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * 管理员添加用户
     *
     * @param userAddRequest 添加用户请求
     * @return 添加用户结果
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        if (user.getUserPassword() == null) {
            // 默认密码 12345678
            final String defaultPassword = "12345678";
            String encryptPassword = userService.getEncryptPassword(defaultPassword);
            user.setUserPassword(encryptPassword);
        }
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 管理员根据 id 获取用户
     *
     * @param id id
     * @return 用户
     */
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 管理员删除用户
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 管理员更新用户
     *
     * @param userUpdateRequest 更新用户请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null || userUpdateRequest.getId() <= 0,
                ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 管理员分页查询用户
     *
     * @param userQueryRequest 分页查询用户请求
     * @return 脱敏后用户列表
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        if (pageNum <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize), userService.getQueryWrapper(userQueryRequest));

        // 数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }
}
