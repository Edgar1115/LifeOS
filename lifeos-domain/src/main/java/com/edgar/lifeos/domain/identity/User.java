package com.edgar.lifeos.domain.identity;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * 用户聚合。
 */
public class User extends AggregateRoot {

    /** 微信 openid，同一公众号/小程序下唯一 */
    private String openid;

    /** 微信 unionid（跨公众号/小程序唯一，可空） */
    private String unionid;

    private String nickname;

    private String avatarUrl;

    /** 状态，见 {@link UserStatus} */
    private UserStatus status;

    protected User() {
    }

    public static User create(String openid, String unionid, String nickname, String avatarUrl) {
        User user = new User();
        user.openid = openid;
        user.unionid = unionid;
        user.nickname = nickname;
        user.avatarUrl = avatarUrl;
        user.status = UserStatus.ACTIVE;
        return user;
    }

    public String getOpenid() {
        return openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void updateProfile(String nickname, String avatarUrl) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
    }
}