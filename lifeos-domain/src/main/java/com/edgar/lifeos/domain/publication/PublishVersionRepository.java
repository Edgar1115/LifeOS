package com.edgar.lifeos.domain.publication;

import java.util.List;

/**
 * 发布版本仓储契约。
 */
public interface PublishVersionRepository {

    NotePublishVersion save(NotePublishVersion version);

    NotePublishVersion findByPublicationAndVersion(Long publicationId, int versionNo);

    /** 最新版本号（用于自增 version_no）。 */
    int maxVersionNo(Long publicationId);

    List<NotePublishVersion> listByPublication(Long publicationId);
}