1) Настроить docker-compose.yml
   Во вложении /hw02/sasl-plain/Settings/docker-compose.yml

2) Настроить kafka_server_jaas.conf
   Во вложении /hw02/sasl-plain/Settings/kafka_server_jaas.conf

3) Настроить клиентов:
    - admin.properties /hw02/sasl-plain/Settings/users/admin.properties
    - dima.properties /hw02/sasl-plain/Settings/users/dima.properties
    - masha.properties /hw02/sasl-plain/Settings/users/masha.properties
    - olya.properties /hw02/sasl-plain/Settings/users/olya.properties

4) Запустить docker-compose.yml
   docker-compose up -d

5) Создать топик
   docker exec -ti kafka /usr/bin/kafka-topics --create --topic test --bootstrap-server kafka:9092 --command-config
   /tmp/users/admin.properties

6) Проверяем наличие топика
   docker exec -ti kafka /usr/bin/kafka-topics --list --bootstrap-server kafka:9092 --command-config
   /tmp/users/admin.properties

7) Выдаем первому пользователю права на запись в этот топик (Пользователь dima)
   docker exec -ti kafka /usr/bin/kafka-acls --bootstrap-server kafka:9092 --add --allow-principal User:dima
   --operation Write --topic test --command-config /tmp/users/admin.properties

8) Второму пользователю, права на чтение этого топика (Пользователь olya)
   docker exec -ti kafka /usr/bin/kafka-acls --bootstrap-server kafka:9092 --add --allow-principal User:olya
   --operation Read --topic test --command-config /tmp/users/admin.properties

9) А третьему пользователю барабан (Пользователь masha)

10) Выполняем команды пользователями (Получить список топиков, Записать сообщения в топик, Прочитать сообщение из
    топика)

11) dima
    - Получить список топиков
    - docker exec -ti kafka /usr/bin/kafka-topics --list --bootstrap-server kafka:9092 --command-config
      /tmp/users/dima.properties
    - Записать сообщения в топик
    - docker exec -ti kafka /usr/bin/kafka-console-producer --topic test --bootstrap-server kafka:9092
      --producer.config /tmp/users/dima.properties
    - Читаю сообщения из топика
    - docker exec -ti kafka /usr/bin/kafka-console-consumer --from-beginning --topic test -bootstrap-server kafka:9092
      --consumer.config /tmp/users/dima.properties

12) olya
    - Получить список топиков
    - docker exec -ti kafka /usr/bin/kafka-topics --list --bootstrap-server kafka:9092 --command-config
      /tmp/users/olya.properties
    - Записать сообщения в топик
    - docker exec -ti kafka /usr/bin/kafka-console-producer --topic test --bootstrap-server kafka:9092
      --producer.config /tmp/users/olya.properties
    - Читаю сообщения из топика
    - docker exec -ti kafka /usr/bin/kafka-console-consumer --from-beginning --topic test -bootstrap-server kafka:9092
      --consumer.config /tmp/users/olya.properties

13) masha
    - Получить список топиков
    - docker exec -ti kafka /usr/bin/kafka-topics --list --bootstrap-server kafka:9092 --command-config
      /tmp/users/masha.properties
    - Записать сообщения в топик
    - docker exec -ti kafka /usr/bin/kafka-console-producer --topic test --bootstrap-server kafka:9092
      --producer.config /tmp/users/masha.properties
    - Читаю сообщения из топика
    - docker exec -ti kafka /usr/bin/kafka-console-consumer --from-beginning --topic test -bootstrap-server kafka:9092
      --consumer.config /tmp/users/masha.properties

14) Останавливаю брокер
    docker-compose stop

15) Результат Фото-1
    /hw02/sasl-plain/img/Фото-1.png
