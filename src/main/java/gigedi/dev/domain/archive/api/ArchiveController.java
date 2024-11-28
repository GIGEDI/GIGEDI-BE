package gigedi.dev.domain.archive.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import gigedi.dev.domain.archive.application.ArchiveService;
import gigedi.dev.domain.archive.dto.request.CreateArchiveRequest;
import gigedi.dev.domain.archive.dto.request.UpdateArchiveRequest;
import gigedi.dev.domain.archive.dto.response.ArchiveInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Archive", description = "Archive 관련 API")
@RestController
@RequestMapping("/api/v1/archive")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;

    @Operation(summary = "아카이브 생성", description = "아카이브 생성을 진행하는 API")
    @PostMapping
    public ArchiveInfoResponse createArchive(@RequestBody CreateArchiveRequest request) {
        return archiveService.createArchive(request);
    }

    @Operation(summary = "아카이브 조회", description = "파일에 있는 모든 아카이브를 조회하는 API")
    @GetMapping
    public List<ArchiveInfoResponse> getArchiveList() {
        return archiveService.getArchiveList();
    }

    @Operation(summary = "아카이브 제목 수정", description = "아카이브의 제목을 수정하는 API")
    @PatchMapping("/title/{archiveId}")
    public ArchiveInfoResponse updateArchiveTitle(
            @PathVariable Long archiveId, @RequestBody UpdateArchiveRequest request) {
        return archiveService.updateArchiveTitle(archiveId, request);
    }

    @Operation(summary = "아카이브 삭제", description = "아카이브를 삭제하는 API")
    @DeleteMapping("/{archiveId}")
    public void deleteArchive(@PathVariable Long archiveId) {
        archiveService.deleteArchive(archiveId);
    }
}
