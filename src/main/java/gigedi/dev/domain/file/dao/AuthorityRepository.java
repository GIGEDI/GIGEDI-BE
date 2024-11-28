package gigedi.dev.domain.file.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.domain.Authority;
import gigedi.dev.domain.file.domain.File;

@Repository
public interface AuthorityRepository
        extends JpaRepository<Authority, Long>, AuthorityRepositoryCustom {
    Optional<Authority> findByFigmaAndFile(Figma figma, File file);
}
