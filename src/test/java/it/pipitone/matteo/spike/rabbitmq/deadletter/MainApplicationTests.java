package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static it.pipitone.matteo.spike.rabbitmq.configuration.MessagingConfiguration.MESSAGE_EXCHANGE;
import static java.lang.Thread.sleep;
import static org.mockito.Mockito.atLeastOnce;

@SpringBootTest
@Import(TestConfiguration.class)
@ActiveProfiles("test")
class MainApplicationTests {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    DeadLetterConsumer deadLetterConsumer;

    @Test
    void contextLoads() {
    }

    @Test
    void verifyDeadLetterIsCalled() {
        amqpTemplate.convertAndSend(MESSAGE_EXCHANGE, "simple.message","messageTest");
        Mockito.verify(deadLetterConsumer, atLeastOnce()).resendMessage(Mockito.any());
    }
}
