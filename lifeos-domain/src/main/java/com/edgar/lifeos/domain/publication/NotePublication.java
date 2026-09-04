package com.edgar.lifeos.domain.publication;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * Note 发布记录。发布内容使用快照（NotePublishVersion），与私人编辑完全分离。
 */
@Getter
public class NotePublication extends AggregateRoot {

    private Long userId;

    private Long noteId;

    private String slug;

    private PublishVisibility visibility;

    private Long currentVersionId;

    private String status;

    private Instant publishedAt;

    protected NotePublication() {
    }

    public static NotePublication create(Long userId, Long noteId, String slug, PublishVisibility visibility) {
        NotePublication p = new NotePublication();
        p.userId = userId;
        p.noteId = noteId;
        p.slug = slug;
        p.visibility = visibility;
        p.status = "DRAFT";
        return p;
    }

    public void publish(Long versionId, Instant now) {
        this.currentVersionId = versionId;
        this.status = "PUBLISHED";
        this.publishedAt = now;
    }

    public void unpublish() {
        this.status = "UNPUBLISHED";
    }

    public void updateVisibility(PublishVisibility visibility) {
        this.visibility = visibility;
    }

    public void updateCurrentVersion(Long versionId) {
        this.currentVersionId = versionId;
    }







}