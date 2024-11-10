package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.dto.MemberRequestDto;
import oopsla.ILikeBaby.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    
    private final MemberService memberService;
    
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberRequestDto memberRequestDto) {
        
        System.out.println("memberRequestDTO.getAccountId() = " + memberRequestDto.getAccountId());
        System.out.println("memberRequestDTO.getPassword() = " + memberRequestDto.getPassword());
        
        memberService.joinMember(memberRequestDto);
        
        System.out.println("회원가입 성공");
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequestDto memberRequestDto) {
        
        
        System.out.println("member.getAccountId() = " + memberRequestDto.getAccountId());
        System.out.println("member.getPassword() = " + memberRequestDto.getPassword());
        
        memberService.loginMember(memberRequestDto);
        
        System.out.println("로그인 성공");
        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }
    
    
    
}
