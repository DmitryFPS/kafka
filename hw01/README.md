Первый урок по Kafka

1) Запуск Zookeeper
   sudo bin/zookeeper-server-start.sh -daemon config/zookeeper.properties

2) Проверка работы Zookeeper
   ps -ef | grep zoo

3) Запуск Брокер Kafka
   sudo bin/kafka-server-start.sh -daemon config/server.properties

4) Просмотр какие есть топики
   sudo bin/kafka-topics.sh --list --bootstrap-server localhost:9092

5) Просмотр описания о теме (топик)
   sudo bin/kafka-topics.sh --describe --bootstrap-server localhost:9092

6) Создать топик
   sudo bin/kafka-topics.sh --create --topic имя_топика --bootstrap-server localhost:9092

7) Удалить топик
   sudo bin/kafka-topics.sh --delete --topic имя_топика --bootstrap-server localhost:9092

8) Записать сообщение в топик
   sudo bin/kafka-console-producer.sh --topic имя_топика --bootstrap-server localhost:9092
   По окончанию записи нажать сочетание клавиш Ctrl+D (Потому что продюсер будет запущен и ждать в себя сообщения и что бы его остановить нужно нажать это сочетание). Пока продюсер запущен он в реальном времени ожидает и отправляет сообщения.

9) Чтение консюмером сообщений в реальном времени
   sudo bin/kafka-console-consumer.sh --topic имя_топика --bootstrap-server localhost:9092
   Сообщения будут приходить в реальном времени и что бы остановить консюмер жмем Ctrl+D
   !!! По дефолту он читает с последнего сообщения и на экране будет пусто если он только запущен и сообщений новых не было.
   Что бы читал с начала то нужен параметр --from--beginning

10) Для продюсера можно сделать что бы сообщения были с ключами и в качестве разделителя будем использовать например запятую (,)
    sudo bin/kafka-console-producer.sh --topic имя_топика --bootstrap-server localhost:9092 --property parse.key=true --property key.separator=,
    Пример записи >1,one

11) Вызов хелпа
    sudo bin/kafka-console-producer.sh --- Просто дергаем скрипт без параметров и он выдаст подсказки

12) Чтение сообщений по ключу и значению!
    sudo bin/kafka-console-consumer.sh --topic имя_топика --bootstrap-server localhost:9092 --from--beginning

- По умолчанию выводит только value - значения
- Нужно добавить --property print.key=true (В этом случае будет выводить с ключами)
- Можно еще узнать какие у сообщений offset (номер)
  --property print.offset=true
- Можно посмотреть в какой партиции сообщение --property print.partition=true

Основные операции
• zookeeper-server-start.sh – запуск Zookeeper
• zookeeper-server-stop.sh – останов Zookeeper
• kafka-server-start.sh – запуск Kafka брокера
• kafka-server-stop.sh – останов Kafka брокера
• kafka-console-producer.sh – консольный Producer
• kafka-console-consumer.sh – консольный Consumer
• kafka-topics.sh – работа с темами
• kafka-consumer-groups.sh — работа с группами потребителей
