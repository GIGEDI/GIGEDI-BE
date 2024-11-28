package gigedi.dev.infra.config.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.discord.client")
public record DiscordProperties(String id, String secret, String redirectUri, String botToken) {}
