package gigedi.dev.domain.figma.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.dao.FigmaRepository;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FigmaService {
    private final FigmaRepository figmaRepository;

    @Transactional(readOnly = true)
    public void validateFigmaAccountAlreadyExists(String figmaId) {
        if (figmaRepository.findByFigmaUserIdAndDeletedAtIsNull(figmaId).isPresent()) {
            throw new CustomException(ErrorCode.FIGMA_ACCOUNT_ALREADY_CONNECTED);
        }
    }

    @Transactional(readOnly = true)
    public Figma getFigmaByFigmaId(String figmaId) {
        return figmaRepository
                .findByFigmaUserIdAndDeletedAtIsNull(figmaId)
                .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_CONNECTED));
    }

    public Figma findByTag(String tag, File file) {
        return figmaRepository
                .findByFigmaNameAndFile(tag, file)
                .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_USER_INFO_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Figma> getFigmaListByMember(Member member) {
        return figmaRepository.findByMemberAndDeletedAtIsNull(member);
    }
}
