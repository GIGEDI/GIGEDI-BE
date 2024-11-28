package gigedi.dev.domain.discord.application;

import static gigedi.dev.global.common.constants.SecurityConstants.BOT_TOKEN_PREFIX;
import static gigedi.dev.global.common.constants.SecurityConstants.DISCORD_GUILD_URL;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordGuildApiService {
    private final RestClient restClient;
    private final DiscordProperties discordProperties;

    public void updateGuildInfo(String guildId, String base64Icon) {
        try {
            String url = DISCORD_GUILD_URL + "/" + guildId;
            String guildName = "SHOOT";

            Map<String, String> requestBody =
                    Map.of(
                            "name", guildName,
                            "icon", base64Icon);

            restClient
                    .patch()
                    .uri(url)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            BOT_TOKEN_PREFIX + discordProperties.botToken())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord 길드 아이콘 업데이트 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.DISCORD_GUILD_UPDATE_FAILED);
                            })
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Discord 길드 아이콘 업데이트 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_GUILD_UPDATE_FAILED);
        }
    }
}
