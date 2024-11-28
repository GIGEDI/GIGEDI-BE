package gigedi.dev.domain.file.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FigmaTokenReissueResponse(@JsonProperty("access_token") String accessToken) {}
