FROM openjdk:8-jre
WORKDIR /opt
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]