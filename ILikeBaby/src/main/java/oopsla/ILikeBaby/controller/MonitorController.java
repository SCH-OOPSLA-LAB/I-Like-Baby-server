package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.service.MonitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MonitorController {
    
    private final MonitorService monitorService;
    
    @GetMapping("/monitor/check")
    ResponseEntity<String> check(@RequestParam String accountId) {
        
        return ResponseEntity.ok().body(monitorService.checkStatus(accountId));
    }
    
    
}
