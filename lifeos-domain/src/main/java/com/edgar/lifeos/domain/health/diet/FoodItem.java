package com.edgar.lifeos.domain.health.diet;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.math.BigDecimal;

/**
 * 食物条目，属于某一餐。
 */
public class FoodItem extends AggregateRoot {

    private Long mealId;

    private String foodName;

    private String amount;

    private BigDecimal calories;

    private BigDecimal protein;

    private BigDecimal carbohydrate;

    private BigDecimal fat;

    protected FoodItem() {
    }

    public static FoodItem create(Long mealId, String foodName, String amount,
                                  BigDecimal calories, BigDecimal protein, BigDecimal carbohydrate, BigDecimal fat) {
        FoodItem f = new FoodItem();
        f.mealId = mealId;
        f.foodName = foodName;
        f.amount = amount;
        f.calories = calories;
        f.protein = protein;
        f.carbohydrate = carbohydrate;
        f.fat = fat;
        return f;
    }

    public Long getMealId() {
        return mealId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getAmount() {
        return amount;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public BigDecimal getCarbohydrate() {
        return carbohydrate;
    }

    public BigDecimal getFat() {
        return fat;
    }
}