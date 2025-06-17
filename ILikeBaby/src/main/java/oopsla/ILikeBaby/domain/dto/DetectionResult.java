package oopsla.ILikeBaby.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 탐지 결과를 저장할 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectionResult {
    float[] boundingBox;  // 좌표 배열로 변경
    int classId;
    float score;
}