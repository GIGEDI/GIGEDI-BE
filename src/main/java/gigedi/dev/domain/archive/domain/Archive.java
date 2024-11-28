package gigedi.dev.domain.archive.domain;

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
import gigedi.dev.domain.config.BaseTimeEntity;
import gigedi.dev.domain.file.domain.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Archive extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long archiveId;

    @Column(length = 30)
    private String title;

    @Column(nullable = false)
    private Integer blockCount;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figma_id", nullable = false)
    private Figma figma;

    public void increaseBlockCount() {
        this.blockCount++;
    }

    public void decreaseBlockCount() {
        this.blockCount--;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Archive(String title, Integer blockCount, File file, Figma figma) {
        this.title = title;
        this.blockCount = blockCount;
        this.file = file;
        this.figma = figma;
    }

    public static Archive createArchive(String title, File file, Figma figma) {
        return Archive.builder().title(title).blockCount(0).file(file).figma(figma).build();
    }

    public void updateArchive(String newTitle) {
        this.title = newTitle;
    }

    public void deleteArchive() {
        this.deletedAt = LocalDateTime.now();
    }
}
