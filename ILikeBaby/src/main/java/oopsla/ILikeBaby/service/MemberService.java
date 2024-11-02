package oopsla.ILikeBaby.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Member;
import oopsla.ILikeBaby.domain.dto.MemberRequestDto;
import oopsla.ILikeBaby.domain.dto.MemberResponseDto;
import oopsla.ILikeBaby.domain.enums.MonitorStatus;
import oopsla.ILikeBaby.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        
        if (memberRepository.findByAccountId(memberRequestDto.getAccountId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 accountId 입니다.");
        }
        
        Member member = memberRepository.save(Member.builder()
                .accountId(memberRequestDto.getAccountId())
                .password(memberRequestDto.getPassword())
                .monitorStatus(MonitorStatus.WAIT)
                .build());
        
        return MemberResponseDto.builder()
                .accountId(member.getAccountId())
                .password(member.getPassword())
                .monitorStatus(member.getMonitorStatus())
                .build();
        
    }
    
}
