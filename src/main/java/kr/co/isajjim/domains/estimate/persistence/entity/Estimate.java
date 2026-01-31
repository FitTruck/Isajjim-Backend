package kr.co.isajjim.domains.estimate.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.estimate.application.mapper.LocationDetailMapper;
import kr.co.isajjim.domains.estimate.application.request.LocationDetailRequest;
import kr.co.isajjim.domains.estimate.domain.constant.*;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "estimate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Estimate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "start_location_id")
    private LocationDetail startLocation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "end_location_id")
    private LocationDetail endLocation;

    private LocalDate preferredMovingDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AIStatus aiStatus = AIStatus.PENDING;

    @Builder.Default
    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateItem> estimateItems = new ArrayList<>();


    public void addImage(Image image) {
        this.images.add(image);
        if (image.getEstimate() != this) {
            image.setEstimate(this);
        }
    }

    public void addEstimateItem(EstimateItem estimateItem) {
        this.estimateItems.add(estimateItem);
        if (estimateItem.getEstimate() != this) {
            estimateItem.setEstimate(this);
        }
    }

    public void updateEstimate(LocalDate date) {
        this.preferredMovingDate = (date != null) ? date : this.preferredMovingDate;
    }

    public void updateAIStatus(AIStatus aiStatus) {
        this.aiStatus = aiStatus;
    }

    public void updateStartLocationDetail(LocationDetailRequest request) {
        if (this.startLocation != null) {
            this.startLocation.updateLocationDetail(request);
        } else {
            this.startLocation = LocationDetailMapper.toLocationDetail(request);
        }
    }

    public void updateEndLocationDetail(LocationDetailRequest request) {
        if (this.endLocation != null) {
            this.endLocation.updateLocationDetail(request);
        } else {
            this.endLocation = LocationDetailMapper.toLocationDetail(request);
        }
    }
}
