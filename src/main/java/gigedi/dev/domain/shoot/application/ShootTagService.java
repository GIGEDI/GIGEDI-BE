package gigedi.dev.domain.shoot.application;

import java.util.List;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.figma.application.FigmaService;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.domain.shoot.dao.ShootTagRepository;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.ShootTag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShootTagService {
    private final ShootTagRepository shootTagRepository;
    private final FigmaService figmaService;

    public void createShootTags(Shoot shoot, List<String> tags, File currentFile) {
        tags.forEach(
                tag -> {
                    Figma figma = figmaService.findByTag(tag, currentFile);
                    if (figma != null) {
                        ShootTag shootTag = ShootTag.createShootTag(shoot, figma);
                        shootTagRepository.save(shootTag);
                    }
                });
    }
}
