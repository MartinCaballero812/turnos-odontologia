package ar.edu.utn.odonclick;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OdonclickApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdonclickApplication.class, args);
    }

    @Bean
    Clock reloj() {
        return Clock.systemUTC();
    }
}
