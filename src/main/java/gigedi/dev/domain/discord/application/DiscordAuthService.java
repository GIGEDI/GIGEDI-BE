package gigedi.dev.domain.discord.application;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.discord.domain.Discord;
import gigedi.dev.domain.discord.dto.response.*;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscordAuthService {
    private final MemberUtil memberUtil;
    private final ImageConverter converter;
    private final DiscordService discordService;
    private final DiscordAuthApiService discordAuthApiService;
    private final DiscordGuildApiService discordGuildApiService;
    private final DiscordDmApiService discordDmApiService;

    public DiscordInfoResponse discordConnect(String code) {
        discordService.validateDiscordExistsForMember();
        DiscordLoginResponse loginResponse =
                discordAuthApiService.discordLogin(URLDecoder.decode(code, StandardCharsets.UTF_8));
        DiscordUserResponse userInfo =
                discordAuthApiService.getDiscordUserInfo(loginResponse.accessToken());

        discordGuildApiService.updateGuildInfo(
                loginResponse.getGuildId(), converter.getShootImageBase64Encoded());
        CreateDMChannelResponse dmChannel = discordDmApiService.createDMChannel(userInfo.id());

        return saveDiscord(loginResponse, userInfo, dmChannel);
    }

    private DiscordInfoResponse saveDiscord(
            DiscordLoginResponse loginResponse,
            DiscordUserResponse userInfo,
            CreateDMChannelResponse dmChannel) {
        Member currentMember = memberUtil.getCurrentMember();
        Discord discord =
                Discord.createDiscord(
                        currentMember,
                        userInfo.email(),
                        loginResponse.refreshToken(),
                        userInfo.id(),
                        dmChannel.id(),
                        loginResponse.getGuildId());
        return DiscordInfoResponse.from(discordService.saveDiscord(discord));
    }

    public void discordDisconnect() {
        Discord discord = discordService.findConnectedDiscord();
        ReissueDiscordTokenResponse tokenResponse =
                discordAuthApiService.reissueDiscordToken(discord.getRefreshToken());
        discord.updateRefreshToken(tokenResponse.refreshToken());

        discordAuthApiService.disconnectDiscordAccount(tokenResponse.accessToken());
        discordService.deleteDiscord(discord);
    }
}
