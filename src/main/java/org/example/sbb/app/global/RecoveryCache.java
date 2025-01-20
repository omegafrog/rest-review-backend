package org.example.sbb.app.global;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class RecoveryCache {
    private static final Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private static final RecoveryCache INSTANCE = new RecoveryCache();

    private RecoveryCache(){}

    public static RecoveryCache getCache() {
        return INSTANCE;
    }

    public static void put(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.getIfPresent(key);
    }
}
