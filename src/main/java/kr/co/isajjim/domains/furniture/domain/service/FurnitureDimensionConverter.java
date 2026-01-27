package kr.co.isajjim.domains.furniture.domain.service;

import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FurnitureDimensionConverter {

    public Double calculateAbsoluteVolume(AIAnalysisResponse.FurnitureInfo info, FurnitureType matchedType) {
        List<Double> sortedRelative = Stream.of(info.width(), info.depth(), info.height())
                .sorted()
                .toList();

        double l1 = sortedRelative.get(0); // 상대적 높이 (제일 작은 값)
        double l2 = sortedRelative.get(1); // 중간 값
        double l3 = sortedRelative.get(2); // 가장 큰 값

        double standardLongSide = Math.max(matchedType.getWidth(), matchedType.getDepth());
        double standardShortSide = Math.min(matchedType.getWidth(), matchedType.getDepth());

        double actualHeight;
        if (matchedType.getHeight() != -1) {
            actualHeight = matchedType.getHeight();
        } else {
            // 스케일 팩터
            double scaleFactor = standardLongSide / l3;

            // 절대 높이 역산
            actualHeight = l1 * scaleFactor;
        }

        // 절대 부피 계산
        double volumeM3 = standardShortSide * standardLongSide * actualHeight;

        return volumeM3 * 1e-9;
    }

    // AI가 준 라벨의 서브 타입들 중 비율이 가장 유사한 타입을 찾아 반환
    public FurnitureType findBestMatch(FurnitureLabel label, double detectedRatio) {
        List<FurnitureType> candidates = label.getSubTypes();

        if (candidates.size() == 1) {
            return candidates.get(0);
        }

        return candidates.stream()
                .min(Comparator.comparingDouble(type ->
                        Math.abs(type.getDimensionRatio() - detectedRatio)))
                .orElse(candidates.get(0));
    }
}