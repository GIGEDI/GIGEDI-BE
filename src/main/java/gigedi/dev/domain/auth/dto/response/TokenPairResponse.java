package gigedi.dev.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPairResponse {
    private final String accessToken;
    private final String refreshToken;
}
