package com.edgar.lifeos.infrastructure.repository;

import com.edgar.lifeos.common.time.ClockProvider;
import com.edgar.lifeos.domain.identity.User;
import com.edgar.lifeos.domain.identity.UserRepository;
import com.edgar.lifeos.infrastructure.persistence.mybatis.user.UserDO;
import com.edgar.lifeos.infrastructure.persistence.mybatis.user.UserMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * User 领域仓储的基础设施实现。
 *
 * <p>职责：完成 {@link User} 领域聚合与 {@link UserDO} 数据库对象之间的转换，
 * 具体 SQL 执行委托给 {@link UserMapper}。</p>
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;
    private final ClockProvider clockProvider;

    public UserRepositoryImpl(UserMapper userMapper, ClockProvider clockProvider) {
        this.userMapper = userMapper;
        this.clockProvider = clockProvider;
    }

    @Override
    public User save(User user) {
        UserDO userDO = toDO(user);
        if (userDO.getId() == null) {
            // 新聚合：持久化时补齐聚合坐标。id 由数据库自增回填，创建/更新时间取自时钟
            Instant now = clockProvider.now();
            userDO.setCreatedAt(now);
            userDO.setUpdatedAt(now);
            userMapper.insert(userDO);
            // 回写持久化产生的聚合坐标，保持领域对象与数据库状态一致
            user.setId(userDO.getId());
            user.setCreatedAt(now);
            user.setUpdatedAt(now);
        } else {
            // 已存在聚合：更新并刷新 updatedAt
            Instant now = clockProvider.now();
            userDO.setUpdatedAt(now);
            userMapper.update(userDO);
            user.setUpdatedAt(now);
        }
        return user;
    }

    @Override
    public Optional<User> findByOpenid(String openid) {
        UserDO userDO = userMapper.selectByOpenid(openid);
        return userDO == null ? Optional.empty() : Optional.of(toDomain(userDO));
    }

    @Override
    public Optional<User> findById(Long id) {
        UserDO userDO = userMapper.selectById(id);
        return userDO == null ? Optional.empty() : Optional.of(toDomain(userDO));
    }

    /** 领域 User -> DO */
    private UserDO toDO(User user) {
        UserDO userDO = new UserDO();
        userDO.setId(user.getId());
        userDO.setOpenid(user.getOpenid());
        userDO.setUnionid(user.getUnionid());
        userDO.setNickname(user.getNickname());
        userDO.setAvatarUrl(user.getAvatarUrl());
        userDO.setStatus(user.getStatus());
        userDO.setCreatedAt(user.getCreatedAt());
        userDO.setUpdatedAt(user.getUpdatedAt());
        return userDO;
    }

    /** DO -> 领域 User（用 reconstitute 重建聚合） */
    private User toDomain(UserDO userDO) {
        return User.reconstitute(userDO.getId(), userDO.getOpenid(), userDO.getUnionid(), userDO.getNickname(),
                userDO.getAvatarUrl(), userDO.getStatus(), userDO.getCreatedAt(), userDO.getUpdatedAt());
    }
}