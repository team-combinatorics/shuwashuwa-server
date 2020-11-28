FROM openjdk:11.0.9.1-jre-buster

# image layer
WORKDIR /app

EXPOSE 8848

# Wait until mysql accepts connection 
COPY ./docker-run/wait-for-it.sh /app
RUN chmod +x /app/wait-for-it.sh

CMD ["java","-jar","/app/target.jar"]