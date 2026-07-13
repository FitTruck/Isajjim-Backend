package kr.co.isajjim.domains.estimate.application.mapper;

import kr.co.isajjim.domains.estimate.application.response.AdminEstimateResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailListResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateItemListResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateItemResponse;
import kr.co.isajjim.domains.estimate.application.response.LocationDetailResponse;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.entity.EstimateItem;
import kr.co.isajjim.domains.estimate.persistence.entity.LocationDetail;
import kr.co.isajjim.domains.image.application.mapper.ImageMapper;
import kr.co.isajjim.domains.image.application.response.ImageResponse;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

public class EstimateMapper {
    public static Estimate toEstimate(
            UserEntity user
    ) {
        return Estimate.builder()
                .user(user)
                .build();
    }

    public static EstimateDetailResponse fromEstimate(
            Estimate estimate
    ) {
        List<Image> images = estimate.getImages();
        List<ImageResponse> imageResponses = images.stream().map(ImageMapper::fromImage).toList();

        List<EstimateItem> items = estimate.getEstimateItems();
        List<EstimateItemResponse> itemResponses = items.stream().map(EstimateMapper::fromEstimateItem).toList();

        LocationDetail startLocation = estimate.getStartLocation();
        LocationDetail endLocation = estimate.getEndLocation();

        return EstimateDetailResponse.builder()
                .createdDate(LocalDate.from(estimate.getCreatedDate()))
                .estimateId(estimate.getId())
                .startLocation(startLocation != null ? fromLocationDetail(startLocation) : null)
                .endLocation(endLocation != null ? fromLocationDetail(endLocation) : null)
                .preferredMovingDate(estimate.getPreferredMovingDate())
                .aiStatus(estimate.getAiStatus())
                .images(imageResponses)
                .items(itemResponses)
                .build();
    }

    public static EstimateItemResponse fromEstimateItem(
            EstimateItem estimateItem
    ) {
        return EstimateItemResponse.builder()
                .category(estimateItem.getCategory())
                .itemType(estimateItem.getType())
                .quantity(estimateItem.getQuantity())
                .build();
    }

    public static LocationDetailResponse fromLocationDetail(LocationDetail locationDetail) {
        return new LocationDetailResponse(
                locationDetail.getAddress(),
                locationDetail.getDetailAddress(),
                locationDetail.getBuildingType(),
                locationDetail.getRoomSize(),
                locationDetail.getFloor(),
                locationDetail.getElevator(),
                locationDetail.getLadderTruck(),
                locationDetail.getRoomType(),
                locationDetail.getDuplex(),
                locationDetail.getGroundStair(),
                locationDetail.getParking()
        );
    }

    public static EstimateDetailListResponse toEstimateDetailListResponse(
            List<EstimateDetailResponse> items
    ) {
        return EstimateDetailListResponse.toEstimateDetailListResponse(items);
    }

    public static AdminEstimateResponse fromEstimateForAdmin(
            Estimate estimate
    ) {
        UserEntity user = estimate.getUser();

        return AdminEstimateResponse.builder()
                .estimateId(estimate.getId())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .aiStatus(estimate.getAiStatus())
                .preferredMovingDate(estimate.getPreferredMovingDate())
                .createdDate(estimate.getCreatedDate())
                .updatedAt(estimate.getUpdatedAt())
                .build();
    }
}
