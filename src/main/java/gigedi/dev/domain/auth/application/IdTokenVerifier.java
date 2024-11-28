package gigedi.dev.domain.auth.application;

import static gigedi.dev.domain.member.domain.MemberRole.USER;
import static gigedi.dev.global.common.constants.SecurityConstants.GOOGLE_ISSUER;
import static gigedi.dev.global.common.constants.SecurityConstants.GOOGLE_JWK_SET_URL;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.GoogleProperties;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IdTokenVerifier {
    private final GoogleProperties googleProperties;
    private final JwtDecoder jwtDecoder = buildDecoder();

    public OidcUser getOidcUser(String idToken) {
        Jwt jwt = jwtDecoder.decode(idToken);
        OidcIdToken oidcIdToken =
                new OidcIdToken(idToken, jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());

        validateIssuer(oidcIdToken);
        validateExpired(oidcIdToken);
        validateAudience(oidcIdToken);

        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(USER.getValue()));
        return new DefaultOidcUser(authorities, oidcIdToken);
    }

    private void validateExpired(OidcIdToken oidcIdToken) {
        Instant expiration = oidcIdToken.getExpiresAt();
        if (expiration != null && expiration.isBefore(Instant.now())) {
            throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
        }
    }

    private void validateAudience(OidcIdToken oidcIdToken) {
        String clientId = oidcIdToken.getAudience().get(0);
        if (!googleProperties.getId().equals(clientId)) {
            throw new CustomException(ErrorCode.ID_TOKEN_VERIFICATION_FAILED);
        }
    }

    private void validateIssuer(OidcIdToken oidcIdToken) {
        String issuer = oidcIdToken.getIssuer().toString();
        if (!GOOGLE_ISSUER.equals(issuer)) {
            throw new CustomException(ErrorCode.ID_TOKEN_VERIFICATION_FAILED);
        }
    }

    private JwtDecoder buildDecoder() {
        String jwkUrl = GOOGLE_JWK_SET_URL;
        return NimbusJwtDecoder.withJwkSetUri(jwkUrl).build();
    }
}
