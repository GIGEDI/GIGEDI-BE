package gigedi.dev.domain.member.dto.response;

import gigedi.dev.domain.member.domain.Member;

public record MemberInfoResponse(Long userId, String username, String profileImg) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getOauthInfo().getUsername(),
                member.getOauthInfo().getProfileImg());
    }
}
