package com.edgar.lifeos.domain.capture.note;

import java.util.List;

/**
 * Note 仓储契约。
 */
public interface NoteRepository {

    Note save(Note note);

    Note findByIdAndUserId(Long id, Long userId);

    List<Note> findByUser(Long userId);

    /** 更新排序/层级由 Simple 实现支持。 */
    List<Note> listByFolder(Long userId, Long folderId);

    void deleteById(Long id);
}