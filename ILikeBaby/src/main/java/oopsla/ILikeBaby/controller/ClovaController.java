package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Clova;
import oopsla.ILikeBaby.domain.dto.ChatResponse;
import oopsla.ILikeBaby.service.ClovaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/clova")
@RequiredArgsConstructor
public class ClovaController {
    
    private final ClovaService clovaService;

    
    @GetMapping("/chat")
    public String resultPage() {
        return "chat"; 
    }
    
    @ResponseBody
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody String message) {
        System.out.println(message);
        return clovaService.sendChatRequest(message);
    }
    
    @GetMapping
    public List<Clova> read() {
        return clovaService.readChat();
    }
}

