package gigedi.dev.domain.block.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.archive.application.ArchiveService;
import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.block.dao.BlockRepository;
import gigedi.dev.domain.block.domain.Block;
import gigedi.dev.domain.block.dto.request.CreateBlockRequest;
import gigedi.dev.domain.block.dto.request.UpdateBlockRequest;
import gigedi.dev.domain.block.dto.response.CreateBlockResponse;
import gigedi.dev.domain.block.dto.response.GetBlockResponse;
import gigedi.dev.global.common.constants.FigmaConstants;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.FigmaUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final ArchiveService archiveService;
    private final FigmaUtil figmaUtil;
    private final BlockTitleService blockTitleService;

    public CreateBlockResponse createBlock(Long archiveId, CreateBlockRequest request) {
        final Figma figma = figmaUtil.getCurrentFigma();
        Archive archive = archiveService.getArchiveById(archiveId);

        int blockCount = archive.getBlockCount();
        if (blockCount >= FigmaConstants.MaxCount) {
            throw new CustomException(ErrorCode.BLOCK_EXCEED_LIMIT);
        }

        String title = request.title();
        if (title == null || title.trim().isEmpty()) {
            title = FigmaConstants.NEW_BLOCK_NAME;
        }

        title = blockTitleService.generateUniqueTitle(title);
        archive.increaseBlockCount();

        Block block =
                blockRepository.save(
                        Block.createBlock(
                                title,
                                request.xCoordinate(),
                                request.yCoordinate(),
                                request.height(),
                                request.width(),
                                archive,
                                figma));

        return CreateBlockResponse.from(block);
    }

    public List<GetBlockResponse> getBlock(Long archiveId) {
        return blockRepository.findByArchive_ArchiveIdAndDeletedAtIsNull(archiveId).stream()
                .map(GetBlockResponse::from)
                .collect(Collectors.toList());
    }

    public CreateBlockResponse updateBlockTitle(Long blockId, UpdateBlockRequest request) {
        Block block = getBlockById(blockId);
        block.setTitle(request.title());
        return CreateBlockResponse.from(block);
    }

    public void deleteBlock(Long blockId) {
        Block block = getBlockById(blockId);
        Archive archive = block.getArchive();
        archive.decreaseBlockCount();
        block.deleteBlock();
    }

    public Block getBlockById(Long blockId) {
        return blockRepository
                .findByBlockIdAndDeletedAtIsNull(blockId)
                .orElseThrow(() -> new CustomException(ErrorCode.BLOCK_NOT_FOUND));
    }
}
