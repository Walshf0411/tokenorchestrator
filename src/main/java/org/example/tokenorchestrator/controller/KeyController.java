package org.example.tokenorchestrator.controller;

import org.apache.coyote.Response;
import org.example.tokenorchestrator.dto.Key;
import org.example.tokenorchestrator.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/keys/")
public class KeyController {

    @Autowired
    private final KeyService keyService;

    public KeyController(final KeyService keyService) {
        this.keyService = keyService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createKeys() {
        keyService.createKeys();
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getKeys() {
        final Optional<Key> keyOpt = keyService.getAvailableKey();

        return keyOpt.map(key -> new ResponseEntity<>(Map.of("keyId", key.getId().toString()), HttpStatusCode.valueOf(200)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getKey(@PathVariable("id") UUID keyId) {
        final Optional<Key> keyOpt = keyService.getKey(keyId);

        if (keyOpt.isPresent()) {
            final Map<String, Object> response = Map.of(
                    "isBlocked", keyOpt.get().getBlockedAt() != null,
                    "createdAt", keyOpt.get().getCreatedAt(),
                    "blockedAt", keyOpt.get().getBlockedAt() == null ? "null" : keyOpt.get().getBlockedAt()
                    );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKey(@PathVariable("id") UUID keyId) {
        final Optional<Key> deletedKey = keyService.deleteKey(keyId);

        return new ResponseEntity<>(HttpStatusCode.valueOf(deletedKey.isPresent() ? 200 : 404));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> blockKey(@PathVariable("id") UUID keyId) {
        final Optional<Key> blockedKey = keyService.blockKey(keyId);

        return new ResponseEntity<>(HttpStatusCode.valueOf(blockedKey.isPresent() ? 200 : 404));
    }

    @PutMapping("/keepalive/{id}")
    public ResponseEntity<Void> keepAlive(@PathVariable("id") UUID keyId) {
        final Optional<Key> blockedKey = keyService.keepKeyAlive(keyId);

        return new ResponseEntity<>(HttpStatusCode.valueOf(blockedKey.isPresent() ? 200 : 404));
    }
}
