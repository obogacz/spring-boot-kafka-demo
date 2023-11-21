# SpringBoot Kafka Demo

## General Info

This demo project is an example of the usage of Apache Kafka. This project has been meticulously crafted to serve as a comprehensive example of Kafka's usage, providing developers with insights into its capabilities and practical applications.

Apache Kafka is an open-source distributed event streaming platform known for its scalability and fault tolerance. It facilitates the real-time collection, processing, and distribution of data across clusters. Kafka is widely used for building high-throughput, low-latency data pipelines and event-driven architectures, employing a publish-subscribe model with features like partitioning and replication for robust data handling.

The primary goal of this demo project is to illustrate key concepts such as:
- Kafka Broker
- Event Producer & Event Consumer
- Single Event processing & Batch Event processing
- Policies of Events processing (at-most-once, at-least-once, exactly-once)

## Technologies
- [Java 21](https://openjdk.org/projects/jdk/21/)
- [SpringBoot 3](https://spring.io/projects/spring-boot)
- [Apache Kafka](https://kafka.apache.org)
- [Infinispan](https://infinispan.org)
- [PostgreSQL](https://www.postgresql.org)
- [Lombok](https://projectlombok.org)

## Setup

Before getting started, ensure you have the following installed:
- Java Development Kit (JDK)
- Docker

To simplify setup, all container definitions are provided in the `docker-compose.yml` file.
- Zookeeper
- Kafka Broker x3
- Event Producer
- Event Consumer x3
- PostgreSQL Database

> [!NOTE]  
> Other examples of the Apache Kafka environment setup are in the `/kafka-configs` directory.

### Running Kafka with Docker
To run only Apache Kafka containers navigate to the project root and run the following command:
```shell
docker compose up -d
```

### Running Kafka with Producer and Consumers with Docker
To run the whole demo with Kafka Containers, Event Producer and Event Consumers navigate to the project root and run the following command:
```shell
docker compose --profile demo up -d
```

### Project Structure
The project is built with Spring Boot and comprises two modules:
- kafka-producer
- kafka-consumer

If you prefer running the project locally, ensure you have the JDK installed. You can then build and run each module using Maven or your preferred IDE.

## Event Producer Configuration

The Event Producer exposes a Swagger UI under http://localhost:8081/swagger-ui/index.html by which you can publish events manually. In addition, it also sends events via a scheduled job which can be configured by the `application.yaml`.

### Processing Policies
The Event Producer in this project can publish events according to three available publishing policies:
- **At-Most-Once** - Guarantees that events are published by the producer at most once but there is a possibility of events being missed in case of failures.
- **At-Least-Once** - Ensures that events are published by the producer at least once, possibly resulting in duplicate publishing in the event of failures.
- **Exactly-Once** - Aims to publish events exactly once, ensuring both no duplicates and no missed events.

### Producer Publishing Modes
The Event Producer allows events to be published in two modes:
- **Synchronous Publishing** - Waits for acknowledgment from the Kafka broker before moving on to the next event.
- **Asynchronous Publishing** - Continues to publish events without waiting for acknowledgment, enhancing throughput at the cost of potential delivery confirmation.

### Configuration via application.yml
All of these publishing properties can be configured in the `application.yml` file of the **kafka-producer** module. Customize the configurations based on your specific requirements, taking advantage of the flexibility offered by the YAML format for clear and concise adjustments. Refer to the provided sample configurations for guidance.
- `kafka.bootstrap-servers` - (String) list of kafka brokers, at least one has to be provided
- `kafka.producer.strategy` - (String) strategy of events publishing: `at-most-once`(default), `at-least-once`, `exactly-once`
- `kafka.producer.publishing` - (String) publishing mode: `async`(default), `sync`
- `kafka.topics.purchases.name` - (String) name of a topic to publish
- `kafka.topics.purchases.partitions` - (Number) number of partitions for a topic
- `kafka.topics.purchases.replicas` - (Number) number of replicas for partitions
- `scheduled.send-event-fix-delay` - (Number) fixed an event send delay in milliseconds

## Event Consumer Configuration

### Processing Policies
The Event Consumer in this project can handle events according to three available processing policies:
- **At-Most-Once** - Guarantees that events are processed by the consumer at most once but there is a possibility of events being missed in case of failures.
- **At-Least-Once** - Ensures that events are processed by the consumer at least once, possibly resulting in duplicate processing in the event of failures.
- **Exactly-Once** - Aims to process events exactly once, ensuring both no duplicates and no missed events, though it requires additional configuration and coordination.

> [!NOTE]  
> **The Exactly Once** strategy was implemented in the usage of Infinispan cache and PostgreSQL. The Consumer stores in the replicated cache data like already processed events and the last processed offset for each topic and partition. In addition, the cache snapshot is stored in the database in case of crushing all consumers to fetch initial data.

### Consumer Processing Modes
The Event Consumer supports two processing modes:
- **Single Mode** - Processes events individually as they are received.
- **Batch Mode** - Aggregates events into batches for more efficient processing.

### Configuration via application.yml
All of these processing properties can be configured in the `application.yml` file of the **kafka-consumer** module. Customize the configurations based on your specific requirements, taking advantage of the flexibility offered by the YAML format for clear and concise adjustments. Refer to the provided sample configurations for guidance.
- `kafka.bootstrap-servers` - (String) list of kafka brokers, at least one has to be provided
- `kafka.consumer.client-id` - (String) identifier of an application
- `kafka.consumer.group-id` - (String) kafka consumer group identifier
- `kafka.consumer.strategy` - (String) strategy of events consuming: `at-most-once`(default), `at-least-once`, `exactly-once`
- `kafka.consumer.type` - (String) events consuming mode: `single`(default), `batch`
- `kafka.topics.purchases.name` (String) name of the topic on which the consumer is listening
