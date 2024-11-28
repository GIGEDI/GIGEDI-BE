package gigedi.dev.domain.discord.application;

import static gigedi.dev.global.common.constants.SecurityConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import gigedi.dev.domain.discord.dto.response.CreateDMChannelResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordDmApiService {
    private final RestClient restClient;
    private final DiscordProperties discordProperties;

    private static final String DM_BASE_TITLE = "Please check it in OUR SHOOT !";
    private static final int DM_BASE_COLOR = 3447003;

    public CreateDMChannelResponse createDMChannel(String userId) {
        try {
            Map<String, Object> requestBody = Map.of("recipient_id", userId);

            return restClient
                    .post()
                    .uri(DISCORD_CREATE_DM_CHANNEL_URL)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            BOT_TOKEN_PREFIX + discordProperties.botToken())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord DM 채널 생성 실패: {}", response.getStatusCode());
                                throw new CustomException(
                                        ErrorCode.DISCORD_DM_CHANNEL_CREATION_FAILED);
                            })
                    .body(CreateDMChannelResponse.class);
        } catch (Exception e) {
            log.error("Discord DM 채널 생성 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_DM_CHANNEL_CREATION_FAILED);
        }
    }

    public void sendDMMessage(
            String channelId,
            String sender,
            String receiver,
            String archiveTitle,
            String blockTitle,
            String content) {
        try {
            Map<String, Object> embed = new HashMap<>();
            embed.put("title", DM_BASE_TITLE);
            embed.put("color", DM_BASE_COLOR);

            List<Map<String, Object>> fields = new ArrayList<>();
            fields.add(createField("From", sender, true));
            fields.add(createField("To", receiver, true));
            fields.add(
                    createField("In", "ARCHIVE " + archiveTitle + " - BLOCK " + blockTitle, false));
            fields.add(createField("Content", content, false));

            embed.put("fields", fields);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("embeds", List.of(embed));

            restClient
                    .post()
                    .uri(
                            uriBuilder ->
                                    uriBuilder
                                            .scheme(HTTPS_SCHEME)
                                            .host(DISCORD_HOST)
                                            .path(DISCORD_SEND_DM_URL)
                                            .build(channelId))
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            BOT_TOKEN_PREFIX + discordProperties.botToken())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord DM 메시지 전송 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.DISCORD_DM_MESSAGE_SEND_FAILED);
                            })
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Discord DM 메시지 전송 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_DM_MESSAGE_SEND_FAILED);
        }
    }

    private Map<String, Object> createField(String name, String value, boolean inline) {
        Map<String, Object> field = new HashMap<>();
        field.put("name", name);
        field.put("value", value);
        field.put("inline", inline);
        return field;
    }
}
