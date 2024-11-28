package gigedi.dev.domain.shoot.dao;

import static gigedi.dev.domain.shoot.domain.QShoot.shoot;
import static gigedi.dev.domain.shoot.domain.QShootStatus.shootStatus;
import static gigedi.dev.domain.shoot.domain.QShootTag.shootTag;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.Status;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShootRepositoryImpl implements ShootRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Shoot> findByFigmaAndStatusAndDeletedAtIsNull(Figma figma, Status status) {
        return queryFactory
                .selectFrom(shoot)
                .join(shootStatus)
                .on(shoot.shootId.eq(shootStatus.shoot.shootId))
                .where(
                        shoot.figma.eq(figma),
                        shootStatus.status.eq(status),
                        shoot.deletedAt.isNull())
                .fetch();
    }

    @Override
    public List<Shoot> findMentionedShootsByFigma(Figma figma) {
        return queryFactory
                .select(shootTag.shoot)
                .from(shootTag)
                .where(shootTag.figma.eq(figma), shootTag.shoot.deletedAt.isNull())
                .fetch();
    }
}
