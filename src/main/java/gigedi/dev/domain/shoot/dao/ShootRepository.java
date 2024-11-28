package gigedi.dev.domain.shoot.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.shoot.domain.Shoot;

@Repository
public interface ShootRepository extends JpaRepository<Shoot, Long>, ShootRepositoryCustom {
    List<Shoot> findAllByBlock_BlockIdAndDeletedAtIsNull(Long blockId);

    Optional<Shoot> findByShootIdAndDeletedAtIsNull(Long shootId);
}
