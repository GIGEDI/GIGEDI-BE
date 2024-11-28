package gigedi.dev.domain.auth.application;

import static gigedi.dev.global.common.constants.SecurityConstants.*;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import gigedi.dev.domain.auth.dto.response.FigmaTokenResponse;
import gigedi.dev.domain.auth.dto.response.FigmaUserResponse;
import gigedi.dev.domain.auth.dto.response.UserInfoResponse;
import gigedi.dev.domain.file.dto.response.FigmaTokenReissueResponse;
import gigedi.dev.domain.file.dto.response.GetFileInfoResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.FigmaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigmaApiService {

    private final FigmaProperties figmaProperties;
    private final RestClient restClient;

    public FigmaTokenResponse getAccessToken(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(CLIENT_ID_KEY, figmaProperties.id());
            formData.add(CLIENT_SECRET_KEY, figmaProperties.secret());
            formData.add(REDIRECT_URI_KEY, figmaProperties.redirectUri());
            formData.add(CODE_KEY, code);
            formData.add(GRANT_TYPE_KEY, LOGIN_GRANT_TYPE_VALUE);

            String responseBody =
                    restClient
                            .post()
                            .uri(FIGMA_GET_ID_TOKEN_URL)
                            .header(
                                    HttpHeaders.CONTENT_TYPE,
                                    MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                            .body(formData)
                            .retrieve()
                            .onStatus(
                                    status -> !status.is2xxSuccessful(),
                                    (request, responseEntity) -> {
                                        log.error(
                                                "Figma ID Token 요청 실패: {}",
                                                responseEntity.getStatusCode());
                                        throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
                                    })
                            .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, FigmaTokenResponse.class);
        } catch (Exception e) {
            log.error("Figma 로그인 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
        }
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        try {
            FigmaUserResponse figmaUserResponse =
                    restClient
                            .get()
                            .uri(FIGMA_GET_USER_INFO_URL)
                            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken)
                            .retrieve()
                            .onStatus(
                                    status -> !status.is2xxSuccessful(),
                                    (request, response) -> {
                                        log.error(
                                                "Figma 유저 정보 요청 실패: {}", response.getStatusCode());
                                        throw new CustomException(ErrorCode.FIGMA_USER_INFO_FAILED);
                                    })
                            .body(FigmaUserResponse.class);

            if (figmaUserResponse == null) {
                throw new CustomException(ErrorCode.FIGMA_USER_INFO_NOT_FOUND);
            }

            return new UserInfoResponse(
                    figmaUserResponse.handle(),
                    figmaUserResponse.email(),
                    figmaUserResponse.img_url(),
                    figmaUserResponse.id());

        } catch (Exception e) {
            log.error("Figma 유저 정보 조회 중 예외 발생 : " + e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_USER_INFO_FAILED);
        }
    }

    public FigmaTokenReissueResponse reissueAccessToken(String refreshToken) {
        try {
            String figmaInfo = figmaProperties.id() + ":" + figmaProperties.secret();

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(REFRESH_TOKEN, refreshToken);

            return restClient
                    .post()
                    .uri(
                            uriBuilder ->
                                    uriBuilder
                                            .scheme(HTTPS_SCHEME)
                                            .host(FIGMA_HOST)
                                            .path(FIGMA_TOKEN_REISSUE_URL)
                                            .build())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            BASIC_TOKEN_PREFIX
                                    + Base64.getEncoder().encodeToString(figmaInfo.getBytes()))
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Figma 토큰 재발급 요청 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.FIGMA_TOKEN_REISSUE_FAILED);
                            })
                    .body(FigmaTokenReissueResponse.class);
        } catch (Exception e) {
            log.error("Figma 토큰 재발급 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_TOKEN_REISSUE_FAILED);
        }
    }

    public GetFileInfoResponse getFileInfo(String fileId, String accessToken) {
        try {
            return restClient
                    .get()
                    .uri(
                            uriBuilder ->
                                    uriBuilder
                                            .scheme(HTTPS_SCHEME)
                                            .host(FIGMA_HOST)
                                            .path(FIGMA_FILE_INFO_URL)
                                            .queryParam("depth", "1")
                                            .build(fileId))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Figma 토큰 재발급 요청 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.GETTING_FIGMA_FILE_INFO_FAILED);
                            })
                    .body(GetFileInfoResponse.class);
        } catch (Exception e) {
            log.error("Figma 토큰 재발급 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.GETTING_FIGMA_FILE_INFO_FAILED);
        }
    }
}
