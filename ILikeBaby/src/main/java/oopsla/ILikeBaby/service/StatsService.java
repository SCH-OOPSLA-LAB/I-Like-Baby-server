package oopsla.ILikeBaby.service;

import lombok.RequiredArgsConstructor;
import oopsla.ILikeBaby.domain.Box;
import oopsla.ILikeBaby.domain.dto.StatsDto;
import oopsla.ILikeBaby.repository.BoxRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StatsService {
    
    private final BoxRepository boxRepository;
    
    public ResponseEntity<StatsDto> calStats(String day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate parsedDate = LocalDate.parse(day, formatter);
        
        // 자정부터 시작해 24시간 동안 10분 단위로 반복
        LocalDateTime startDateTime = parsedDate.atStartOfDay();
        
        System.out.println("startDateTime = " + startDateTime);
        LocalDateTime endDateTime = startDateTime.plusDays(1);
        
        // 원하는 시간대에 맞는 ZoneId로 변환 (예: "Asia/Seoul")
        ZoneId zoneId = ZoneId.systemDefault();
        
        Instant startTime = startDateTime.atZone(zoneId).toInstant();
        Instant endTime = endDateTime.atZone(zoneId).toInstant();
        
        List<StatsDto.TimeAndSum> timeAndSums = new ArrayList<>();
        
        while (startTime.isBefore(endTime)) {
            Instant nextTime = startTime.plus(10, ChronoUnit.MINUTES);
            //System.out.println("Querying range: " + startTime + " to " + nextTime); // 디버그 로그 추가
            
            List<Integer> movementLevels = boxRepository.findMovementLevelsByTimeRange(startTime, nextTime);
            //System.out.println("Movement levels: " + movementLevels); // 쿼리 결과 디버그 로그 추가
            
            int weightedSum = movementLevels.stream()
                    .mapToInt(level -> {
                        switch (level) {
                            case 1:
                                return 1;
                            case 2:
                                return 5;
                            case 3:
                                return 10;
                            default:
                                return 0;
                        }
                    })
                    .sum();
            timeAndSums.add(new StatsDto.TimeAndSum(startDateTime, weightedSum));
            
            startTime = nextTime;
            startDateTime = startDateTime.plusMinutes(10); // LocalDateTime도 같이 증가시킴
        }
        
        // StatsDto에 결과를 담아서 반환
        StatsDto statsDto = new StatsDto(timeAndSums);
        return ResponseEntity.ok(statsDto);
    }
    
    public void insertSampleData() {
        
        // 데이터 생성 시작 시간과 종료 시간
        Instant startTime = Instant.parse("2024-11-12T15:01:00Z"); // UTC 시간대 기준
        Instant endTime = startTime.plus(4, ChronoUnit.HOURS);
        
        Random random = new Random();
        Instant currentTime = startTime;
        List<Box> boxList = new ArrayList<>();
        
        while (currentTime.isBefore(endTime)) {
            // 얕은 잠과 깊은 잠의 주기를 반영한 movementLevel 설정
            int movementLevel;
            long elapsedMinutes = ChronoUnit.MINUTES.between(startTime, currentTime);
            
            if ((elapsedMinutes / 45) % 2 == 0) { // 45분 간격으로 주기를 변경
                movementLevel = 2; // 얕은 잠: movementLevel 3
            } else {
                movementLevel = random.nextInt(2) + 1; // 깊은 잠: movementLevel 1 또는 2
            }
            
            // Box 엔티티 생성 및 리스트에 추가
            boxList.add(Box.builder()
                    .timeInSeconds(currentTime.truncatedTo(ChronoUnit.SECONDS))
                    .movementLevel(movementLevel)
                    .build());
            
            // 1초씩 증가
            currentTime = currentTime.plus(1, ChronoUnit.SECONDS);
        }
        
        // 한 번에 모든 Box 엔티티 저장
        boxRepository.saveAll(boxList);
    }
    
    
}
