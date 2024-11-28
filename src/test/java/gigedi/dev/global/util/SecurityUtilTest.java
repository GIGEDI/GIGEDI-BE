package gigedi.dev.global.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;
    private SecurityUtil securityUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityUtil = new SecurityUtil();
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    @DisplayName("현재 사용자의 id를 불러올 때")
    class getCurrentMemberIdTest {

        @Test
        @DisplayName("성공한다")
        void getCurrentMemberId_Success() {
            // given
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("1");

            // when
            Long memberId = securityUtil.getCurrentMemberId();

            // then
            assertEquals(1L, memberId);
        }

        @Test
        @DisplayName("인증이 존재하지 않아 실패한다")
        void getCurrentMemberId_Failure_AUTH_NOT_FOUND() {
            // given
            when(securityContext.getAuthentication()).thenReturn(null);

            // when & then
            CustomException exception =
                    assertThrows(CustomException.class, () -> securityUtil.getCurrentMemberId());
            assertEquals(ErrorCode.ACCESS_TOKEN_EXPIRED, exception.getErrorCode());
        }
    }
}
