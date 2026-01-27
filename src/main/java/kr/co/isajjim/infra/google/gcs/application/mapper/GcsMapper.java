package kr.co.isajjim.infra.google.gcs.application.mapper;

import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlResponse;

import java.net.URL;

public class GcsMapper {

    public static PresignedUrlResponse toPresignedUrlResponse(URL presignedUrl, String fileUrl, String key) {
        return PresignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .fileUrl(fileUrl)
                .key(key)
                .build();
    }
}
