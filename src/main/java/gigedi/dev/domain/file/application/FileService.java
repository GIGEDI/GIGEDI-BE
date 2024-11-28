package gigedi.dev.domain.file.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.application.FigmaApiService;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.dao.FileRepository;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.domain.file.dto.response.GetFileInfoResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
    private final FigmaApiService figmaApiService;
    private final FileRepository fileRepository;

    public File getFileByFileId(String fileKey, Figma figma) {
        return fileRepository
                .findByFileKey(fileKey)
                .orElseGet(() -> fileRepository.save(createFile(fileKey, figma)));
    }

    private File createFile(String fileKey, Figma figma) {
        String accessToken =
                figmaApiService.reissueAccessToken(figma.getRefreshToken()).accessToken();
        GetFileInfoResponse fileInfoResponse = figmaApiService.getFileInfo(fileKey, accessToken);
        return File.createFile(fileKey, fileInfoResponse.fileName());
    }
}
