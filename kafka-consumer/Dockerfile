FROM eclipse-temurin:21-jdk-alpine
COPY target/*.jar kafka-consumer.jar
ENTRYPOINT ["java","-jar","/kafka-consumer.jar"]