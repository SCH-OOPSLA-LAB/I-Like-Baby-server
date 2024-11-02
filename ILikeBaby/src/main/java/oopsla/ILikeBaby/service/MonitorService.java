package oopsla.ILikeBaby.service;


import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Member;
import oopsla.ILikeBaby.domain.enums.MonitorStatus;
import oopsla.ILikeBaby.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {
    
    private final MemberRepository memberRepository;
    
    public String checkStatus(String accountId) {
        
        Member member = memberRepository.findByAccountId(accountId).get();
        
        
        if (member.getMonitorStatus() == MonitorStatus.WAIT) {
            return "wait";
        } else {
            return "monitor";
        }
        
    }
}
