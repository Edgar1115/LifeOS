package com.edgar.lifeos.domain.capture.diary;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * 日记：强日期属性的个人生活记录。
 */
@Getter
public class Diary extends AggregateRoot {

    private Long userId;

    /** 日记所属日期（自然日） */
    private LocalDate diaryDate;

    private String title;

    private String content;

    /** 情绪评分 1-5，可空 */
    private Integer mood;

    /** 对外只读，修改走 update */
    @Getter(AccessLevel.NONE)
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

    /** 只读视图，外部无法修改 */
    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void delete() {
        this.deleted = true;
    }
}