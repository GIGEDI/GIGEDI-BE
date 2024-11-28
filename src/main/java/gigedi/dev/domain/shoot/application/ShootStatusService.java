package gigedi.dev.domain.shoot.application;

import java.util.EnumSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.shoot.dao.ShootStatusRepository;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.domain.shoot.domain.Status;
import gigedi.dev.domain.shoot.dto.response.GetShootResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.FigmaUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShootStatusService {
    private final ShootStatusRepository shootStatusRepository;
    private final FigmaUtil figmaUtil;
    private final ShootService shootService;

    @Transactional
    public GetShootResponse updateShootStatus(Long shootId, Status newStatus) {
        final Figma figma = figmaUtil.getCurrentFigma();
        validateStatus(newStatus);
        Shoot shoot = shootService.findValidShoot(shootId);
        ShootStatus shootStatus =
                shootStatusRepository
                        .findByShoot_ShootIdAndFigma_FigmaId(shootId, figma.getFigmaId())
                        .orElseGet(
                                () -> {
                                    ShootStatus newShootStatus =
                                            ShootStatus.createShootStatus(newStatus, figma, shoot);
                                    return shootStatusRepository.save(newShootStatus);
                                });

        if (shootStatus.getStatus() != newStatus) {
            shootStatus.updateStatus(newStatus);
        }
        return GetShootResponse.of(
                shoot,
                shootService.getUsersByStatus(shoot, Status.YET),
                shootService.getUsersByStatus(shoot, Status.DOING),
                shootService.getUsersByStatus(shoot, Status.DONE));
    }

    private void validateStatus(Status status) {
        if (status == null || !EnumSet.allOf(Status.class).contains(status)) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }
    }
}
