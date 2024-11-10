package oopsla.ILikeBaby.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.dto.DetectionResult;
import oopsla.ILikeBaby.domain.dto.DetectionResultAndImage;
import oopsla.ILikeBaby.domain.dto.MonitorStatusResponse;
import oopsla.ILikeBaby.service.MonitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/monitor")
public class MonitorController {
    
    private final MonitorService monitorService;
    
    // 카메라 앱에서 부모의 모니터링 상태 체크
    @GetMapping("/check")
    ResponseEntity<MonitorStatusResponse> check() {
        
        return ResponseEntity.ok().body(monitorService.checkStatus());
    }
    
    // 부모 앱에서 모니터링 요청 WAIT -> MONITORING
    @GetMapping("/monitoring/start")
    ResponseEntity<MonitorStatusResponse> monitoring(@RequestParam String folder) {
        
        return ResponseEntity.ok().body(monitorService.startMonitoring(folder));
    }
    
    @GetMapping("/monitoring/stop")
    ResponseEntity<MonitorStatusResponse> monitoringStop() {
        
        return ResponseEntity.ok().body(monitorService.stopMonitoring());
    }
    

    
    //이미지를 부모 폰에 반환
    @GetMapping(value = "/image/bumo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> imageBumo() {
        
        byte[] imageBytes = monitorService.getMonitoringImage();
        
        if (imageBytes == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.ok(imageBytes);
    }
    
    //카메라 앱에서 박스값 받는거
    @PostMapping(value = "/box")
    public ResponseEntity<String> getModelResult(
            @RequestBody DetectionResult detectionResult) {
        
        System.out.println("getModelResult()");
        
        monitorService.saveDetectionResult(detectionResult);
        
        return ResponseEntity.ok().body("박스 받음");
    }
    
    //카메라 앱에서 박스값 이미지 받는거
    @PostMapping(value = "/boximage")
    public ResponseEntity<String> getModelResultAndImage(
            @RequestBody DetectionResultAndImage detectionResultAndImage) {
        
        System.out.println("getModelResultAndImage()");
    
        monitorService.saveDetectionResultAndImage(detectionResultAndImage);
        
        return ResponseEntity.ok().body("박스 이미지 받음");
    }
    
    
    
    
    //테스트용 기본 이미지 반환
    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage() throws IOException {
        System.out.println("/image 테스트 이미지 요청");
        
        String imagePath = "src/main/resources/images/aa.jpg";
        Path path = Paths.get(imagePath);
        byte[] bytes = Files.readAllBytes(path);
        //System.out.println("bytes = " + Arrays.toString(bytes));
        return ResponseEntity.ok(bytes);
    }
    
    
}
