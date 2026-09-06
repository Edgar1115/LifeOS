package com.edgar.lifeos.server;

import com.edgar.lifeos.domain.identity.User;
import com.edgar.lifeos.domain.identity.UserRepository;
import com.edgar.lifeos.domain.identity.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UserRepository 真实数据库持久化集成测试（连 local Docker MySQL 3307）。
 */
@SpringBootTest
@ActiveProfiles("local")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    /** 每个测试用一个独立 openid，避免 openid 唯一索引冲突 */
    private static final String UNIQUE_OPENID = "itest_" + System.nanoTime();

    @Test
    void saveAndFindById() {
        User user = User.create(UNIQUE_OPENID, "union-" + System.nanoTime(), "集成测试用户", "http://avatar/1");

        User saved = userRepository.save(user);

        // 新聚合保存后应有自增 id 和时间戳
        assert saved.getId() != null : "插入后应回填 id";
        assert saved.getCreatedAt() != null : "插入后应回填 createdAt";
        assert saved.getUpdatedAt() != null : "插入后应回填 updatedAt";

        User found = userRepository.findById(saved.getId()).orElseThrow();
        assertEquals(UNIQUE_OPENID, found.getOpenid());
        assertEquals(UserStatus.ACTIVE, found.getStatus());
    }

    @Test
    void saveAndFindByOpenid() {
        String openid = "it2_" + System.nanoTime();
        User user = User.create(openid, null, "查openid", "内置头像");

        User saved = userRepository.save(user);
        User found = userRepository.findByOpenid(openid).orElseThrow();

        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getNickname(), found.getNickname());
    }
}