package com.edgar.lifeos.domain.planning.todo;

import lombok.Getter;

/**
 * Todo 优先级。
 */
@Getter
public enum TodoPriority {
    LOW(1),
    MEDIUM(3),
    HIGH(5);

    private final int value;

    TodoPriority(int value) {
        this.value = value;
    }


    public static TodoPriority fromValue(int v) {
        for (TodoPriority p : values()) {
            if (p.value == v) {
                return p;
            }
        }
        throw new IllegalArgumentException("未知优先级: " + v);
    }
}