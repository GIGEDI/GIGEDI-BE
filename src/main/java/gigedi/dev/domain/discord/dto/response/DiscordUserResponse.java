package gigedi.dev.domain.discord.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DiscordUserResponse(String id, String email) {}
