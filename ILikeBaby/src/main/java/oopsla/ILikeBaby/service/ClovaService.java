package oopsla.ILikeBaby.service;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Clova;
import oopsla.ILikeBaby.domain.dto.ClovaMessageDto;
import oopsla.ILikeBaby.repository.ClovaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClovaService {
    
    @Value("${clova.api.url}")
    private String apiUrl;
    
    @Value("${clova.api.key}")
    private String apiKey;
    
    @Value("${clova.api.gateway.key}")
    private String apiGatewayKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    private final ClovaRepository clovaRepository;
    
    public String sendChatRequest(String userMessage) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        headers.set("X-NCP-APIGW-API-KEY", apiGatewayKey);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", "test-request-id");
        
        // 요청 바디 생성
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);
        
        Map<String, Object> body = new HashMap<>();
        body.put("messages", new Map[]{message});
        body.put("topP", 0.8);
        body.put("temperature", 0.5);
        body.put("maxTokens", 500);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        
        // POST 요청 전송 및 응답 받기
        ResponseEntity<ClovaMessageDto> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, request, ClovaMessageDto.class
        );
        
        ClovaMessageDto clovaMessageDto = response.getBody();
        
        // 응답 내용 가져오기
        String clovaMessage = clovaMessageDto.getResult().getMessage().getContent();
        
        System.out.println("요청 = " + userMessage);
        System.out.println("응답 = " + clovaMessage);
        
        clovaRepository.save(Clova.builder()
                .requestContent(userMessage)
                .responseContent(clovaMessage)
                .build());
        
        return clovaMessage;
    }
    
    public List<Clova> readChat() {
        return clovaRepository.findAll();
    }
}

