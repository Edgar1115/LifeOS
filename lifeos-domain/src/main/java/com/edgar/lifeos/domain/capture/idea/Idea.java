package com.edgar.lifeos.domain.capture.idea;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * Idea：极低成本、极短的信息捕获。
 * 建议内容 <= 500 字（强制 1000 上限）。
 */
public class Idea extends AggregateRoot {

    private Long userId;

    private String content;

    private IdeaStatus status;

    /** 转换目标类型：NOTE / TODO */
    private String convertedType;

    private Long convertedId;

    protected Idea() {
    }

    public static Idea create(Long userId, String content) {
        Idea idea = new Idea();
        idea.userId = userId;
        idea.content = content;
        idea.status = IdeaStatus.RAW;
        return idea;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public IdeaStatus getStatus() {
        return status;
    }

    public String getConvertedType() {
        return convertedType;
    }

    public Long getConvertedId() {
        return convertedId;
    }

    public void organize() {
        if (status == IdeaStatus.RAW || status == IdeaStatus.ARCHIVED) {
            this.status = IdeaStatus.ORGANIZED;
        }
    }

    public void convertTo(String type, Long targetId) {
        this.status = "NOTE".equals(type) ? IdeaStatus.CONVERTED_TO_NOTE : IdeaStatus.CONVERTED_TO_TODO;
        this.convertedType = type;
        this.convertedId = targetId;
    }

    public void archive() {
        this.status = IdeaStatus.ARCHIVED;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    /** 可供事件记录的名称 */
    public String typeName() { return status.name(); }
}