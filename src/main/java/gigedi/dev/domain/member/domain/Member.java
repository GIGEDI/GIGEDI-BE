package gigedi.dev.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import gigedi.dev.domain.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded private OauthInfo oauthInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(OauthInfo oauthInfo, LocalDateTime lastLoginAt, MemberRole role) {
        this.oauthInfo = oauthInfo;
        this.lastLoginAt = lastLoginAt;
        this.role = role;
    }

    public static Member createMember(OauthInfo oauthInfo) {
        return Member.builder()
                .oauthInfo(oauthInfo)
                .lastLoginAt(LocalDateTime.now())
                .role(MemberRole.USER)
                .build();
    }

    public void memberWithdrawal() {
        this.deletedAt = LocalDateTime.now();
    }
}
