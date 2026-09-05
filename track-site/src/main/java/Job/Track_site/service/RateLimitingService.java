package Job.Track_site.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {
    // Thread-safe map storing buckets for each user/client
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::createNewBucket);
    }
    private Bucket createNewBucket(String key) {
        // Rule: Bucket capacity of 5 tokens, refilling 5 tokens every 1 hour
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillIntervally(5, Duration.ofHours(1))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
