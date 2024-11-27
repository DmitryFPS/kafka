Результат выполнения в директории img/result.png

1) Сначала отправлял сообщения в рамках первого 5 минутного окна и получил результат
данные с одинаковыми ключами посчитанны
2) Потом в рамках следующего 5 минутного окна отправлял следующие данные и по ним посчитанны 
заного в рамках нового 5 минутного окна результат

3) Как отправлял docker exec -ti kafka1 kafka-console-producer --topic events --bootstrap-server kafka1:9191 --property parse.key=true --property key.separator=,

4) Данные отправленные в первое 5 минутное окно
   key-1,value1
   key-1,value2
   key-3,value3
   key-2,value4
   key-3,value5
   key-1,value6
   key-4,value7
   key-1,value8
   key-4,value9
   key-2,value10
   key-3,value11
   key-5,value12
   key-3,value13
   key-5,value14
   key-2,value15
5) Данные отправленные во второе 5 минутное окно
   key-5,value16
   key-4,value17
   key-5,value18
   key-7,value19
   key-4,value20
   key-3,value21
   key-7,value22
   key-2,value23
   key-7,value24
   key-7,value25