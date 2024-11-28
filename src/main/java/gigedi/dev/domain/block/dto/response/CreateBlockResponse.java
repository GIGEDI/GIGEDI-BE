package gigedi.dev.domain.block.dto.response;

import gigedi.dev.domain.block.domain.Block;

public record CreateBlockResponse(
        Long blockId,
        String title,
        double xCoordinate,
        double yCoordinate,
        double height,
        double width) {
    public static CreateBlockResponse from(Block block) {
        return new CreateBlockResponse(
                block.getBlockId(),
                block.getTitle(),
                block.getXCoordinate(),
                block.getYCoordinate(),
                block.getHeight(),
                block.getWidth());
    }
}
