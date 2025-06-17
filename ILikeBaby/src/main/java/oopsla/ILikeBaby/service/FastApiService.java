package oopsla.ILikeBaby.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class FastApiService {
    
    private final String preUrl = "http://127.0.0.1:8001";
    
    public String test(String url) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> response = restTemplate.getForEntity(preUrl + url, String.class);
        
        String responseGet = response.getBody();
        System.out.println("GET Response: " + responseGet);
        
        return responseGet;
    }
    
    
    public void imageSendToFastApi(String url, MultipartFile file) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        // MultipartBodyMap 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // 원본 파일 이름 반환
            }
        });
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        //Post
        ResponseEntity<String> response = restTemplate.postForEntity(preUrl + url, requestEntity, String.class);
        
        System.out.println("response.getBody() = " + response.getBody());
        
    }
    
    
    
    
    
}
