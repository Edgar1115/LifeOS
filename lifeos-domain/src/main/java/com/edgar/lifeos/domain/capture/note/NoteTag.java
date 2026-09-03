package com.edgar.lifeos.domain.capture.note;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * Note 标签。
 */
public class NoteTag extends AggregateRoot {

    private Long userId;

    private String name;

    protected NoteTag() {
    }

    public static NoteTag create(Long userId, String name) {
        NoteTag tag = new NoteTag();
        tag.userId = userId;
        tag.name = name;
        return tag;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}