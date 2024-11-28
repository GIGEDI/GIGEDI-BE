package gigedi.dev.domain.archive.dto.response;

import gigedi.dev.domain.archive.domain.Archive;

public record ArchiveInfoResponse(Long archiveId, String archiveTitle, int blockCount) {

    public static ArchiveInfoResponse from(Archive archive) {
        return new ArchiveInfoResponse(
                archive.getArchiveId(), archive.getTitle(), archive.getBlockCount());
    }
}
