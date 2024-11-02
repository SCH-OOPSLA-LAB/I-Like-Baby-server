package oopsla.ILikeBaby.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClovaMessageDto {
    
    private Status status;
    private Result result;
    
    @Data
    public static class Status {
        private String code;
        private String message;
    }
    
    @Data
    public static class Result {
        private Message message;
        private int inputLength;
        private int outputLength;
        private String stopReason;
        private long seed;
    }
    
    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
