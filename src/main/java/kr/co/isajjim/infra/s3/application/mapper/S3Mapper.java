package kr.co.isajjim.infra.s3.application.mapper;

import kr.co.isajjim.infra.s3.application.dto.PresignedUrlListResponse;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlResponse;

import java.net.URL;
import java.util.List;

public class S3Mapper {

    public static PresignedUrlResponse toPresignedUrlResponse(URL presignedUrl, String fileUrl, String key) {
        return PresignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .fileUrl(fileUrl)
                .key(key)
                .build();
    }

    public static PresignedUrlListResponse toPresignedUrlListResponse(
            List<PresignedUrlResponse> urls
    ) {
        return PresignedUrlListResponse.builder()
                .urls(urls)
                .build();
    }
}
