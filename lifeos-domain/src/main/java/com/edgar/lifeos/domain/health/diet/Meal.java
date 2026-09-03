package com.edgar.lifeos.domain.health.diet;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 一餐（Meal）。
 */
public class Meal extends AggregateRoot {

    private Long userId;

    private MealType mealType;

    private Instant mealTime;

    private String note;

    private String imageUrl;

    private BigDecimal estimatedCalories;

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

    public Long getUserId() {
        return userId;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Instant getMealTime() {
        return mealTime;
    }

    public String getNote() {
        return note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getEstimatedCalories() {
        return estimatedCalories;
    }

    public List<FoodItem> getItems() {
        return items;
    }
}