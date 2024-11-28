package gigedi.dev.domain.auth.application;

import static gigedi.dev.global.common.constants.SecurityConstants.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import gigedi.dev.domain.auth.dao.GoogleRepository;
import gigedi.dev.domain.auth.domain.Google;
import gigedi.dev.domain.auth.dto.response.GoogleLoginResponse;
import gigedi.dev.domain.auth.dto.response.GoogleReissueResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.GoogleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GoogleService {
    private final GoogleProperties googleProperties;
    private final GoogleRepository googleRepository;
    private final RestClient restClient;

    public GoogleLoginResponse getIdTokenByGoogleLogin(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(CODE_KEY, code);
            formData.add(CLIENT_ID_KEY, googleProperties.getId());
            formData.add(CLIENT_SECRET_KEY, googleProperties.getSecret());
            formData.add(REDIRECT_URI_KEY, googleProperties.getRedirectUri());
            formData.add(GRANT_TYPE_KEY, googleProperties.getGrantType());

            return restClient
                    .post()
                    .uri(GOOGLE_ID_TOKEN_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Google ID Token 요청 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.GOOGLE_LOGIN_FAILED);
                            })
                    .body(GoogleLoginResponse.class);
        } catch (Exception e) {
            log.error("Google 로그인 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.GOOGLE_LOGIN_FAILED);
        }
    }

    public void saveGoogleRefreshToken(Long memberId, String token) {
        googleRepository.save(Google.of(memberId, token));
    }

    public void googleWithdrawal(Long memberId) {
        Google google =
                googleRepository
                        .findById(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.GOOGLE_AUTH_NOT_FOUND));
        GoogleReissueResponse response = getAccessTokenWithRefreshToken(google.getToken());
        googleWithdrawalWithAccessToken(response.getAccessToken());
        googleRepository.delete(google);
    }

    private GoogleReissueResponse getAccessTokenWithRefreshToken(String refreshToken) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(GRANT_TYPE_KEY, REISSUE_GRANT_TYPE_VALUE);
            formData.add(REFRESH_TOKEN, refreshToken);
            formData.add(CLIENT_ID_KEY, googleProperties.getId());
            formData.add(CLIENT_SECRET_KEY, googleProperties.getSecret());

            return restClient
                    .post()
                    .uri(GOOGLE_TOKEN_REISSUE_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Access Token 요청 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.GOOGLE_TOKEN_REISSUE_FAILED);
                            })
                    .body(GoogleReissueResponse.class);
        } catch (Exception e) {
            log.error("Access Token 요청 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.GOOGLE_TOKEN_REISSUE_FAILED);
        }
    }

    private void googleWithdrawalWithAccessToken(String accessToken) {
        try {
            restClient
                    .post()
                    .uri(GOOGLE_WITHDRAWAL_URL + accessToken)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Google 계정 연결 해제 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.GOOGLE_WITHDRAWAL_FAILED);
                            })
                    .body(Void.class);
        } catch (Exception e) {
            log.error("Google 계정 연결 해제 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.GOOGLE_WITHDRAWAL_FAILED);
        }
    }
}
