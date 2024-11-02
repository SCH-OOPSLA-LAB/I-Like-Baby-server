package oopsla.ILikeBaby.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberRequestDto {
    
    String accountId;
    String password;
    
}
