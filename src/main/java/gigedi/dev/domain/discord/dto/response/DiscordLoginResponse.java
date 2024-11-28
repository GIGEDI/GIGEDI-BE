package gigedi.dev.domain.discord.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DiscordLoginResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("guild") Guild guild) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Guild(@JsonProperty("id") String id) {}

    public String getGuildId() {
        return guild != null ? guild.id() : null;
    }
}
