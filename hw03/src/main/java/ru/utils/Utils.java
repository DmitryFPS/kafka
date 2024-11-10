package ru.utils;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.config.ConsumerConfigs;
import ru.config.ProducerConfigs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static ru.config.ConnectConfigs.HOST;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Создает нужное количество топиков
     */
    public static void createTopics(final int numPartitions, final int replicationFactor, final String... topics) {
        final Map<String, Object> properties = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, HOST,
                AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000
        );

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
     * Создать продюсер по конфигурации
     */
    public static Map<String, Object> createProducerConfig(Consumer<Map<String, Object>> builder) {
        final Map<String, Object> configs = new HashMap<>(ProducerConfigs.producerConfig());
        builder.accept(configs);
        return configs;
    }

    /**
     * Создать консюмер по конфигурации
     */
    public static Map<String, Object> createConsumerConfig(final Consumer<Map<String, Object>> builder) {
        final Map<String, Object> map = new HashMap<>(ConsumerConfigs.consumerConfig());
        builder.accept(map);
        return map;
    }

    public static void processOne(ConsumerRecord<String, String> record) {
        Utils.LOGGER.info("Читаем: Ключ: - {}, Значение - {}, Offset - {}", record.key(), record.value(), record.offset());
    }

    public static String getUUID() {
        return String.valueOf(UUID.randomUUID());
    }
}
