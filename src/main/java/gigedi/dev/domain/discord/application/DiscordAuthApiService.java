package gigedi.dev.domain.discord.application;

import static gigedi.dev.global.common.constants.SecurityConstants.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import gigedi.dev.domain.discord.dto.response.DiscordLoginResponse;
import gigedi.dev.domain.discord.dto.response.DiscordUserResponse;
import gigedi.dev.domain.discord.dto.response.ReissueDiscordTokenResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordAuthApiService {
    private final RestClient restClient;
    private final DiscordProperties discordProperties;

    public DiscordLoginResponse discordLogin(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(CODE_KEY, code);
            formData.add(CLIENT_ID_KEY, discordProperties.id());
            formData.add(CLIENT_SECRET_KEY, discordProperties.secret());
            formData.add(REDIRECT_URI_KEY, discordProperties.redirectUri());
            formData.add(GRANT_TYPE_KEY, LOGIN_GRANT_TYPE_VALUE);

            return restClient
                    .post()
                    .uri(DISCORD_TOKEN_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Dicord 로그인 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.DISCORD_LOGIN_FAILED);
                            })
                    .body(DiscordLoginResponse.class);
        } catch (Exception e) {
            log.error("Discord 로그인 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_LOGIN_FAILED);
        }
    }

    public DiscordUserResponse getDiscordUserInfo(String accessToken) {
        try {
            return restClient
                    .get()
                    .uri(DISCORD_USER_INFO_URL)
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord 사용자 정보 조회 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.DISCORD_USER_INFO_FAILED);
                            })
                    .body(DiscordUserResponse.class);
        } catch (Exception e) {
            log.error("Discord 사용자 정보 조회 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_USER_INFO_FAILED);
        }
    }

    public ReissueDiscordTokenResponse reissueDiscordToken(String refreshToken) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(CLIENT_ID_KEY, discordProperties.id());
            formData.add(CLIENT_SECRET_KEY, discordProperties.secret());
            formData.add(GRANT_TYPE_KEY, REISSUE_GRANT_TYPE_VALUE);
            formData.add(REFRESH_TOKEN, refreshToken);

            return restClient
                    .post()
                    .uri(DISCORD_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord 토큰 갱신 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.DISCORD_TOKEN_REISSUE_FAILED);
                            })
                    .body(ReissueDiscordTokenResponse.class);
        } catch (Exception e) {
            log.error("Discord 토큰 갱신 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_TOKEN_REISSUE_FAILED);
        }
    }

    public void disconnectDiscordAccount(String accessToken) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(CLIENT_ID_KEY, discordProperties.id());
            formData.add(CLIENT_SECRET_KEY, discordProperties.secret());
            formData.add("token", accessToken);

            restClient
                    .post()
                    .uri(DISCORD_DISCONNECT_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord 연결 해제 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.DISCORD_DISCONNECT_FAILED);
                            })
                    .toBodilessEntity();

            log.info("Discord 연결 해제 성공");
        } catch (Exception e) {
            log.error("Discord 연결 해제 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_DISCONNECT_FAILED);
        }
    }
}
