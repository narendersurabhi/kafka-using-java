# Kafka Using Java

This repository demonstrates how to use Apache Kafka with Java. It includes examples for producing and consuming messages.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Apache Kafka is a distributed streaming platform that is used to build real-time data pipelines and streaming applications. This project provides simple examples to get started with Kafka using Java.

## Prerequisites

- Java 11 or higher
- Apache Kafka
- Gradle
- Docker

## Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/narendersurabhi/kafka-using-java.git
   cd kafka-using-java

2. Install the dependencies:
   ```sh
   ./gradlew build

3. Generate Kafka Cluster UUID:
   ```sh
   kafka-storage.sh random-uuid

4. Start your Kafka server:
   ```sh
   kafka-storage.sh format -t <<cluster_uuid>> -c ~/kafka_2.13-3.1.0/config/kraft/server.properties 
   kafka-server-start.sh ~/kafka_2.13-3.1.0/config/kraft/server.properties

5. To start Opensearch cluster, run below command from project directory.
   ```sh
   docker-compose up

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any changes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
