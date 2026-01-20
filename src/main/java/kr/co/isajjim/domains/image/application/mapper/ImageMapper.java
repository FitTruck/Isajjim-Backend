package kr.co.isajjim.domains.image.application.mapper;

import kr.co.isajjim.domains.image.application.response.ImageResponse;
import kr.co.isajjim.domains.image.persistence.entity.Image;

public class ImageMapper {
    public static Image toImage(
            String imageUrl
    ) {
        return Image.builder()
                .imageUrl(imageUrl)
                .build();
    }

    public static ImageResponse fromImage(Image image) {
        return ImageResponse.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
