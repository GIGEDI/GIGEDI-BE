package gigedi.dev.domain.discord.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.discord.dao.DiscordRepository;
import gigedi.dev.domain.discord.domain.Discord;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscordService {
    private final MemberUtil memberUtil;
    private final DiscordRepository discordRepository;

    @Transactional
    public Discord saveDiscord(Discord discord) {
        return discordRepository.save(discord);
    }

    @Transactional(readOnly = true)
    public Discord findConnectedDiscord() {
        Member currentMember = memberUtil.getCurrentMember();
        return discordRepository
                .findByMember(currentMember)
                .orElseThrow(() -> new CustomException(ErrorCode.DISCORD_ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public void deleteDiscord(Discord discord) {
        discordRepository.delete(discord);
    }

    @Transactional(readOnly = true)
    public void validateDiscordExistsForMember() {
        Member currentMember = memberUtil.getCurrentMember();
        if (discordRepository.findByMember(currentMember).isPresent()) {
            throw new CustomException(ErrorCode.DISCORD_ACCOUNT_ALREADY_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    public String getDmChannelByMember(Member member) {
        return discordRepository.findByMember(member).map(Discord::getDmChannel).orElse(null);
    }
}
