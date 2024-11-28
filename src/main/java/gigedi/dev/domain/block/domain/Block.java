package gigedi.dev.domain.block.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @Setter
    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private Integer shootCount;

    @Column(nullable = false)
    private double xCoordinate;

    @Column(nullable = false)
    private double yCoordinate;

    @Column(nullable = false)
    private double height;

    @Column(nullable = false)
    private double width;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figma_id", nullable = false)
    private Figma figma;

    public void increaseShootCount() {
        this.shootCount++;
    }

    public void deleteBlock() {
        this.deletedAt = LocalDateTime.now();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Block(
            String title,
            Integer shootCount,
            double xCoordinate,
            double yCoordinate,
            double height,
            double width,
            Archive archive,
            Figma figma) {
        this.title = title;
        this.shootCount = shootCount;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.height = height;
        this.width = width;
        this.archive = archive;
        this.figma = figma;
    }

    public static Block createBlock(
            String title,
            double xCoordinate,
            double yCoordinate,
            double height,
            double width,
            Archive archive,
            Figma figma) {
        return Block.builder()
                .title(title)
                .shootCount(0)
                .xCoordinate(xCoordinate)
                .yCoordinate(yCoordinate)
                .height(height)
                .width(width)
                .archive(archive)
                .figma(figma)
                .build();
    }
}
