package com.edgar.lifeos.infrastructure.persistence.mybatis.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * User 表 Mapper。
 */
@Mapper
public interface UserMapper {

    int insert(UserDO user);

    UserDO selectById(Long id);

    UserDO selectByOpenid(String openid);

    List<UserDO> selectAll();

    int update(UserDO user);

    int deleteById(Long id);
}