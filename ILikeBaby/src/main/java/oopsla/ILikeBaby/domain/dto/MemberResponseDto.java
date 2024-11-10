package oopsla.ILikeBaby.domain.dto;

import lombok.Builder;
import lombok.Data;
import oopsla.ILikeBaby.domain.enums.MonitorStatus;

@Builder
@Data
public class MemberResponseDto {
    
    String accountId;
    String password;
    String monitorStatus;
    
}
