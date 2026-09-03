package com.edgar.lifeos.domain.publication;

import java.util.List;

/**
 * Publication 仓储契约。
 */
public interface PublicationRepository {

    NotePublication save(NotePublication publication);

    NotePublication findByIdAndUserId(Long id, Long userId);

    NotePublication findByNoteIdAndUserId(Long noteId, Long userId);

    /** 公开访问：按 slug 查找，仅 PUBLIC/UNLISTED，PRIVATE 返回 null。 */
    NotePublication findBySlugIfVisible(String slug);

    List<NotePublication> listByUser(Long userId);

    void deleteById(Long id);
}