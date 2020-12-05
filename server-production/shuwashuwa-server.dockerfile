FROM openjdk:11.0.9.1-jre-buster

# image layer
WORKDIR /app

COPY ./server-production/wait-for-it.sh /app/wait-for-it.sh
COPY ./server-production/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/wait-for-it.sh /app/entrypoint.sh

# Final conf
EXPOSE 8848
ENTRYPOINT [ "bash", "/app/entrypoint.sh" ] 