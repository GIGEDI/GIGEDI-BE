package gigedi.dev.domain.block.dto.request;

public record CreateBlockRequest(
        String title, double xCoordinate, double yCoordinate, double height, double width) {}
