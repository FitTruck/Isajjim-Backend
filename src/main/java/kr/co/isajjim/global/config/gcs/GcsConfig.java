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

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class GcsConfig {

    @Value("${infra.google.project-id}")
    private String projectId;

    @Value("${infra.google.gcs.key-path}")
    private String keyPath;

    private final ResourceLoader resourceLoader;

    @Bean
    public Storage storage() throws IOException {
        Resource resource = resourceLoader.getResource(keyPath);
        InputStream serviceAccount = resource.getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }
}
