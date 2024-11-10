package ru.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.utils.Utils;

import java.util.concurrent.ExecutionException;

public class ProducerImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerImpl.class);

    /**
     * Создать продюсер, отправить сообщения в транзакции
     */
    public void createProducer(final String firstMessage,
                               final String secondMessage,
                               final boolean isCommit,
                               final int count) {
        try (final KafkaProducer<String, String> producer = new KafkaProducer<>(Utils.createProducerConfig(config -> {
            config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction-1");
        }))) {
            producer.initTransactions();
            LOGGER.info("Инициализация транзакции: {}", producer);

            producer.beginTransaction();
            LOGGER.info("Старт транзакции: {}", producer);

            for (int i = 1; i <= count; i++) {
                final String textForFirstTopic = firstMessage + "-" + i;
                producer.send(new ProducerRecord<>("topic1", Utils.getUUID(), textForFirstTopic)).get();
                logging(textForFirstTopic, "topic1");

                final String textForSecondTopic = secondMessage + "-" + i;
                producer.send(new ProducerRecord<>("topic2", Utils.getUUID(), textForSecondTopic)).get();
                logging(textForSecondTopic, "topic2");
            }

            if (isCommit) {
                producer.commitTransaction();
                LOGGER.info("Закомитили транзакцию: {}", producer);
                producer.flush();
                return;
            }

            producer.abortTransaction();
        } catch (final InterruptedException | ExecutionException e) {
            LOGGER.error("Ошибка при отправке сообщений:", e);
        }
    }

    private void logging(final String message, final String topicName) {
        LOGGER.info("Отправили сообщение: {} в топик: {}", message, topicName);
    }
}
