# Vehicle auction facade

It is a Java web application built upon spring framework to be used as a facade to call the legacy auction API.

## Requirements
- Install Java 8

## Installation

Maven command line

```bash
mvn clean install
```

## Usage

Docker:
```
docker pull egamorim/leilao-veiculos:latest
docker run -p 127.0.0.1:8080:8080/tcp egamorim/leilao-veiculos:latest
```

Java:
```bash
java -jar target/leilao-veiculos-facade-0.0.1-SNAPSHOT.jar
```
The API operations documentation can be found at [http://localhost:8080/auction/swagger-ui.html](http://localhost:8080/auction/swagger-ui.html)
