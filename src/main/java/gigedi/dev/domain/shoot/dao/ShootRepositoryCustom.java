package gigedi.dev.domain.shoot.dao;

import java.util.List;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.Status;

public interface ShootRepositoryCustom {
    List<Shoot> findByFigmaAndStatusAndDeletedAtIsNull(Figma figma, Status status);

    List<Shoot> findMentionedShootsByFigma(Figma figma);
}
