package gigedi.dev.domain.archive.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.archive.dao.ArchiveRepository;
import gigedi.dev.global.common.constants.FigmaConstants;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArchiveTitleService {
    private final ArchiveRepository archiveRepository;
    private static final int MAX_TITLE_LENGTH = 30;

    @Transactional(readOnly = true)
    public String generateUniqueTitle(String newTitle) {
        validateTitleLength(newTitle);
        List<String> existingTitles = archiveRepository.findTitlesByBaseExact(newTitle);
        if (!existingTitles.contains(newTitle)) {
            return newTitle;
        }
        return findUniqueTitle(newTitle, existingTitles);
    }

    private void validateTitleLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new CustomException(ErrorCode.ARCHIVE_TITLE_EXCEED_LIMIT);
        }
    }

    private String findUniqueTitle(String newTitle, List<String> existingTitles) {
        int maxNumber = findMaxNumber(newTitle, existingTitles);
        String uniqueTitle = newTitle;
        int suffixLength = String.valueOf(maxNumber + 1).length();

        while (uniqueTitle.length() + suffixLength > MAX_TITLE_LENGTH) {
            uniqueTitle = uniqueTitle.substring(0, uniqueTitle.length() - 1);
        }

        return uniqueTitle + (maxNumber + 1);
    }

    private int findMaxNumber(String newTitle, List<String> existingTitles) {
        int maxNumber = 0;
        for (String title : existingTitles) {
            if (!title.equals(newTitle)) {
                int number = extractNumber(title, newTitle);
                if (number > maxNumber) {
                    maxNumber = number;
                }
            }
        }
        return maxNumber;
    }

    private int extractNumber(String title, String newTitle) {
        if (title.length() <= newTitle.length()) {
            return 0;
        }
        String suffix = title.substring(newTitle.length());
        if (suffix.matches(FigmaConstants.ARCHIVE_TITLE_REGEX)) {
            return Integer.parseInt(suffix);
        }
        return 0;
    }
}
