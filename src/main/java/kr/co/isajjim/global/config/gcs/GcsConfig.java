package kr.co.isajjim.global.config.gcs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class GcsConfig {

    @Value("${infra.google.project-id}")
    private String projectId;

    @Value("${infra.google.gcs.key-path:}")
    private String keyPath;

    private final ResourceLoader resourceLoader;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials;

        if (StringUtils.hasText(keyPath)) {
            // 로컬 개발환경: key 파일 사용
            Resource resource = resourceLoader.getResource(keyPath);
            InputStream serviceAccount = resource.getInputStream();
            credentials = GoogleCredentials.fromStream(serviceAccount);
        } else {
            // Cloud Run 환경: 서비스 계정 ADC 자동 사용
            credentials = GoogleCredentials.getApplicationDefault();
        }

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }
}
