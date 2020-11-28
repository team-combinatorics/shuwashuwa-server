# https://stackoverflow.com/questions/52120845/docker-compose-build-with-maven-that-re-uses-the-maven-repository
FROM openjdk:11.0.9.1-jre-buster
FROM maven:3.6.3-openjdk-11

# image layer
WORKDIR /app
ADD ./pom.xml /app
RUN mvn verify clean --fail-never

# Actually $pwd=../

# Image layer: with the application
COPY ./ /app
RUN mvn -v
RUN mvn clean install -DskipTests
EXPOSE 8848

# Wait until mysql accepts connection 
COPY ./docker/wait-for-it.sh /app
RUN chmod +x /app/wait-for-it.sh

CMD ["java","-jar","/app/target/your.jar"]