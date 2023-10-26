FROM openjdk:11.0.9.1-jre-buster

# image layer
WORKDIR /app

COPY ./server-production/wait-for-it.sh ./server-production/entrypoint.sh /app/
RUN chmod +x /app/*.sh

# Final conf
EXPOSE 8848
ENTRYPOINT [ "bash", "/app/entrypoint.sh" ] 