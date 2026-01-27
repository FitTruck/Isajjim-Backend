package kr.co.isajjim.domains.estimate.domain.service;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateItemMapper;
import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemCategory;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemType;
import kr.co.isajjim.domains.estimate.persistence.repository.EstimateRepository;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.domains.image.application.mapper.ImageMapper;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.domains.image.domain.service.ImageService;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EstimateService {

    @Value("${estimate.extra-volume-ratio}")
    private double extraVolumeRatio;

    private final EstimateRepository estimateRepository;
    private final ImageService imageService;

    public Estimate getEstimateById(Long estimateId) {
        return getOrThrow(estimateId);
    }

    public Long createEstimate(EstimateRequest request) {
        Estimate estimate = EstimateMapper.toEstimate();
        request.imageUrls().forEach(url -> estimate.addImage(ImageMapper.toImage(url)));

        return estimateRepository.save(estimate).getId();
    }

    public List<ImageAnalysisDto> getImageDtos(Long estimateId) {
        return imageService.getAllByEstimateId(estimateId).stream()
                .map(img -> new ImageAnalysisDto(img.getId(), img.getImageUrl()))
                .toList();
    }

    public boolean isAIProcessingCompleted(Long estimateId) {
        Estimate estimate = getOrThrow(estimateId);
        return estimate.getAiStatus() == AIStatus.COMPLETED;
    }

    public void updateDefaultInfo(Estimate estimate, EstimateUpdateRequest request) {
        estimate.updateEstimate(
                request.buildingType(),
                request.roomSize(),
                request.floor(),
                request.elevator(),
                request.ladderTruck(),
                request.roomType(),
                request.duplex(),
                request.groundStair(),
                request.parking()
        );
    }

    @Transactional
    public void updateAIStatus(Long estimateId, AIStatus status) {
        Estimate estimate = getOrThrow(estimateId);
        estimate.updateAIStatus(status);
    }

    @Transactional
    public void calculateTruck(Long estimateId) {
        Estimate estimate = getEstimateById(estimateId);

        // 1. 총 부피 계산 (가구 리스트 합산)
        double totalVolume = estimate.getImages().stream()
                .flatMap(image -> image.getFurnitures().stream())
                .mapToDouble(Furniture::getVolume)
                .sum();

        // 2. 빈 공간 고려 (20% 여유)
        double requiredVolume = totalVolume * extraVolumeRatio;

        // 3. 최적 트럭 조합 계산 (ItemType 사용)
        Map<ItemType, Integer> truckCombination = determineTruckCombination(requiredVolume);

        // 4. 결과 저장 (EstimateItem으로 변환하여 추가)
        // 기존에 계산된 트럭 아이템이 있다면 삭제 로직이 필요할 수 있습니다.
        estimate.getEstimateItems().removeIf(item -> item.getCategory() == ItemCategory.TRUCK);

        truckCombination.forEach((type, qty) -> {
            estimate.addEstimateItem(
                    EstimateItemMapper.toEstimateItem(
                            estimate,
                            ItemCategory.TRUCK,
                            type,
                            qty
                    )
            );
        });
    }

    public Map<ItemType, Integer> determineTruckCombination(double volume) {
        // 트럭 타입만 오름차순으로 가져오기
        List<ItemType> truckTypes = ItemType.getTruckTypesAsc();
        ItemType maxTruck = truckTypes.get(truckTypes.size() - 1); // 5톤

        // 1. 필요한 최소 트럭 대수 계산
        int minTruckCount = (int) Math.ceil(volume / maxTruck.getCapacity());
        if (minTruckCount == 0 && volume > 0) minTruckCount = 1;

        // 2. 해당 대수 내에서 최적의 조합 찾기
        for (int n = minTruckCount; n <= minTruckCount + 1; n++) {
            List<ItemType> bestCombination = findBestCombination(volume, n, truckTypes);
            if (bestCombination != null) {
                return convertToMap(bestCombination);
            }
        }

        return fallbackGreedy(volume, truckTypes);
    }

    private List<ItemType> findBestCombination(double target, int n, List<ItemType> truckTypes) {
        // n이 너무 크면(예: 4대 이상) 계산 효율을 위해 모든 조합을 찾지 않고 그리디로 넘기는 것이 안전합니다.
        if (n > 4) {
            return null;
        }

        List<List<ItemType>> allCombos = new ArrayList<>();
        // 3번째 인자로 시작 인덱스인 0을 전달하여 StackOverflow 방지 및 중복 제거
        generateCombinations(new ArrayList<>(), truckTypes, 0, n, allCombos);

        return allCombos.stream()
                .filter(combo -> getSum(combo) >= target) // 목표 부피를 만족하는 조합 중
                .min(Comparator.comparingDouble(this::getSum)) // 낭비가 가장 적은(합계가 최소인) 조합 선택
                .orElse(null);
    }

    private void generateCombinations(List<ItemType> current, List<ItemType> types, int start, int n, List<List<ItemType>> result) {
        // 1. 탈출 조건 (원하는 개수만큼 다 뽑았을 때)
        if (current.size() == n) {
            result.add(new ArrayList<>(current));
            return;
        }

        // 2. start 변수를 사용하여 이미 뽑았던 조합을 또 뽑지 않도록 방지
        for (int i = start; i < types.size(); i++) {
            current.add(types.get(i));
            // 중복 조합이므로 다음 재귀에서도 현재 인덱스(i)부터 탐색 가능하게 넘김
            generateCombinations(current, types, i, n, result);
            current.remove(current.size() - 1); // 백트래킹 (가지치기)
        }
    }

    private double getSum(List<ItemType> combo) {
        return combo.stream().mapToDouble(ItemType::getCapacity).sum();
    }

    private Map<ItemType, Integer> convertToMap(List<ItemType> combination) {
        Map<ItemType, Integer> map = new EnumMap<>(ItemType.class);
        for (ItemType type : combination) {
            map.put(type, map.getOrDefault(type, 0) + 1);
        }
        return map;
    }

    private Map<ItemType, Integer> fallbackGreedy(double volume, List<ItemType> truckTypes) {
        Map<ItemType, Integer> result = new EnumMap<>(ItemType.class);
        double remainingVolume = volume;

        // 내림차순 정렬 (큰 트럭부터)
        List<ItemType> descendingTrucks = new ArrayList<>(truckTypes);
        Collections.reverse(descendingTrucks);

        for (ItemType truck : descendingTrucks) {
            int count = (int) (remainingVolume / truck.getCapacity());
            if (count > 0) {
                result.put(truck, count);
                remainingVolume -= (count * truck.getCapacity());
            }
        }

        if (remainingVolume > 0) {
            final double finalRemainingVolume = remainingVolume;
            ItemType smallestAvailable = truckTypes.stream()
                    .filter(t -> t.getCapacity() >= finalRemainingVolume)
                    .findFirst()
                    .orElse(descendingTrucks.get(0)); // 5톤

            result.put(smallestAvailable, result.getOrDefault(smallestAvailable, 0) + 1);
        }

        return result;
    }

    /* HELPER METHOD */
    private Estimate getOrThrow(Long id) {
        return estimateRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_ESTIMATE));
    }
}
