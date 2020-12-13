package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    @Primary
    DeadLetterConsumer deadLetterConsumerMock(){
        return Mockito.mock(DeadLetterConsumer.class);
    }
}
