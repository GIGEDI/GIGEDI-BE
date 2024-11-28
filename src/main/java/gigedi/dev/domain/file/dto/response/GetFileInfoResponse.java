package gigedi.dev.domain.file.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetFileInfoResponse(@JsonProperty("name") String fileName) {}
