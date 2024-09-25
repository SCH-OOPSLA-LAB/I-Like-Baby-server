package oopsla.ILikeBaby.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import oopsla.ILikeBaby.domain.enums.MonitorStatus;

@Entity
public class Member {
    
    @Id
    Long id;
    
    String accountId;
    
    String password;

    MonitorStatus monitorStatus;
            
}


