package ru.config;

import org.apache.kafka.streams.StreamsConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class StreamsConfigs {
    public static final String HOST = "localhost:9091,localhost:9092,localhost:9093";

    public static StreamsConfig createStreamsConfig(final String id) {
        return createStreamsConfig(builder -> builder.put(StreamsConfig.APPLICATION_ID_CONFIG, id));
    }

    private static final Map<String, Object> streamsConfig = Map.of(
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, HOST,
            StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);

    private static StreamsConfig createStreamsConfig(final Consumer<Map<String, Object>> builder) {
        final Map<String, Object> map = new HashMap<>(streamsConfig);
        builder.accept(map);
        return new StreamsConfig(map);
    }
}
