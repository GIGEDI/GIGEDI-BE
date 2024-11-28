package gigedi.dev.domain.shoot.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.domain.shoot.domain.Status;

@Repository
public interface ShootStatusRepository extends JpaRepository<ShootStatus, Long> {
    List<ShootStatus> findByShoot_ShootIdAndStatus(Long shootId, Status status);

    Optional<ShootStatus> findByShoot_ShootIdAndFigma_FigmaId(Long shootId, Long figmaId);
}
