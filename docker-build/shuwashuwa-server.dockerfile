# https://stackoverflow.com/questions/52120845/docker-compose-build-with-maven-that-re-uses-the-maven-repository
FROM openjdk:11.0.9.1-jre-buster
FROM maven:3.6.3-openjdk-11

# Aliyun Mirror
COPY ./docker-build/settings.xml /usr/share/maven/ref/

# image layer
WORKDIR /app
ADD ./pom.xml /app
RUN mvn verify clean --fail-never -s /usr/share/maven/ref/settings.xml

# Actually $pwd=../

# Image layer: with the application
COPY ./src /app/src
RUN mvn -v
RUN mvn clean install -DskipTests -s /usr/share/maven/ref/settings.xml

COPY ./docker-build/wait-for-it.sh /app/wait-for-it.sh
COPY ./docker-build/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/wait-for-it.sh /app/entrypoint.sh

# Final conf
EXPOSE 8848
ENTRYPOINT [ "bash", "/app/entrypoint.sh" ] 