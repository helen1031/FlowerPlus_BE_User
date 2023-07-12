FROM adoptopenjdk:11-jdk-hotspot
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","/app.jar"]
#ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app.jar"]