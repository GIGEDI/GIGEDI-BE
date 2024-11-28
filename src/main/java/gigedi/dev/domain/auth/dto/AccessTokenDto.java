package gigedi.dev.domain.auth.dto;

import gigedi.dev.domain.member.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenDto {
    private Long memberId;
    private MemberRole role;
    private String token;
}
