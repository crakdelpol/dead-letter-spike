package it.pipitone.matteo.spike.rabbitmq.controller;

import it.pipitone.matteo.spike.rabbitmq.messages.SimpleProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rabbitmq/")
public class RabbitmqWebController {

    @Autowired
    private SimpleProducer simpleProducer;

    @GetMapping(value = "/producer")
    public String producer() {

        simpleProducer.sendGenericMessage();
        return "Message sent to the RabbitMQ Successfully";
    }
}
