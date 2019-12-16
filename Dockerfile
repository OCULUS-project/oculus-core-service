FROM openjdk:8-jdk-alpine

COPY build /app/build

WORKDIR /app

ENV spring_profiles_active dev
EXPOSE 80

CMD java -jar build/libs/oculus-core-service-0.1.0.jar
