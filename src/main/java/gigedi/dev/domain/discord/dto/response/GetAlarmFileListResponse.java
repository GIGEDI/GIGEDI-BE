package gigedi.dev.domain.discord.dto.response;

import java.util.List;

public record GetAlarmFileListResponse(String discordEmail, List<AlarmFileResponse> alarmList) {}
