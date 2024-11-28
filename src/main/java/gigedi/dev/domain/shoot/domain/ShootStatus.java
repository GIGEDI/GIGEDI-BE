package gigedi.dev.domain.shoot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ShootStatus extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shootStatusId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figma_id")
    private Figma figma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoot_id")
    private Shoot shoot;

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private ShootStatus(Status status, Figma figma, Shoot shoot) {
        this.status = status;
        this.figma = figma;
        this.shoot = shoot;
    }

    public static ShootStatus createShootStatus(Status status, Figma figma, Shoot shoot) {
        return ShootStatus.builder().status(status).figma(figma).shoot(shoot).build();
    }
}
