package gigedi.dev.domain.discord.api;

import org.springframework.web.bind.annotation.*;

import gigedi.dev.domain.discord.application.AlarmService;
import gigedi.dev.domain.discord.dto.response.AlarmFileResponse;
import gigedi.dev.domain.discord.dto.response.GetAlarmFileListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Alarm", description = "Alarm 관련 API")
@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @Operation(summary = "알람 정보 파일 조회", description = "알람이 설정되어있는 파일 리스틀를 조회하는 API")
    @GetMapping("/discord")
    public GetAlarmFileListResponse getAlarmFileList() {
        return alarmService.getAlarmFileList();
    }

    @Operation(summary = "알람 설정 활성화", description = "특정 파일의 알람을 활성화하는 API")
    @PutMapping("/{fileId}/active")
    public AlarmFileResponse activateFileAlarm(@PathVariable Long fileId) {
        return alarmService.changeAlarmActive(fileId);
    }

    @Operation(summary = "알람 설정 비활성화", description = "특정 파일의 알람을 비활성화하는 API")
    @PutMapping("/{fileId}/inactive")
    public AlarmFileResponse inactiveFileAlarm(@PathVariable Long fileId) {
        return alarmService.changeAlarmInactive(fileId);
    }
}
