package org.example.tokenorchestrator.service;

import org.example.tokenorchestrator.events.Event;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class EventBus {
    private final BlockingQueue<Event<?>> eventQueue = new LinkedBlockingDeque<>();

    public void publish(Event<?> event) {
        eventQueue.add(event);
    }

    public Event<?> take() throws InterruptedException {
        return eventQueue.take();
    }
}
