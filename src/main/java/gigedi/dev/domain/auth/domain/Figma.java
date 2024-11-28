package gigedi.dev.domain.auth.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import gigedi.dev.domain.config.BaseTimeEntity;
import gigedi.dev.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Figma extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long figmaId;

    @Column(nullable = false)
    private String figmaName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String figmaProfile;

    @Column(nullable = false)
    private String figmaUserId;

    @Column private LocalDateTime deletedAt;

    @Column(nullable = false)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memeber_id")
    private Member member;

    public void deleteFigmaAccount() {
        this.deletedAt = LocalDateTime.now();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Figma(
            String figmaName,
            String email,
            String figmaProfile,
            String figmaUserId,
            String refreshToken,
            Member member) {
        this.figmaName = figmaName;
        this.email = email;
        this.figmaProfile = figmaProfile;
        this.figmaUserId = figmaUserId;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public static Figma createFigma(
            String figmaName,
            String email,
            String figmaProfile,
            String figmaUserId,
            Member member,
            String refreshToken) {
        return Figma.builder()
                .figmaName(figmaName)
                .email(email)
                .figmaProfile(figmaProfile)
                .figmaUserId(figmaUserId)
                .member(member)
                .refreshToken(refreshToken)
                .build();
    }
}
