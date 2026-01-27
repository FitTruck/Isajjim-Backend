package kr.co.isajjim.domains.furniture.domain.service;

import kr.co.isajjim.domains.furniture.application.mapper.FurnitureMapper;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.domains.furniture.persistence.repository.FurnitureRepository;
import kr.co.isajjim.domains.image.domain.service.ImageService;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FurnitureService {

    private final FurnitureRepository furnitureRepository;
    private final ImageService imageService;
    private final FurnitureDimensionConverter dimensionConverter;

    @Transactional
    public void saveFurnitureList(List<AIAnalysisResponse.ImageResult> imageResults) {
        for (AIAnalysisResponse.ImageResult result : imageResults) {
            saveFurniturePerImage(result);
        }
    }

    public void updateFurnitureQuantity(Long estimateId, Long furnitureId, Integer quantity) {
        Furniture furniture = getOrThrow(furnitureId);
        furniture.validateBelongsTo(estimateId);
        furniture.updateQuantity(quantity);
    }

    /* HELPER METHOD */
    private Furniture getOrThrow(Long id) {
        return furnitureRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_FURNITURE));
    }

    private void saveFurniturePerImage(AIAnalysisResponse.ImageResult result) {
        Image image = imageService.getImageById(result.imageId());

        // 그룹핑하여 label과 type이 같은 것은 같은 가구로 계산
        Map<String, Furniture> groupedMap = result.objects().stream()
                .map(info -> {
                    FurnitureType type = info.type();
                    double volume;
                    if (type == null) {
                        // 상대 길이 비율 계산
                        List<Double> sorted = Stream.of(info.width(), info.depth(), info.height()).sorted().toList();
                        double detectedRatio = sorted.get(1) / sorted.get(2);

                        type = dimensionConverter.findBestMatch(info.label(), detectedRatio);

                        volume = dimensionConverter.calculateAbsoluteVolume(info, type);
                    } else {
                        volume = type.getWidth() * type.getDepth() * type.getHeight();
                    }

                    return FurnitureMapper.toFurniture(1, info, type, volume);
                })
                .collect(Collectors.toMap(
                        f -> f.getLabel().name() + ":" + (f.getType() != null ? f.getType().name() : "NONE"),
                        f -> f,
                        (existing, replacement) -> {
                            existing.updateQuantity(existing.getQuantity() + 1);
                            return existing;
                        }
                ));

        List<Furniture> sortedList = groupedMap.values().stream()
                .sorted(Comparator.comparing(f -> f.getLabel().name()))
                .peek(f -> f.setImage(image))
                .toList();

        furnitureRepository.saveAll(sortedList);
    }
}