package oopsla.ILikeBaby.repository;


import oopsla.ILikeBaby.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
}
