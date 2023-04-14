package util.hrdnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HrdNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrdNotificationApplication.class, args);
    }

}
