package com.edgar.lifeos.domain.publication;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 发布版本快照。发布内容与私人 Note 分离，此表保存每次发布的 Markdown 与渲染 HTML。
 */
@Getter
public class NotePublishVersion extends AggregateRoot {

    private Long publicationId;

    private int versionNo;

    private String title;

    private String contentMarkdown;

    private String contentHtml;

    private Instant publishedAt;

    protected NotePublishVersion() {
    }

    public static NotePublishVersion create(Long publicationId, int versionNo, String title,
                                            String contentMarkdown, String contentHtml, Instant now) {
        NotePublishVersion v = new NotePublishVersion();
        v.publicationId = publicationId;
        v.versionNo = versionNo;
        v.title = title;
        v.contentMarkdown = contentMarkdown;
        v.contentHtml = contentHtml;
        v.publishedAt = now;
        return v;
    }






}