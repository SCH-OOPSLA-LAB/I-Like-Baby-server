package oopsla.ILikeBaby.common.exception;

import lombok.Getter;
import oopsla.ILikeBaby.common.error.ErrorCodeIfs;

@Getter
public class CustomException extends RuntimeException {
    
    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;
    
    public CustomException(ErrorCodeIfs errorCodeIfs){
        super(errorCodeIfs.getErrorDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getErrorDescription();
    }
    
}
