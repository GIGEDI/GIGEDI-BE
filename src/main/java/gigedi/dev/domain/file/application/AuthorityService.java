package gigedi.dev.domain.file.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.dao.AuthorityRepository;
import gigedi.dev.domain.file.domain.Authority;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Transactional(readOnly = true)
    public List<Authority> getRelatedAuthorityList(Long memberId) {
        return authorityRepository.findRelatedAuthorities(memberId);
    }

    @Transactional(readOnly = true)
    public Authority getAuthorityByFileIdAndFigmaList(Long fileId, List<Figma> figmaList) {
        return authorityRepository
                .findByFileAndActiveFigma(fileId, figmaList)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTHORITY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Figma> getAlarmTargetListByFigmaName(File file, List<String> figmaNameList) {
        return authorityRepository.getFigmaNamesWithActiveAlarmByFile(file, figmaNameList);
    }
}
