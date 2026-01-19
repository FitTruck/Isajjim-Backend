package kr.co.isajjim.domains.image.domain.service;

import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.domains.image.persistence.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Long createImage(Image image) {
        return imageRepository.save(image).getId();
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }
}
