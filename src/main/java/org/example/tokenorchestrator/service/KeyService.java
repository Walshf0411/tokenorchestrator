package org.example.tokenorchestrator.service;

import org.example.tokenorchestrator.dto.Key;
import org.example.tokenorchestrator.events.KeyDeletionEvent;
import org.example.tokenorchestrator.events.KeyUnblockingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class KeyService {
    private static final int UNBLOCKING_TIME_IN_SECONDS = 30;

    private final Map<UUID, Key> keys = new HashMap<>();
    private final Set<UUID> availableKeys = new HashSet<>();

    private final EventBus eventBus;

    @Autowired
    public KeyService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Key createKeys() {
        final Key key = new Key();
        keys.put(key.getId(), key);
        availableKeys.add(key.getId());

        eventBus.publish(new KeyDeletionEvent(key.getId(), key.getExpiresAt(), this));

        return key;
    }

    public Optional<Key> getAvailableKey() {
        final Optional<UUID> keyId = availableKeys.stream().findFirst();

        if (keyId.isEmpty()) {
            return Optional.empty();
        }

        final Optional<Key> key = keyId.map(keys::get);
        blockKey(keyId.get());

        return key;
    }

    public Optional<Key> getKey(UUID key) {
        return Optional.ofNullable(keys.get(key));
    }

    public Optional<Key> deleteKey(UUID key) {
        final Optional<Key> keyOpt = getKey(key);

        if (keyOpt.isEmpty()) {
            return keyOpt;
        }

        availableKeys.remove(keyOpt.get().getId());
        return Optional.ofNullable(keys.remove(key));
    }

    public Optional<Key> blockKey(UUID key) {
        final Optional<Key> keyOpt = getKey(key);

        if (keyOpt.isEmpty()) {
            return keyOpt;
        }
        keyOpt.get().setBlockedAt(LocalDateTime.now());
        availableKeys.remove(keyOpt.get().getId());
        eventBus.publish(new KeyUnblockingEvent(keyOpt.get().getId(), keyOpt.get().getBlockedAt().plusSeconds(UNBLOCKING_TIME_IN_SECONDS), this));

        return keyOpt;
    }

    public Optional<Key> keepKeyAlive(UUID key) {
        final Optional<Key> keyOpt = getKey(key);

        if (keyOpt.isEmpty()) {
            return keyOpt;
        }
        keyOpt.get().keepAlive();

        return keyOpt;
    }

    public Optional<Key> unblockKey(UUID key) {
        final Optional<Key> keyOpt = getKey(key);

        if (keyOpt.isEmpty()) {
            return Optional.empty();
        }
        keyOpt.get().setBlockedAt(null);
        availableKeys.add(keyOpt.get().getId());

        return keyOpt;
    }
}
