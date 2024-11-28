package gigedi.dev.domain.member.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gigedi.dev.domain.member.application.MemberService;
import gigedi.dev.domain.member.dto.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "웹사이트 프로필 조회", description = "웹사이트 헤더 프로필을 조회하는 API")
    @GetMapping
    public MemberInfoResponse getMember() {
        return memberService.findMember();
    }
}
