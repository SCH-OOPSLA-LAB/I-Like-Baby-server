package oopsla.ILikeBaby.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Clova;
import oopsla.ILikeBaby.domain.dto.ChatResponse;
import oopsla.ILikeBaby.repository.ClovaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClovaService {
    
    @Value("${clova.api.url}")
    private String apiUrl;
    
    @Value("${clova.api.key}")
    private String apiKey;
    
    @Value("${clova.api.gateway.key}")
    private String apiGatewayKey;
    
    private final ClovaRepository clovaRepository;
    
    public ChatResponse sendChatRequest(String message) {
        
        
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        headers.set("X-NCP-APIGW-API-KEY", apiGatewayKey);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", UUID.randomUUID().toString());
        
        // JSON Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("topP", 0.8);
        payload.put("topK", 0);
        payload.put("maxTokens", 150);
        payload.put("temperature", 0.5);
        payload.put("repeatPenalty", 5.0);
        payload.put("includeAiFilters", true);
        payload.put("seed", 0);
        
        // Messages list
        List<Map<String, Object>> messages = new ArrayList<>();
        
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "-영아 부모님을 위한 상담 서비스 입니다." +
                "\n-답변을 짧게 생성해줘" +
                "\n-알아 들을 수 없으면 알아 들을 수 없다고 대답해줘ㅜㅡ" +
                "\n\n");
        
        Map<String, Object> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", message);
        
        messages.add(systemMessage);
        messages.add(userMessageMap);
        
        payload.put("messages", messages);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        String content;
        
        try {
            JsonNode root = mapper.readTree(response.getBody());
            content = root.path("result").path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        ChatResponse result = ChatResponse.builder()
                .message(content)
                .build();
        
        
        System.out.println("content = " + result);
        //return content;
        return result;
    }
    
    public List<Clova> readChat() {
        return clovaRepository.findAll();
    }
}

