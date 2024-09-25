package oopsla.ILikeBaby.controller;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.service.FastApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FastApiController {
    
    private final FastApiService fastApiService;
    
    @GetMapping("/fastapi")
    @ResponseBody
    public String test() {
        return fastApiService.test("/fastapi");
    }
    
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

        fastApiService.imageSendToFastApi("/image", file);
        return ResponseEntity.ok().body("객체 검출 성공");
    }
    
}
