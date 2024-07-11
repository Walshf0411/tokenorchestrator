package org.example.tokenorchestrator.events;

import org.example.tokenorchestrator.dto.Key;
import org.example.tokenorchestrator.service.KeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class KeyUnblockingEvent extends Event<UUID> {

    private static final Logger logger = LoggerFactory.getLogger(KeyUnblockingEvent.class);

    private final KeyService keyService;

    public KeyUnblockingEvent(UUID eventParticipant, LocalDateTime processAt, KeyService keyService) {
        super(eventParticipant, processAt);
        this.keyService = keyService;
    }

    @Override
    void execute() {
        Optional<Key> key = keyService.getKey(eventParticipant);
        if (key.isEmpty()) {
            return;
        }

        logger.info("Unblocking key: " + eventParticipant);
        keyService.unblockKey(eventParticipant);
    }
}
