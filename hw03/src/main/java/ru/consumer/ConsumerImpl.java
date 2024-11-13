package ru.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.utils.Utils;

import java.time.Duration;
import java.util.List;

public class ConsumerImpl {

    /**
     * Создать консюмер, прочитать сообщения в транзакции
     */
    public void createConsumer(String... topics) {
        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(Utils.createConsumerConfig(config -> {
            config.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-1");
            config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        }))) {
            consumer.subscribe(List.of(topics));

            while (!Thread.interrupted()) {
                final ConsumerRecords<String, String> read = consumer.poll(Duration.ofSeconds(3));
                for (final ConsumerRecord<String, String> record : read) {
                    Utils.processOne(record);
                }
            }
        }
    }
}
