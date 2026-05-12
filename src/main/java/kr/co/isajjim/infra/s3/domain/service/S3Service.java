package kr.co.isajjim.infra.s3.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import kr.co.isajjim.infra.s3.domain.constant.S3Folder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final long PRESIGNED_URL_EXPIRATION_MINUTES = 2;
    private static final String PATH_DELIMITER = "/";
    private static final String FILE_URL_FORMAT = "https://%s.s3.%s.amazonaws.com/%s";
    private static final String S3_HOST_SUFFIX = ".amazonaws.com/";

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${infra.aws.s3.bucket}")
    private String bucketName;

    @Value("${infra.aws.region.static}")
    private String region;

    public URL generatePresignedUrl(
            String key
    ) {
        String contentType = URLConnection.guessContentTypeFromName(key);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return generatePresignedUrlInternal(key, contentType);
    }

    public String createKey(String fileName, S3Folder folder) {
        return String.join(PATH_DELIMITER, folder.getPath(), createUniqueFileName(fileName));
    }

    public String generateFileUrl(String key) {
        return String.format(FILE_URL_FORMAT, bucketName, region, key);
    }

    public void deleteObjectByUrl(String fileUrl) {
        String key = fileUrl.substring(fileUrl.indexOf(S3_HOST_SUFFIX) + S3_HOST_SUFFIX.length());
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    /* HELPER METHOD */
    private String createUniqueFileName(final String originalFileName) {
        return String.format("%s_%s", UUID.randomUUID(), originalFileName);
    }

    private URL generatePresignedUrlInternal(String key, String contentType) {
        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType);

        PutObjectRequest objectRequest = builder.build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(PRESIGNED_URL_EXPIRATION_MINUTES))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);
        return presigned.url();
    }
}
