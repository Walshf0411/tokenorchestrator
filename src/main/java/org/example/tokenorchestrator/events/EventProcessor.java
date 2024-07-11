package org.example.tokenorchestrator.events;

import org.example.tokenorchestrator.service.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessor.class.getSimpleName());
    private final EventBus eventBus;

    @Autowired
    public EventProcessor(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void start() throws InterruptedException {
        logger.info("Starting event processor");
        while(true) {
            processEvent(eventBus.take());
            Thread.sleep(500);
        }
    }

    private void processEvent(final Event<?> event) {
        if (LocalDateTime.now().isAfter(event.getProcessAt())) {
            logger.info("Processing event: " + event.getClass().getSimpleName());
            event.execute();
        } else {
            eventBus.publish(event);
        }
    }
}
