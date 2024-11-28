package gigedi.dev.domain.auth.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.domain.member.domain.Member;

@Repository
public interface FigmaRepository extends JpaRepository<Figma, Long> {
    Optional<Figma> findByMemberId(Long memberId);

    List<Figma> findByMemberAndDeletedAtIsNull(Member member);

    Optional<Figma> findByFigmaIdAndDeletedAtIsNull(Long figmaId);

    Optional<Figma> findByFigmaUserIdAndDeletedAtIsNull(String figmaUserId);

    @Query(
            "SELECT f FROM Figma f WHERE f.figmaName = :figmaName AND f.figmaId IN (SELECT a.figma.figmaId FROM Authority a WHERE a.file = :file)")
    Optional<Figma> findByFigmaNameAndFile(
            @Param("figmaName") String figmaName, @Param("file") File file);
}
