package com.edgar.lifeos.domain.planning.todo;

import lombok.Getter;

/**
 * Todo 优先级。
 *
 * <p>数值为排序权重：越小越优先。使用 1/3/5 而非连续值，中间间隔
 * 预留了将来插入中间档位（如 LOW-MEDIUM=2）的扩展空间，避免改动现有值。</p>
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