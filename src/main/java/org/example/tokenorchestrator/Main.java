package org.example.tokenorchestrator;


import org.example.tokenorchestrator.events.EventProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        startEventProcessor(applicationContext);
    }

    private static void startEventProcessor(ApplicationContext applicationContext) throws InterruptedException {
        EventProcessor eventProcessor = applicationContext.getBean(EventProcessor.class);

        eventProcessor.start();;
    }
}