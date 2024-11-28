package gigedi.dev.domain.block.application;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.block.dao.BlockRepository;
import gigedi.dev.global.common.constants.FigmaConstants;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockTitleService {
    private final BlockRepository blockRepository;

    public String generateUniqueTitle(String newTitle) {
        String baseTitle = removeNumberSuffix(newTitle);
        List<String> existingTitles = blockRepository.findTitlesByBase(baseTitle);
        int count = 1;
        String uniqueTitle = baseTitle;
        while (existingTitles.contains(uniqueTitle)) {
            uniqueTitle = baseTitle + "(" + count + ")";
            count++;
        }

        return uniqueTitle;
    }

    private String removeNumberSuffix(String title) {
        Pattern pattern = Pattern.compile(FigmaConstants.TITLE_REGEX);
        Matcher matcher = pattern.matcher(title);
        if (matcher.matches()) {
            return matcher.group(1).trim();
        }
        return title;
    }
}
