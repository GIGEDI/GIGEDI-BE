package gigedi.dev.domain.shoot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShootTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shootTagId;

    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoot_id")
    private Shoot shoot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figma_id")
    private Figma figma;

    @Builder(access = AccessLevel.PRIVATE)
    private ShootTag(boolean isRead, Shoot shoot, Figma figma) {
        this.isRead = isRead;
        this.shoot = shoot;
        this.figma = figma;
    }

    public static ShootTag createShootTag(Shoot shoot, Figma figma) {
        return ShootTag.builder().isRead(false).shoot(shoot).figma(figma).build();
    }
}
