package it.pipitone.matteo.spike.rabbitmq.configuration;

import it.pipitone.matteo.spike.rabbitmq.AppStartupRunner;
import it.pipitone.matteo.spike.rabbitmq.messages.SimpleProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    @Bean
    SimpleProducer simpleProducer(AmqpTemplate amqpTemplate){
        return new SimpleProducer(amqpTemplate);
    }

    @Bean
    AppStartupRunner appStartupRunner(SimpleProducer simpleProducer){
        return new AppStartupRunner(simpleProducer);
    }
}
