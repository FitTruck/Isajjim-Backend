package kr.co.isajjim.domains.estimate.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.estimate.application.request.LocationDetailRequest;
import kr.co.isajjim.domains.estimate.domain.constant.*;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "location_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_detail_id")
    private Long id;

    private String address;

    private String detailAddress;

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

    @Builder
    private LocationDetail(
            String address,
            String detailAddress,
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
        this.address = address;
        this.detailAddress = detailAddress;
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

    public void updateLocationDetail(
            LocationDetailRequest request
    ) {
        this.address = (request.address() != null) ? request.address() : this.address;
        this.detailAddress = (request.detailAddress() != null) ? request.detailAddress() : this.detailAddress;
        this.buildingType = (request.buildingType() != null) ? request.buildingType() : this.buildingType;
        this.roomSize = (request.roomSize() != null) ? request.roomSize() : this.roomSize;
        this.floor = (request.floor() != null) ? request.floor() : this.floor;
        this.elevator = (request.elevator() != null) ? request.elevator() : this.elevator;
        this.ladderTruck = (request.ladderTruck() != null) ? request.ladderTruck() : this.ladderTruck;
        this.roomType = (request.roomType() != null) ? request.roomType() : this.roomType;
        this.duplex = (request.duplex() != null) ? request.duplex() : this.duplex;
        this.groundStair = (request.groundStair() != null) ? request.groundStair() : this.groundStair;
        this.parking = (request.parking() != null) ? request.parking() : this.parking;
    }
}
