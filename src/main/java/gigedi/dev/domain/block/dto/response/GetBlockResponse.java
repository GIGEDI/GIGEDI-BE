package gigedi.dev.domain.block.dto.response;

import gigedi.dev.domain.block.domain.Block;

public record GetBlockResponse(
        Long blockId,
        String title,
        int shootCount,
        double xCoordinate,
        double yCoordinate,
        double height,
        double width) {
    public static GetBlockResponse from(Block block) {
        return new GetBlockResponse(
                block.getBlockId(),
                block.getTitle(),
                block.getShootCount(),
                block.getXCoordinate(),
                block.getYCoordinate(),
                block.getHeight(),
                block.getWidth());
    }
}
