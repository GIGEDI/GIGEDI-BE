package gigedi.dev.global.util;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class TimeUtil {
    private static final String MINUTES_AGO = " minutes ago";
    private static final String HOURS_AGO = " hours ago";
    private static final String DAYS_AGO = " days ago";

    private static final long MINUTES_IN_AN_HOUR = 60;
    private static final long HOURS_IN_A_DAY = 24;

    public static String calculateTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < MINUTES_IN_AN_HOUR) {
            return minutes + MINUTES_AGO;
        } else if (hours < HOURS_IN_A_DAY) {
            return hours + HOURS_AGO;
        } else {
            return days + DAYS_AGO;
        }
    }
}
