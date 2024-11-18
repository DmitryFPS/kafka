package ru;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.config.StreamsConfigs;
import ru.utils.Utils;

import java.time.Duration;

import static ru.utils.Utils.toLocalTime;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Utils.createTopics(3, 3, "events");

        final Serde<Long> longSerde = Serdes.Long();
        final Serde<String> stringSerde = Serdes.String();
        final StreamsBuilder builder = new StreamsBuilder();

        final KTable<Windowed<String>, Long> result = builder
                .stream("events", Consumed.with(stringSerde, stringSerde))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
                .count();
//                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));

        result.toStream()
                .foreach((key, value) -> LOGGER.info("Окно: [{} - {}]. Ключ:{}, Количество: {}",
                        toLocalTime(key.window().start()), toLocalTime(key.window().end()), key.key(), value));

        final KStream<String, Long> toTopic = result.toStream()
                .map((key, value) -> {
                    final String integerKey = key.key();
                    return KeyValue.pair(integerKey, value);
                });
        toTopic.to("count-topic", Produced.with(stringSerde, longSerde));

        final Topology topology = builder.build();
        LOGGER.warn("{}", topology.describe());

        try (final KafkaStreams kafkaStreams = new KafkaStreams(topology, StreamsConfigs.createStreamsConfig("stream"))) {
            LOGGER.info("Старт приложения");
            kafkaStreams.start();

            while (!Thread.interrupted()) {
                Thread.sleep(1_000L);
            }
        } catch (final InterruptedException exp) {
            LOGGER.error("Ошибка потока: ", exp);
        }
    }
}
