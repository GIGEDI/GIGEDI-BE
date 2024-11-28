package gigedi.dev.domain.shoot.dto.response;

import java.util.List;

import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.global.util.TimeUtil;

public record GetShootResponse(
        Long shootId,
        String authorName,
        String authorProfileImg,
        String content,
        String timeAgo,
        List<User> yet,
        List<User> doing,
        List<User> done,
        String createdAt) {
    public record User(Long userId, String username, String profileImg) {}

    public static GetShootResponse of(
            Shoot shoot, List<User> yetUsers, List<User> doingUsers, List<User> doneUsers) {
        return new GetShootResponse(
                shoot.getShootId(),
                shoot.getFigma().getFigmaName(),
                shoot.getFigma().getFigmaProfile(),
                shoot.getContent(),
                TimeUtil.calculateTimeAgo(shoot.getCreatedAt()),
                yetUsers,
                doingUsers,
                doneUsers,
                shoot.getCreatedAt().toString());
    }
}
