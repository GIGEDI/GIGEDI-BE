package gigedi.dev.domain.shoot.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.block.domain.Block;
import gigedi.dev.domain.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shoot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shootId;

    @Column(nullable = false, length = 1000)
    private String content;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figma_id")
    private Figma figma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    public void deleteShoot() {
        this.deletedAt = LocalDateTime.now();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Shoot(String content, Figma figma, Block block) {
        this.content = content;
        this.figma = figma;
        this.block = block;
    }

    public static Shoot createShoot(String content, Figma figma, Block block) {
        return Shoot.builder().content(content).figma(figma).block(block).build();
    }
}
