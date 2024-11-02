package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Clova;
import oopsla.ILikeBaby.service.ClovaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        System.out.println("message = " + message);
        clovaService.sendChatRequest(message);
        return "redirect:/chat";
    }
    
    @GetMapping
    public List<Clova> read() {
        return clovaService.readChat();
    }
}

