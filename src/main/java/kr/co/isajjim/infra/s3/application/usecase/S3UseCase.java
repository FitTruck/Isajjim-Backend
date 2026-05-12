package kr.co.isajjim.infra.s3.application.usecase;

import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlListResponse;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlRequest;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlResponse;
import kr.co.isajjim.infra.s3.application.mapper.S3Mapper;
import kr.co.isajjim.infra.s3.domain.service.S3Service;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class S3UseCase {

    private final S3Service s3Service;

    public PresignedUrlListResponse generatePresignedUrl(
            PresignedUrlRequest request
    ) {
        List<PresignedUrlResponse> list = request.fileNames().stream().map(
                fileName -> {
                    //todo AI 서버에서 지원하는 확장자 검증
                    //validSupportedExtension(fileName);

                    String key = s3Service.createKey(fileName, request.folder());
                    URL presignedUrl = s3Service.generatePresignedUrl(key);
                    String fileUrl = s3Service.generateFileUrl(key);

                    return S3Mapper.toPresignedUrlResponse(presignedUrl, fileUrl, key);
                }
        ).toList();

        return S3Mapper.toPresignedUrlListResponse(list);
    }

//    private void validSupportedExtension(String fileName) {
//        if (!Objects.equals(StringUtils.getFilenameExtension(fileName), "pdf")) {
//            throw new BaseException(ResponseCode.NOT_SUPPORTED_EXTENSION);
//        }
//    }
}
