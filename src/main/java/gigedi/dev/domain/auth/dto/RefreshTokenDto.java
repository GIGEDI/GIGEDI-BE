package gigedi.dev.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenDto {
    private Long memberId;
    private String token;
    private Long ttl;
}
