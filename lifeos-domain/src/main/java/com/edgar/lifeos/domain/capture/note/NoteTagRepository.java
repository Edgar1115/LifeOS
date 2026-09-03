package com.edgar.lifeos.domain.capture.note;

import java.util.List;

/**
 * Note 标签仓储契约。
 */
public interface NoteTagRepository {

    NoteTag findByNameAndUserId(Long userId, String name);

    NoteTag save(NoteTag tag);

    List<NoteTag> listByUserId(Long userId);

    List<NoteTag> listByNoteId(Long noteId);

    void linkNoteToTags(Long noteId, List<Long> tagIds);

    void clearNoteTags(Long noteId);
}