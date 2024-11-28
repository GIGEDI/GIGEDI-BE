package gigedi.dev.domain.member.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class MemberTest {
    private OauthInfo oauthInfo;

    @BeforeEach
    void setUp() {
        oauthInfo =
                OauthInfo.of("testGoogleId", "testGoogleEmail", "testUsername", "testProfileImg");
    }

    @Nested
    @DisplayName("사용자 생성 시")
    class createMemberTest {

        @Test
        @DisplayName("성공한다")
        void createMember_Success() {
            // given & when
            Member testMember = Member.createMember(oauthInfo);

            // then
            assertEquals("testGoogleId", testMember.getOauthInfo().getGoogleId());
            assertEquals("testGoogleEmail", testMember.getOauthInfo().getGoogleEmail());
            assertEquals("testUsername", testMember.getOauthInfo().getUsername());
            assertEquals("testProfileImg", testMember.getOauthInfo().getProfileImg());
            assertNull(testMember.getDeletedAt());
            assertNotNull(testMember.getLastLoginAt());
            assertEquals(MemberRole.USER, testMember.getRole());
        }
    }

    @Nested
    @DisplayName("사용자 탈퇴 시")
    class memberWithdrawalTest {

        @Test
        @DisplayName("성공한다")
        void memberWithdrawal_Success() {
            // given
            Member testMember = Member.createMember(oauthInfo);

            // when
            testMember.memberWithdrawal();

            // then
            assertNotNull(testMember.getDeletedAt());
        }
    }
}
