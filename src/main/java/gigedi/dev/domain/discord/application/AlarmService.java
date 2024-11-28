package gigedi.dev.domain.discord.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.block.domain.Block;
import gigedi.dev.domain.discord.domain.Discord;
import gigedi.dev.domain.discord.dto.response.AlarmFileResponse;
import gigedi.dev.domain.discord.dto.response.GetAlarmFileListResponse;
import gigedi.dev.domain.figma.application.FigmaService;
import gigedi.dev.domain.file.application.AuthorityService;
import gigedi.dev.domain.file.domain.Authority;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.util.MemberUtil;
import gigedi.dev.global.util.ShootUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final DiscordService discordService;
    private final AuthorityService authorityService;
    private final FigmaService figmaService;
    private final DiscordDmApiService discordDmApiService;
    private final MemberUtil memberUtil;

    public GetAlarmFileListResponse getAlarmFileList() {
        Member currentMember = memberUtil.getCurrentMember();
        Discord connectedDiscord = discordService.findConnectedDiscord();
        List<AlarmFileResponse> alarmList =
                authorityService.getRelatedAuthorityList(currentMember.getId()).stream()
                        .map(AlarmFileResponse::from)
                        .collect(Collectors.toList());
        return new GetAlarmFileListResponse(connectedDiscord.getEmail(), alarmList);
    }

    @Transactional
    public AlarmFileResponse changeAlarmActive(Long fileId) {
        Authority authority = getAuthorityByFileId(fileId);
        authority.updateAlarmActive();
        return AlarmFileResponse.from(authority);
    }

    @Transactional
    public AlarmFileResponse changeAlarmInactive(Long fileId) {
        Authority authority = getAuthorityByFileId(fileId);
        authority.updateAlarmInactive();
        return AlarmFileResponse.from(authority);
    }

    private Authority getAuthorityByFileId(Long fileId) {
        Member currentMember = memberUtil.getCurrentMember();
        List<Figma> figmaList = figmaService.getFigmaListByMember(currentMember);
        return authorityService.getAuthorityByFileIdAndFigmaList(fileId, figmaList);
    }

    public void sendAlarmToDiscord(
            List<String> tags, Block block, Figma senderFigma, String message) {
        String sender = senderFigma.getFigmaName();
        String blockTitle = block.getTitle();
        String archiveTitle = block.getArchive().getTitle();
        File currentFile = block.getArchive().getFile();
        List<Figma> receiverList =
                authorityService.getAlarmTargetListByFigmaName(currentFile, tags);

        receiverList.forEach(
                receiver -> {
                    String channelId = discordService.getDmChannelByMember(receiver.getMember());
                    if (channelId != null) {
                        discordDmApiService.sendDMMessage(
                                channelId,
                                sender,
                                receiver.getFigmaName(),
                                ShootUtil.highlightText(archiveTitle),
                                ShootUtil.highlightText(blockTitle),
                                ShootUtil.highlightMentions(message));
                    }
                });
    }
}
