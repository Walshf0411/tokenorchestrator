package org.example.tokenorchestrator.dto;

import lombok.Data;
import org.example.tokenorchestrator.events.EventParticipant;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Key implements EventParticipant {
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime blockedAt;
    // TODO: make the 5 configurable
    private LocalDateTime expiresAt = createdAt.plusMinutes(1);

    public void keepAlive() {
        expiresAt = LocalDateTime.now().plusMinutes(1);
    }
}
