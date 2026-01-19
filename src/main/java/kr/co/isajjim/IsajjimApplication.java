package kr.co.isajjim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IsajjimApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsajjimApplication.class, args);
    }

}
