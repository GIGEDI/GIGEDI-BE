package gigedi.dev.domain.auth.application;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.dao.FigmaRepository;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.auth.dto.AccessTokenDto;
import gigedi.dev.domain.auth.dto.RefreshTokenDto;
import gigedi.dev.domain.auth.dto.request.TokenRefreshRequest;
import gigedi.dev.domain.auth.dto.response.*;
import gigedi.dev.domain.figma.application.FigmaService;
import gigedi.dev.domain.member.dao.MemberRepository;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.domain.OauthInfo;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final GoogleService googleService;
    private final FigmaApiService figmaApiService;
    private final IdTokenVerifier idTokenVerifier;
    private final MemberRepository memberRepository;
    private final FigmaRepository figmaRepository;
    private final JwtTokenService jwtTokenService;
    private final FigmaService figmaService;
    private final MemberUtil memberUtil;

    public TokenPairResponse googleSocialLogin(String code) {
        GoogleLoginResponse response =
                googleService.getIdTokenByGoogleLogin(
                        URLDecoder.decode(code, StandardCharsets.UTF_8));
        OidcUser user = idTokenVerifier.getOidcUser(response.getIdToken());
        Member member = getOrCreateMember(user);
        googleService.saveGoogleRefreshToken(member.getId(), response.getRefreshToken());
        return createTokenPair(member);
    }

    public UserInfoResponse figmaSocialLogin(String code) {
        final Member currentMember = memberUtil.getCurrentMember();
        FigmaTokenResponse tokenResponse =
                figmaApiService.getAccessToken(URLDecoder.decode(code, StandardCharsets.UTF_8));
        figmaService.validateFigmaAccountAlreadyExists(tokenResponse.userId().toString());
        UserInfoResponse userInfo = figmaApiService.getUserInfo(tokenResponse.accessToken());
        saveFigmaAccountInfo(currentMember, userInfo, tokenResponse);
        return userInfo;
    }

    private void saveFigmaAccountInfo(
            Member member, UserInfoResponse userInfo, FigmaTokenResponse tokenResponse) {
        Figma figma =
                Figma.createFigma(
                        userInfo.userName(),
                        userInfo.email(),
                        userInfo.ImgUrl(),
                        userInfo.userId(),
                        member,
                        tokenResponse.refreshToken());
        figmaRepository.save(figma);
    }

    public List<FigmaAccountResponse> getFigmaAccount() {
        final Member currentMember = memberUtil.getCurrentMember();
        List<Figma> figmaList = figmaRepository.findByMemberAndDeletedAtIsNull(currentMember);
        return figmaList.stream()
                .map(figma -> new FigmaAccountResponse(figma.getFigmaId(), figma.getEmail()))
                .toList();
    }

    @Transactional
    public void deleteFigmaAccount(Long figmaId) {
        Figma figma =
                figmaRepository
                        .findByFigmaIdAndDeletedAtIsNull(figmaId)
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_ACCOUNT_NOT_FOUND));
        figma.deleteFigmaAccount();
    }

    public TokenPairResponse refreshToken(TokenRefreshRequest request) {
        RefreshTokenDto oldRefreshTokenDto =
                jwtTokenService.validateRefreshToken(request.getRefreshToken());

        if (oldRefreshTokenDto == null) {
            throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
        }
        RefreshTokenDto newRefreshTokenDto =
                jwtTokenService.refreshRefreshToken(oldRefreshTokenDto);
        AccessTokenDto accessTokenDto =
                jwtTokenService.refreshAccessToken(getMember(newRefreshTokenDto));
        return new TokenPairResponse(accessTokenDto.getToken(), newRefreshTokenDto.getToken());
    }

    public void memberLogout() {
        final Member currentMember = memberUtil.getCurrentMember();
        jwtTokenService.deleteRefreshToken(currentMember);
    }

    public void memberWithdrawal() {
        final Member currentMember = memberUtil.getCurrentMember();
        jwtTokenService.deleteRefreshToken(currentMember);
        googleService.googleWithdrawal(currentMember.getId());
        currentMember.memberWithdrawal();
    }

    private Member getMember(RefreshTokenDto refreshTokenDto) {
        return memberRepository
                .findById(refreshTokenDto.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Member getOrCreateMember(OidcUser user) {
        OauthInfo oauthInfo = createOauthInfo(user);
        return memberRepository
                .findByOauthInfoGoogleIdAndDeletedAtIsNull(oauthInfo.getGoogleId())
                .orElseGet(() -> memberRepository.save(Member.createMember(oauthInfo)));
    }

    private OauthInfo createOauthInfo(OidcUser user) {
        Object picture = user.getAttribute("picture");
        String profileImg = picture != null ? picture.toString() : null;

        return OauthInfo.of(user.getSubject(), user.getEmail(), user.getClaim("name"), profileImg);
    }

    private TokenPairResponse createTokenPair(Member member) {
        String accessToken = jwtTokenService.createAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtTokenService.createRefreshToken(member.getId());
        return new TokenPairResponse(accessToken, refreshToken);
    }
}
