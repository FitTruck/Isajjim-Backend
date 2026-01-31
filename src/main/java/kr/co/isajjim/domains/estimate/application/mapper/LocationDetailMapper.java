package kr.co.isajjim.domains.estimate.application.mapper;

import kr.co.isajjim.domains.estimate.application.request.LocationDetailRequest;
import kr.co.isajjim.domains.estimate.domain.constant.*;
import kr.co.isajjim.domains.estimate.persistence.entity.*;

public class LocationDetailMapper {
    public static LocationDetail toLocationDetail(
            LocationDetailRequest request
    ) {
        return LocationDetail.builder()
                .address(request.address())
                .detailAddress(request.detailAddress())
                .buildingType(request.buildingType())
                .roomSize(request.roomSize())
                .floor(request.floor())
                .elevator(request.elevator())
                .ladderTruck(request.ladderTruck())
                .roomType(request.roomType())
                .duplex(request.duplex())
                .groundStair(request.groundStair())
                .parking(request.parking())
                .build();
    }
}
