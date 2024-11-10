package oopsla.ILikeBaby.common.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    
    private int status;
    private String error;
    
}

