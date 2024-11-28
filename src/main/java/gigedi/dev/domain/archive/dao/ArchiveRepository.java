package gigedi.dev.domain.archive.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.domain.file.domain.File;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByArchiveIdAndDeletedAtIsNull(Long archiveId);

    @Query(
            "SELECT a.title FROM Archive a WHERE a.deletedAt IS NULL AND (a.title = :baseTitle OR a.title LIKE CONCAT(:baseTitle, '%'))")
    List<String> findTitlesByBaseExact(String baseTitle);

    List<Archive> findByFileAndDeletedAtIsNull(File file);
}
