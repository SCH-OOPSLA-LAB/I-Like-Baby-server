package oopsla.ILikeBaby.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.common.error.MemberErrorCode;
import oopsla.ILikeBaby.common.exception.CustomException;
import oopsla.ILikeBaby.domain.Member;
import oopsla.ILikeBaby.domain.dto.MemberRequestDto;
import oopsla.ILikeBaby.repository.MemberRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    @Transactional
    public void joinMember(MemberRequestDto memberRequestDto) {
        
        boolean b = memberRepository.existsByAccountId(memberRequestDto.getAccountId());
        
        if (b) {
            throw new CustomException(MemberErrorCode.MEMBER_ALREADY_EXIST);
        }
        
        memberRepository.save(Member.builder()
                .accountId(memberRequestDto.getAccountId())
                .password(memberRequestDto.getPassword())
                .monitorStatus("WAIT")
                .build());
    }
    
    @Transactional
    public void loginMember(MemberRequestDto memberRequestDto) {
        
        Member member = memberRepository.findByAccountId(memberRequestDto.getAccountId()).orElseThrow(
                () -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        
        if (!memberRequestDto.getPassword().equals(member.getPassword())) {
            throw new CustomException(MemberErrorCode.MEMBER_WRONG_PASSWORD);
        }
        
    }
    
}
