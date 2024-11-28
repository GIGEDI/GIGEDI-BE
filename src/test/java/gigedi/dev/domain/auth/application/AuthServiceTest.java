package gigedi.dev.domain.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import gigedi.dev.domain.auth.dto.AccessTokenDto;
import gigedi.dev.domain.auth.dto.RefreshTokenDto;
import gigedi.dev.domain.auth.dto.request.TokenRefreshRequest;
import gigedi.dev.domain.auth.dto.response.GoogleLoginResponse;
import gigedi.dev.domain.auth.dto.response.TokenPairResponse;
import gigedi.dev.domain.member.dao.MemberRepository;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.domain.MemberRole;
import gigedi.dev.domain.member.domain.OauthInfo;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.MemberUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks AuthService authService;

    @Mock private GoogleService googleService;
    @Mock private IdTokenVerifier idTokenVerifier;
    @Mock private MemberRepository memberRepository;
    @Mock private JwtTokenService jwtTokenService;
    @Mock private MemberUtil memberUtil;

    @Nested
    @DisplayName("구글 소셜 로그인 시")
    class GoogleSocialLoginTest {

        @Test
        @DisplayName("성공한다")
        void googleSocialLogin_Success() {
            // given
            String code = "testCode";
            GoogleLoginResponse googleResponse =
                    new GoogleLoginResponse("accessToken", "refreshToken", "idToken");
            OidcUser oidcUser = mock(OidcUser.class);
            Member member =
                    Member.createMember(OauthInfo.of("googleId", "email", "name", "profileImg"));

            when(googleService.getIdTokenByGoogleLogin(code)).thenReturn(googleResponse);
            when(idTokenVerifier.getOidcUser(googleResponse.getIdToken())).thenReturn(oidcUser);
            when(oidcUser.getSubject()).thenReturn("googleId");
            when(oidcUser.getEmail()).thenReturn("email");
            when(oidcUser.getClaim("name")).thenReturn("name");
            when(oidcUser.getAttribute("picture")).thenReturn("profileImg");
            when(memberRepository.findByOauthInfoGoogleIdAndDeletedAtIsNull("googleId"))
                    .thenReturn(Optional.of(member));
            when(jwtTokenService.createAccessToken(member.getId(), member.getRole()))
                    .thenReturn("accessToken");
            when(jwtTokenService.createRefreshToken(member.getId())).thenReturn("refreshToken");

            // when
            TokenPairResponse result = authService.googleSocialLogin(code);

            // then
            assertNotNull(result);
            assertEquals("accessToken", result.getAccessToken());
            assertEquals("refreshToken", result.getRefreshToken());
        }
    }

    @Nested
    @DisplayName("토큰 재발급 시도 시")
    class RefreshTokenTest {

        @Test
        @DisplayName("성공한다")
        void refreshToken_Success() {
            // given
            TokenRefreshRequest request = new TokenRefreshRequest();
            request.setRefreshToken("oldRefreshToken");
            Long testTtl = 100L;
            RefreshTokenDto oldRefreshTokenDto =
                    new RefreshTokenDto(1L, "oldRefreshToken", testTtl);
            RefreshTokenDto newRefreshTokenDto =
                    new RefreshTokenDto(1L, "newRefreshToken", testTtl);
            AccessTokenDto accessTokenDto =
                    new AccessTokenDto(1L, MemberRole.USER, "newAccessToken");
            Member member = mock(Member.class);

            when(jwtTokenService.validateRefreshToken("oldRefreshToken"))
                    .thenReturn(oldRefreshTokenDto);
            when(jwtTokenService.refreshRefreshToken(oldRefreshTokenDto))
                    .thenReturn(newRefreshTokenDto);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(jwtTokenService.refreshAccessToken(member)).thenReturn(accessTokenDto);

            // when
            TokenPairResponse result = authService.refreshToken(request);

            // then
            assertNotNull(result);
            assertEquals("newAccessToken", result.getAccessToken());
            assertEquals("newRefreshToken", result.getRefreshToken());
        }

        @Test
        @DisplayName("만료된 리프레시 토큰으로 갱신 실패한다")
        void refreshToken_Failure_EXPIRED_JWT_TOKEN() {
            // given
            TokenRefreshRequest request = new TokenRefreshRequest();
            request.setRefreshToken("expiredToken");
            when(jwtTokenService.validateRefreshToken("expiredToken")).thenReturn(null);

            // when & then
            CustomException exception =
                    assertThrows(CustomException.class, () -> authService.refreshToken(request));
            assertEquals(ErrorCode.EXPIRED_JWT_TOKEN, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("로그아웃 시")
    class MemberLogoutTest {

        @Test
        @DisplayName("성공한다")
        void memberLogout_Success() {
            // given
            Member currentMember = mock(Member.class);
            when(memberUtil.getCurrentMember()).thenReturn(currentMember);

            // when
            authService.memberLogout();

            // then
            verify(jwtTokenService).deleteRefreshToken(currentMember);
        }
    }

    @Nested
    @DisplayName("회원탈퇴 시")
    class MemberWithdrawalTest {

        @Test
        @DisplayName("성공한다")
        void memberWithdrawal_Success() {
            // given
            Member currentMember = mock(Member.class);
            when(memberUtil.getCurrentMember()).thenReturn(currentMember);
            when(currentMember.getId()).thenReturn(1L);

            // when
            authService.memberWithdrawal();

            // then
            verify(jwtTokenService).deleteRefreshToken(currentMember);
            verify(googleService).googleWithdrawal(1L);
            verify(currentMember).memberWithdrawal();
        }
    }
}
