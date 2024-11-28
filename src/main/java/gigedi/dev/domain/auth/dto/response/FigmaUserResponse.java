package gigedi.dev.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FigmaUserResponse(String id, String email, String handle, String img_url) {}
