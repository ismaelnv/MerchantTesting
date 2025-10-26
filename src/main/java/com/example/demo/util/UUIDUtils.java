package com.example.demo.util;
import java.util.Optional;
import java.util.UUID;

public final class UUIDUtils {

    public static boolean isValidUUID(String uuid) {
        return Optional.ofNullable(uuid)
                .filter(u -> !u.isEmpty())
                .map(u -> {
                    try {
                        UUID.fromString(u);
                        return true;
                    } catch (IllegalArgumentException e){
                        return false;
                    }
                })
                .orElse(false);
    }
}
