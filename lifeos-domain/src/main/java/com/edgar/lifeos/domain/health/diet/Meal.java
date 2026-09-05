package com.edgar.lifeos.domain.health.diet;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * 一餐（Meal）。
 */
@Getter
public class Meal extends AggregateRoot {

    private Long userId;

    private MealType mealType;

    private Instant mealTime;

    private String note;

    private String imageUrl;

    private BigDecimal estimatedCalories;

    /** 对外只读，修改走 addItem */
    @Getter(AccessLevel.NONE)
    private List<FoodItem> items = new ArrayList<>();

    protected Meal() {
    }

    public static Meal create(Long userId, MealType mealType, Instant mealTime, String note, String imageUrl) {
        Meal m = new Meal();
        m.userId = userId;
        m.mealType = mealType;
        m.mealTime = mealTime;
        m.note = note;
        m.imageUrl = imageUrl;
        return m;
    }

    public void addItem(FoodItem item) {
        items.add(item);
    }

    /** 只读视图，外部无法修改 */
    public List<FoodItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}