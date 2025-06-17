package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.dto.StatsDto;
import oopsla.ILikeBaby.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {
    
    
    private final StatsService statsService;
    
    //day = 20241110
    @GetMapping("/{day}")
    public ResponseEntity<StatsDto> stats(@PathVariable String day) {
        return statsService.calStats(day);
    }
    
    
    @ResponseBody
    @GetMapping("/sample")
    public String sample() {
        statsService.insertSampleData();
        return "저장 완료";
    }
    
    
}
