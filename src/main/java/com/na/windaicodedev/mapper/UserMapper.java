package com.na.windaicodedev.mapper;

import org.apache.ibatis.annotations.Select;

import com.mybatisflex.core.BaseMapper;
import com.na.windaicodedev.model.entity.User;

/**
 * 用户 映射层。
 *
 * @author <a href="https://natsumeaiovo.github.io">N/A</a>
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户（包括已逻辑删除的记录）
     * @param userAccount 用户账号
     * @return 用户实体
     */
    @Select("SELECT * FROM user WHERE userAccount = #{userAccount}")
    User selectUserByAccountWithDeleted(String userAccount);
}
