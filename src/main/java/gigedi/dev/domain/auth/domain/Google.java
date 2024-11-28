package gigedi.dev.domain.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Google {
    @Id private Long memberId;
    private String token;

    @Builder(access = AccessLevel.PRIVATE)
    private Google(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public static Google of(Long memberId, String token) {
        return Google.builder().memberId(memberId).token(token).build();
    }
}
