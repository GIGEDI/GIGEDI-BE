package gigedi.dev.domain.discord.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateDMChannelResponse(String id) {}
