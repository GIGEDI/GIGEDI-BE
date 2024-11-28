package gigedi.dev.domain.member.application;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.dto.response.MemberInfoResponse;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberUtil memberUtil;

    public MemberInfoResponse findMember() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberInfoResponse.from(currentMember);
    }
}
