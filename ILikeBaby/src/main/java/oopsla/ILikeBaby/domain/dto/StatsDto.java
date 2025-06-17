package oopsla.ILikeBaby.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {
    
    private List<TimeAndSum> timeAndSums;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeAndSum {
        private LocalDateTime time;
        private int sum;
    }
    
}