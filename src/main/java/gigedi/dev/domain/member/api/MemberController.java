package gigedi.dev.domain.member.api;

import gigedi.dev.domain.member.application.MemberService;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.dto.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    //예시
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회하는 API")
    @GetMapping("/info")
    public List<MemberInfoResponse> memberInfo() {
        return memberService.findMemberInfo();
    }

}
