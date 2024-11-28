package gigedi.dev.domain.discord.api;

import org.springframework.web.bind.annotation.*;

import gigedi.dev.domain.discord.application.DiscordAuthService;
import gigedi.dev.domain.discord.dto.response.DiscordInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Discord Auth", description = " Discord Auth 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class DiscordAuthController {
    private final DiscordAuthService discordAuthService;

    @Operation(summary = "디스코드 소셜 로그인", description = "디스코드 연결을 진행하는 API")
    @GetMapping("/code/discord")
    public DiscordInfoResponse discordSocialLogin(@RequestParam String code) {
        return discordAuthService.discordConnect(code);
    }

    @Operation(summary = "디스코드 연결 해제", description = "디스코드 연결을 해제하는 API")
    @GetMapping("/discord/disconnect")
    public void discordSocialLogin() {
        discordAuthService.discordDisconnect();
    }
}
