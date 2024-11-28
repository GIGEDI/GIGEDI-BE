package gigedi.dev.domain.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id private Long memberId;
    private String token;

    @TimeToLive private long ttl;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(Long memberId, String token, long ttl) {
        this.memberId = memberId;
        this.token = token;
        this.ttl = ttl;
    }

    public static RefreshToken of(Long memberId, String token, long ttl) {
        return RefreshToken.builder().memberId(memberId).token(token).ttl(ttl).build();
    }

    public void updateRefreshToken(String newToken, long newTtl) {
        this.token = newToken;
        this.ttl = newTtl;
    }
}
