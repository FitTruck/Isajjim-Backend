package kr.co.isajjim.domains.estimate.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record EstimateRequest(

        @NotEmpty
        List<@NotBlank @URL String> imageUrls
) {
}
