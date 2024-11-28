package gigedi.dev.domain.discord.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReissueDiscordTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken) {}
