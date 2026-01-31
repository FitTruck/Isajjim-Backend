package kr.co.isajjim.domains.estimate.application.request;

import java.time.LocalDate;

public record EstimateUpdateRequest(
        LocalDate date,
        LocationDetailRequest startLocation,
        LocationDetailRequest endLocation
) {
}
