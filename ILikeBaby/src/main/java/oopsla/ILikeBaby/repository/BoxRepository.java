package oopsla.ILikeBaby.repository;

import oopsla.ILikeBaby.domain.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {
    
    @Query("SELECT b.movementLevel FROM Box b " +
            "WHERE b.timeInSeconds >= :startTime AND b.timeInSeconds < :endTime")
    List<Integer> findMovementLevelsByTimeRange(@Param("startTime") Instant startTime,
                                                @Param("endTime") Instant endTime);
}
