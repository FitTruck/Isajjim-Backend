package kr.co.isajjim.infra.google.gcs.domain.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GcsService {

    private final Storage storage;

    @Value("${infra.google.gcs.bucket}")
    private String bucketName;

    public URL generatePresignedUrl(String key) {
        String contentType = URLConnection.guessContentTypeFromName(key);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, key).build();

        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", contentType);

        return storage.signUrl(
                blobInfo,
                15,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                Storage.SignUrlOption.withV4Signature()
        );
    }

    public String createKey(String fileName) {
        return createUniqueFileName(fileName);
    }

    public String generateFileUrl(String key) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, key);
    }

    /* HELPER METHOD */
    private String createUniqueFileName(String originalFileName) {
        return String.format("%s_%s", UUID.randomUUID(), originalFileName);
    }
}
