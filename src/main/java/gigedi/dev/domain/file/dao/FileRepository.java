package gigedi.dev.domain.file.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.file.domain.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileKey(String fileId);
}
