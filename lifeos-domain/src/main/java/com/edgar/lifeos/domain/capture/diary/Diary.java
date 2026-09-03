package com.edgar.lifeos.domain.capture.diary;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 日记：强日期属性的个人生活记录。
 */
public class Diary extends AggregateRoot {

    private Long userId;

    /** 日记所属日期（自然日） */
    private LocalDate diaryDate;

    private String title;

    private String content;

    /** 情绪评分 1-5，可空 */
    private Integer mood;

    private List<String> tags = new ArrayList<>();

    /** 软删除标记 */
    private boolean deleted;

    protected Diary() {
    }

    public static Diary create(Long userId, LocalDate diaryDate, String title, String content, Integer mood, List<String> tags) {
        Diary diary = new Diary();
        diary.userId = userId;
        diary.diaryDate = diaryDate;
        diary.title = title;
        diary.content = content;
        diary.mood = mood;
        diary.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
        return diary;
    }

    public void update(String title, String content, Integer mood, List<String> tags) {
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getDiaryDate() {
        return diaryDate;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getMood() {
        return mood;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }
}