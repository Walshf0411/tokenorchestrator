package org.example.tokenorchestrator.events;

import org.example.tokenorchestrator.dto.Key;
import org.example.tokenorchestrator.service.KeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class KeyDeletionEvent extends Event<UUID> {

    private static final Logger logger = LoggerFactory.getLogger(KeyDeletionEvent.class.getSimpleName());

    private final KeyService keyService;

    public KeyDeletionEvent(final UUID keyID, LocalDateTime processAt, final KeyService keyService) {
        super(keyID, processAt);
        this.keyService = keyService;
    }

    @Override
    public void execute() {
        final Optional<Key> key = keyService.getKey(eventParticipant);
        if (key.isEmpty()) {
            return;
        }

        if (LocalDateTime.now().isAfter(key.get().getExpiresAt())) {
            logger.info("Deleting key: " + key.get().getId());
            keyService.deleteKey(key.get().getId());
        }
    }
}
