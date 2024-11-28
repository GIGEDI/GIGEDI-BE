package gigedi.dev.domain.discord.application;

import java.io.IOException;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImageConverter {
    private final String base64Prefix = "data:image/png;base64,";
    private final String imagePath = "img/shoot.png";

    private String getBase64EncodedImage(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            byte[] imageBytes = resource.getInputStream().readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.info("이미지 로드 실패 {}", e.getMessage());
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
    }

    public String getShootImageBase64Encoded() {
        return base64Prefix + getBase64EncodedImage(imagePath);
    }
}
