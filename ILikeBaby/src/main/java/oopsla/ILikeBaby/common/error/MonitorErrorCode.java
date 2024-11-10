package oopsla.ILikeBaby.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MonitorErrorCode implements ErrorCodeIfs {
    
    
    IMAGE_NOT_FOUND(400, "이미지를 찾을 수 없음")
    ;
    
    private final Integer httpStatusCode;
    private final String errorDescription;
}
