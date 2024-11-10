package ru;

import ru.consumer.ConsumerImpl;
import ru.producer.ProducerImpl;
import ru.utils.Utils;

public class Main {

    public static void main(String[] args) {
        Utils.createTopics(3, 3, "topic1", "topic2");

        final ProducerImpl producerImpl = new ProducerImpl();
        producerImpl.createProducer("topic1-MessageWithTransactional", "topic2-MessageWithTransactional", true, 5);
        producerImpl.createProducer("topic1-MessageWithoutTransactional", "topic2-MessageWithoutTransactional", false, 2);

        final ConsumerImpl consumerImpl = new ConsumerImpl();
        consumerImpl.createConsumer("topic1", "topic2");
    }
}
