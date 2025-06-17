package oopsla.ILikeBaby.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.common.error.MemberErrorCode;
import oopsla.ILikeBaby.common.error.MonitorErrorCode;
import oopsla.ILikeBaby.common.exception.CustomException;
import oopsla.ILikeBaby.domain.Box;
import oopsla.ILikeBaby.domain.Member;
import oopsla.ILikeBaby.domain.dto.DetectionResult;
import oopsla.ILikeBaby.domain.dto.DetectionResultAndImage;
import oopsla.ILikeBaby.domain.dto.MonitorStatusResponse;
import oopsla.ILikeBaby.repository.BoxRepository;
import oopsla.ILikeBaby.repository.MemberRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MonitorService {
    
    static String folder;
    
    private final BoxRepository boxRepository;
    private final MemberRepository memberRepository;
    private static final String BASE_DIRECTORY = "C:/images/";
    
    public MonitorStatusResponse checkStatus() {
        
        Member member = memberRepository.findByAccountId("test").orElseThrow(() ->
                new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        
        
        if (member.getMonitorStatus().equals("WAIT")) {
            //System.out.println("조건문 1 " + member.getMonitorStatus());
            return MonitorStatusResponse.builder().monitorStatus("WAIT").build();
        } else {
            //System.out.println("조건문 2 " + member.getMonitorStatus());
            return MonitorStatusResponse.builder().monitorStatus("MONITORING").build();
        }
        
    }
    
    @Transactional
    public MonitorStatusResponse startMonitoring(String folder) {
        
        MonitorService.folder = folder;
        System.out.println("startMonitoring()");
        System.out.println("folder = " + folder);
        
        Member member = memberRepository.findByAccountId("test").orElseThrow(() ->
                new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        
        member.setMonitorStatus("MONITORING");
        
        // Save default image when monitoring starts
        saveDefaultImage(folder);
        
        return MonitorStatusResponse.builder().monitorStatus("MONITORING").build();
    }
    
    private void saveDefaultImage(String folder) {
        String dateFolder = LocalDate.now().toString();
        String directoryPath = BASE_DIRECTORY + dateFolder + folder;
        
        // Create directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Define the path for the default image
        Path filePath = Paths.get(directoryPath, "latest_image.jpg");
        
        try {
            ClassPathResource resource = new ClassPathResource("default_image.jpg");
            byte[] defaultImageBytes = Files.readAllBytes(resource.getFile().toPath());
            Files.write(filePath, defaultImageBytes);
            System.out.println("Default image saved at start: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save default image");
            e.printStackTrace();
        }
    }
    
    // 부모 앱에서 이미지 가져오는거
    public byte[] getMonitoringImage() {
        
        // 오늘 날짜 기준으로 이미지 폴더 경로 설정
        String dateFolder = LocalDate.now().toString();
        System.out.println("folder = " + folder);
        
        // 고정된 이미지 파일 경로 설정
        String imagePath = BASE_DIRECTORY + dateFolder + folder + "/latest_image.jpg";
        
        System.out.println("이미지 요청 = " + imagePath);
        
        try {
            Path path = Paths.get(imagePath);
            
            // 파일을 읽어서 바이트 배열로 반환
            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                System.out.println("Image not found: " + imagePath);
                throw new CustomException(MonitorErrorCode.IMAGE_NOT_FOUND);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    @Transactional
    public MonitorStatusResponse stopMonitoring() {
        System.out.println("stopMonitoring()");
        Member member = memberRepository.findByAccountId("test").orElseThrow(() ->
                new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        
        member.setMonitorStatus("WAIT");
        
        return MonitorStatusResponse.builder().monitorStatus("WAIT").build();
    }
    
    private float[] previousBoundingBox = null; // 이전 박스 좌표 저장용 필드
    
    public void saveDetectionResult(DetectionResult detectionResult) {
        
        System.out.println("saveDetectionResult()");
        
        float[] boundingBox = detectionResult.getBoundingBox();
        
        if (detectionResult.getScore() > 0.5) {
            int i = calStatus(boundingBox);
            previousBoundingBox = boundingBox;
            System.out.println("점수 = " + detectionResult.getScore());
            
            boxRepository.save(Box.builder()
                    .timeInSeconds(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                    .movementLevel(i)
                    .build());
            
        }
    }
    
    public void saveDetectionResultAndImage(DetectionResultAndImage detectionResultAndImage) {
        
        System.out.println("saveDetectionResultAndImage()");
        
        float[] boundingBox = detectionResultAndImage.getBoundingBox();
        byte[] byteImage = detectionResultAndImage.getByteImage();
        
        if (detectionResultAndImage.getScore() > 0.5) {
            int i = calStatus(boundingBox);
            previousBoundingBox = boundingBox;
            
            System.out.println("점수 = " + detectionResultAndImage.getScore());
            
            boxRepository.save(Box.builder()
                    .timeInSeconds(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                    .movementLevel(i)
                    .build());
        }
        
        String dateFolder = LocalDate.now().toString();
        String directoryPath = BASE_DIRECTORY + File.separator + dateFolder + folder;
        Path filePath = Paths.get(directoryPath, "latest_image.jpg");
        
        System.out.println("이미지 저장(덮어쓰기) = " + filePath);
        
        // Save modified byte array to file
        try {
            Files.write(filePath, byteImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private int calStatus(float[] boundingBox) {
        
        int movementLevel = 0;
        
        // 이전 좌표가 존재할 경우, 현재 좌표와의 거리 계산
        if (previousBoundingBox != null) {
            double distance = calculateDistance(previousBoundingBox, boundingBox);
            
            System.out.println("distance = " + distance);
            if (distance > 0.1) { // 높은 움직임 기준 예시
                movementLevel = 3; // 높은 움직임
            } else if (distance > 0.05) { // 중간 움직임 기준 예시
                movementLevel = 2; // 중간 움직임
            } else {
                movementLevel = 1; // 낮은 움직임
            }
            System.out.println("Movement Level: " + movementLevel);
            
            
        } else {
            System.out.println("초기 감지 - 이전 박스 좌표가 없습니다.");
        }
        
        return movementLevel;
    }
    
    // 두 박스 좌표 간의 유클리드 거리 계산 메서드
    private double calculateDistance(float[] box1, float[] box2) {
        return Math.sqrt(
                Math.pow(box2[0] - box1[0], 2) +
                        Math.pow(box2[1] - box1[1], 2) +
                        Math.pow(box2[2] - box1[2], 2) +
                        Math.pow(box2[3] - box1[3], 2)
        );
    }
}
