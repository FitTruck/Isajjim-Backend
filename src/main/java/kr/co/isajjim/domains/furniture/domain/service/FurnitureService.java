package kr.co.isajjim.domains.furniture.domain.service;

import kr.co.isajjim.domains.furniture.application.mapper.FurnitureMapper;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.domains.furniture.persistence.repository.FurnitureRepository;
import kr.co.isajjim.domains.image.domain.service.ImageService;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FurnitureService {

    private final FurnitureRepository furnitureRepository;
    private final ImageService imageService;

    @Transactional
    public void saveFurnitureList(List<AIAnalysisResponse.ImageResult> imageResults) {
        for (AIAnalysisResponse.ImageResult result : imageResults) {
            saveFurniturePerImage(result);
        }
    }

    private void saveFurniturePerImage(AIAnalysisResponse.ImageResult result) {
        Image image = imageService.getImageById(result.imageId());

        List<Furniture> furnitureList = result.objects().stream()
                .map(info -> {
                    Furniture furniture = FurnitureMapper.toFurniture(info);
                    furniture.setImage(image);
                    return furniture;
                })
                .toList();

        furnitureRepository.saveAll(furnitureList);
    }
}
