package it.pipitone.matteo.spike.rabbitmq;

import it.pipitone.matteo.spike.rabbitmq.messages.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class AppStartupRunner implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AppStartupRunner.class);
    private final SimpleProducer simpleProducer;

    public AppStartupRunner(SimpleProducer simpleProducer) {
        this.simpleProducer = simpleProducer;
    }

    @Override
    public void run(ApplicationArguments args) {
        simpleProducer.sendGenericMessage();
    }
}
