# SpringBoot Kafka Demo

## General Info

Demo of Kafka producer and consumer with SpringBoot

## TODO List
- Kafka Producer
  - Dockerfile
  - Add container of producer to docker-compose.yml
  - Configure by profile which producer will be used (sync/async)
  - Configure by profile which config params are used to create the kafka producer
  - Event Serializer (from spring.kafka.support or custom)
- Kafka Consumer
  - Add basic kafka consumer
  - Add batch kafka consumer
  - Docker file
  - Add container of consumers to docker-compose.yml
- docker-compose file drafts for other configurations of zookeepers and brokers