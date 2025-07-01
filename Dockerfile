FROM amazoncorretto:21-alpine-jdk
COPY target/*.jar deal.jar
ENTRYPOINT ["java","-jar","/deal.jar"]