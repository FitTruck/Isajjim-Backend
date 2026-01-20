package kr.co.isajjim.domains.estimate.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.estimate.domain.constant.*;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "estimate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AIStatus aiStatus;

    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;

    @Enumerated(EnumType.STRING)
    private RoomSize roomSize;

    @Enumerated(EnumType.STRING)
    private Floor floor;

    private Boolean elevator;

    @Enumerated(EnumType.STRING)
    private LadderTruck ladderTruck;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private Boolean duplex;

    private Boolean groundStair;

    private Boolean parking;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateItem> estimateItems = new ArrayList<>();

    @Builder
    private Estimate(
            BuildingType buildingType,
            RoomSize roomSize,
            Floor floor,
            Boolean elevator,
            LadderTruck ladderTruck,
            RoomType roomType,
            Boolean duplex,
            Boolean groundStair,
            Boolean parking
    ) {
        this.aiStatus = AIStatus.PENDING;
        this.buildingType = buildingType;
        this.roomSize = roomSize;
        this.floor = floor;
        this.elevator = elevator;
        this.ladderTruck = ladderTruck;
        this.roomType = roomType;
        this.duplex = duplex;
        this.groundStair = groundStair;
        this.parking = parking;
    }

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

    public void updateEstimate(
            BuildingType buildingType,
            RoomSize roomSize,
            Floor floor,
            Boolean elevator,
            LadderTruck ladderTruck,
            RoomType roomType,
            Boolean duplex,
            Boolean groundStair,
            Boolean parking
    ) {
        this.buildingType = (buildingType != null) ? buildingType : this.buildingType;
        this.roomSize = (roomSize != null) ? roomSize : this.roomSize;
        this.floor = (floor != null) ? floor : this.floor;
        this.elevator = (elevator != null) ? elevator : this.elevator;
        this.ladderTruck = (ladderTruck != null) ? ladderTruck : this.ladderTruck;
        this.roomType = (roomType != null) ? roomType : this.roomType;
        this.duplex = (duplex != null) ? duplex : this.duplex;
        this.groundStair = (groundStair != null) ? groundStair : this.groundStair;
        this.parking = (parking != null) ? parking : this.parking;
    }
}
