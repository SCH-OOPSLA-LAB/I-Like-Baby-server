package oopsla.ILikeBaby.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeIfs {
    
    
    MEMBER_NOT_FOUND(400, "멤버를 찾을 수 없음"),
    MEMBER_ALREADY_EXIST(400, "멤버가 이미 존재함"),
    MEMBER_WRONG_PASSWORD(400, "비밀번호 불일치"),
    MEMBER_SESSION_ERROR(400, "세션 오류"),
    MEMBER_WRONG_ADDRESS(400, "올바른 주소 입력")
    ;
    
    private final Integer httpStatusCode;
    private final String errorDescription;
}
