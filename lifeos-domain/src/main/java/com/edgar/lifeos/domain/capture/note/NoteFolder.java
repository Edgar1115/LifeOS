package com.edgar.lifeos.domain.capture.note;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * Note 文件夹。
 */
public class NoteFolder extends AggregateRoot {

    private Long userId;

    private Long parentId;

    private String name;

    private int sortNo;

    protected NoteFolder() {
    }

    public static NoteFolder create(Long userId, Long parentId, String name, int sortNo) {
        NoteFolder folder = new NoteFolder();
        folder.userId = userId;
        folder.parentId = parentId;
        folder.name = name;
        folder.sortNo = sortNo;
        return folder;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void rename(String name) {
        this.name = name;
    }
}