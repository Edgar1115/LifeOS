package com.edgar.lifeos.domain.capture.note;

import com.edgar.lifeos.common.domain.AggregateRoot;
import lombok.Getter;

/**
 * Note：可长期编辑的 Markdown 知识文档。
 * 发布时使用快照（见 publication 域），与私人编辑版本完全分离。
 */
@Getter
public class Note extends AggregateRoot {

    private Long userId;

    private Long folderId;

    private String title;

    private String contentMarkdown;

    private String excerpt;

    private int wordCount;

    private boolean deleted;

    protected Note() {
    }

    public static Note create(Long userId, Long folderId, String title, String contentMarkdown) {
        Note note = new Note();
        note.userId = userId;
        note.folderId = folderId;
        note.title = title;
        note.contentMarkdown = contentMarkdown;
        note.wordCount = countWords(contentMarkdown);
        return note;
    }

    public void update(Long folderId, String title, String contentMarkdown) {
        this.folderId = folderId;
        this.title = title;
        this.contentMarkdown = contentMarkdown;
        this.excerpt = buildExcerpt(contentMarkdown);
        this.wordCount = countWords(contentMarkdown);
    }

    public void delete() {
        this.deleted = true;
    }








    private static int countWords(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return 0;
        }
        return markdown.trim().split("\\s+").length;
    }

    private static String buildExcerpt(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        String clean = markdown.replaceAll("[#*`>\\[\\]()_~-]", " ").trim();
        return clean.length() > 200 ? clean.substring(0, 200) : clean;
    }
}