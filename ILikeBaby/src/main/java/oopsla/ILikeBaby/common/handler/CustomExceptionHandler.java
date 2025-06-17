package oopsla.ILikeBaby.common.handler;

import oopsla.ILikeBaby.common.error.ErrorResponse;
import oopsla.ILikeBaby.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
    
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        // 예외 정보를 로그에 기록
        logger.warn("CustomException 발생: {} ", ex.getErrorDescription(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCodeIfs().getHttpStatusCode(),
                ex.getErrorDescription()
        );
        return ResponseEntity
                .status(HttpStatus.valueOf(ex.getErrorCodeIfs().getHttpStatusCode()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
