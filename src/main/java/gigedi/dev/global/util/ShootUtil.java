package gigedi.dev.global.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShootUtil {
    private static final String SPACE_DELIMITER = "\\s+";
    private static final String TAG_PREFIX = "@";
    private static final String HIGHLIGHT_FORMAT = "**%s**";

    public static List<String> extractTags(String content) {

        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(content.split(SPACE_DELIMITER))
                .filter(word -> word.startsWith(TAG_PREFIX))
                .map(word -> word.substring(1))
                .collect(Collectors.toList());
    }

    public static String highlightMentions(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        return Arrays.stream(content.split(SPACE_DELIMITER))
                .map(
                        word ->
                                word.startsWith(TAG_PREFIX)
                                        ? String.format(HIGHLIGHT_FORMAT, word)
                                        : word)
                .collect(Collectors.joining(" "));
    }

    public static String highlightText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return String.format(HIGHLIGHT_FORMAT, text);
    }
}
