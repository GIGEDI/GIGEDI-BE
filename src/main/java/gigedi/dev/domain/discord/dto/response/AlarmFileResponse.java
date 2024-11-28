package gigedi.dev.domain.discord.dto.response;

import gigedi.dev.domain.file.domain.Authority;

public record AlarmFileResponse(Long authorityId, String fileName, boolean isAlarmActive) {
    public static AlarmFileResponse from(Authority authority) {
        return new AlarmFileResponse(
                authority.getFile().getFileId(),
                authority.getFile().getFileName(),
                authority.isAlarm());
    }
}
