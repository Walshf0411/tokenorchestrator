package org.example.tokenorchestrator.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public abstract class Event<T> {

    protected T eventParticipant;
    protected LocalDateTime processAt = LocalDateTime.now();
    abstract void execute();
}
