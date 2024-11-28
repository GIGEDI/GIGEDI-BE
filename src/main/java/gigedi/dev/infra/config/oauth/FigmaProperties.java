package gigedi.dev.infra.config.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.figma.client")
public record FigmaProperties(String id, String secret, String redirectUri, String grantType) {}
