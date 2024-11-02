package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.dto.MemberRequestDto;
import oopsla.ILikeBaby.domain.dto.MemberResponseDto;
import oopsla.ILikeBaby.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    @PostMapping("/member/create")
    ResponseEntity<MemberResponseDto> createMember(@RequestBody MemberRequestDto memberRequestDto) {
        
        
        ArrayList<String> arrayList = new ArrayList<>();
        
        
        return ResponseEntity.ok().body(memberService.createMember(memberRequestDto));
    }
    
    
}
