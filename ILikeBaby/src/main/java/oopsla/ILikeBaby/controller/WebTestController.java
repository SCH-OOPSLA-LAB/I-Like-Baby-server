package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Clova;
import oopsla.ILikeBaby.repository.ClovaRepository;
import oopsla.ILikeBaby.service.ClovaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebTestController {
    
    private final ClovaRepository clovaRepository;
    private final ClovaService clovaService;
    @GetMapping("/")
    public String test() {
        return "home";
    }
    
    @GetMapping("/chat")
    public String chat(Model model) {
        
        List<Clova> find = clovaRepository.findAll();
        List<Clova> result = new ArrayList<>();
        
        for (Clova s : find) {
            String replace = s.getResponseContent();
            s.setResponseContent(replace);
            result.add(s);
        }
        
        model.addAttribute("clovaRepository", result);
        //model.addAttribute("clovaRepository", find);
        return "chat";
    }
    
   /* @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        System.out.println("message = " + message);
        clovaService.sendChatRequest(message);
        return "redirect:/chat";
    }*/
    
}
