package ru.utils;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static ru.config.StreamsConfigs.HOST;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Создает нужное количество топиков
     */
    public static void createTopics(final int numPartitions, final int replicationFactor, final String... topics) {
        final Map<String, Object> properties = Map.of(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);

        try (final Admin client = Admin.create(properties)) {
            final Set<String> existingTopics = client.listTopics().names().get();

            final List<NewTopic> newTopics = new ArrayList<>();
            for (final String topic : topics) {
                if (!existingTopics.contains(topic)) {
                    final NewTopic newTopic = new NewTopic(topic, numPartitions, (short) replicationFactor);
                    newTopics.add(newTopic);
                }
            }

            if (!newTopics.isEmpty()) {
                final CreateTopicsResult createTopics = client.createTopics(newTopics);
                createTopics.all().get();
                LOGGER.debug("Созданы топики: {}", newTopics);
            } else {
                LOGGER.debug("Все указанные топики уже существуют.");
            }
        } catch (final ExecutionException | InterruptedException e) {
            LOGGER.debug("Не удалось создать топики");
            throw new RuntimeException(e);
        }
    }

    /**
     * Указать LocalTime
     */
    public static LocalTime toLocalTime(final Long epochMillis) {
        return Instant.
                ofEpochMilli(epochMillis)
                .atZone(ZoneOffset.systemDefault())
                .toLocalTime();
    }
}
