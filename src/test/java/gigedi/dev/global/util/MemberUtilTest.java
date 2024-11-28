package gigedi.dev.global.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import gigedi.dev.domain.member.dao.MemberRepository;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.domain.OauthInfo;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberUtilTest {
    @InjectMocks private MemberUtil memberUtil;

    @Mock private SecurityUtil securityUtil;
    @Mock private MemberRepository memberRepository;

    @Nested
    @DisplayName("현재 사용자를 불러올 때")
    class getCurrentMemberTest {

        @Test
        @DisplayName("성공한다")
        void getCurrentMember_Success() {
            // given
            Member member =
                    Member.createMember(
                            OauthInfo.of(
                                    "testGoogleId",
                                    "testGoogleEmail",
                                    "testUsernme",
                                    "testProfileImg"));
            when(securityUtil.getCurrentMemberId()).thenReturn(member.getId());
            when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

            // when
            Member result = memberUtil.getCurrentMember();

            // then
            assertNotNull(result);
            assertEquals(member.getId(), result.getId());
        }

        @Test
        @DisplayName("사용자가 존재하지 않아 실패한다")
        void getCurrentMember_Failure_MEMBER_NOT_FOUND() {
            // given
            Long memberId = 1L;
            when(securityUtil.getCurrentMemberId()).thenReturn(memberId);
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            // when & then
            CustomException exception =
                    assertThrows(CustomException.class, () -> memberUtil.getCurrentMember());
            assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        }
    }
}
