package kr.co.isajjim.infra.google.gcs.application.usecase;

import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlListResponse;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlRequest;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlResponse;
import kr.co.isajjim.infra.google.gcs.application.mapper.GcsMapper;
import kr.co.isajjim.infra.google.gcs.domain.service.GcsService;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GcsUseCase {

    private final GcsService gcsService;

    public PresignedUrlListResponse generatePresignedUrl(
            PresignedUrlRequest request
    ) {
        List<PresignedUrlResponse> list = request.fileNames().stream().map(
                fileName -> {
                    //todo AI 서버에서 지원하는 확장자 검증
                    //validSupportedExtension(fileName);

                    String key = gcsService.createKey(fileName);
                    URL presignedUrl = gcsService.generatePresignedUrl(key);
                    String fileUrl = gcsService.generateFileUrl(key);

                    return GcsMapper.toPresignedUrlResponse(presignedUrl, fileUrl, key);
                }
        ).toList();

        return GcsMapper.toPresignedUrlListResponse(list);
    }

//    private void validSupportedExtension(String fileName) {
//        if (!Objects.equals(StringUtils.getFilenameExtension(fileName), "pdf")) {
//            throw new BaseException(ResponseCode.NOT_SUPPORTED_EXTENSION);
//        }
//    }
}
