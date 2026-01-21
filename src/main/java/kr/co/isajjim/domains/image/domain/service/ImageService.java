package kr.co.isajjim.domains.image.domain.service;

import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.domains.image.persistence.repository.ImageRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Image getImageById(Long imageId) {
        return getOrThrow(imageId);
    }

    public Long createImage(Image image) {
        return imageRepository.save(image).getId();
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    /* HELPER METHOD */
    private Image getOrThrow(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND));
    }

    public List<Image> getAllByEstimateId(Long estimateId) {
        return imageRepository.findAllByEstimateId(estimateId);
    }
}
