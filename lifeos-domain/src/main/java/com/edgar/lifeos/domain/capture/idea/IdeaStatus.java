package com.edgar.lifeos.domain.capture.idea;

/**
 * Idea 状态机。
 */
public enum IdeaStatus {
    RAW,
    ORGANIZED,
    CONVERTED_TO_NOTE,
    CONVERTED_TO_TODO,
    ARCHIVED
}