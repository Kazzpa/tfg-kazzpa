FROM openjdk:latest
MAINTAINER es.kazzpa

ADD app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
VOLUME /volume/sel-att
EXPOSE 8082