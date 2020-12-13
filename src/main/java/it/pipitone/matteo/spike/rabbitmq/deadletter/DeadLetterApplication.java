package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(JacksonAutoConfiguration.class)
public class DeadLetterApplication {

    public static void main(String[] args) {

        SpringApplication.run(DeadLetterApplication.class, args);
    }

}
