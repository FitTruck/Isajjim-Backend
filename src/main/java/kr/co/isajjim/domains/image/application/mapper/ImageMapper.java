package kr.co.isajjim.domains.image.application.mapper;

import kr.co.isajjim.domains.furniture.FurnitureResponse;
import kr.co.isajjim.domains.furniture.application.mapper.FurnitureMapper;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.domains.image.application.response.ImageResponse;
import kr.co.isajjim.domains.image.persistence.entity.Image;

import java.util.List;

public class ImageMapper {
    public static Image toImage(
            String imageUrl
    ) {
        return Image.builder()
                .imageUrl(imageUrl)
                .build();
    }

    public static ImageResponse fromImage(Image image) {
        List<Furniture> furnitureList = image.getFurnitures();
        List<FurnitureResponse> responses = furnitureList.stream().map(FurnitureMapper::fromFurniture).toList();

        return ImageResponse.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .furnitureList(responses)
                .build();
    }
}
