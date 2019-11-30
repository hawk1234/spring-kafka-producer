Spring Boot kafka producer example
==========

This repository contains Spring Boot (https://spring.io/projects/spring-boot) 
application example that produces string messages to Kafka. It supports 
customization of messages XML or JSON, by specifying paths, XPath or JsonPath, under 
which randomized values should be generated

Requirements
----------
* Java 1.8 latest update installed
* Access to maven central
* Kafka server to which data will be published

Features
----------
* Basic Thymeleaf UI for sending messages
* Rest API for sending messages
* Messages customization based on XPath or JsonPath

Building application
----------
Target creates packaged zip with executable jar (see pom.xml for exact location of zip package).
To change version edit pom.xml. application.properties; log4j config and kafka-producer.properties 
are provided outside application jar for easy configuration. Application may be also 
configured through environment variables eg. this may be used with docker images. 

```bash
mvnw package
```