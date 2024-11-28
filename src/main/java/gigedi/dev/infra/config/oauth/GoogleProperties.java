package gigedi.dev.infra.config.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth.google.client")
public class GoogleProperties {
    private String id;
    private String secret;
    private String redirectUri;
    private String grantType;
}
