FROM gradle:8.7.0-jdk21 AS build
COPY --chown=gradle:gradle . .
WORKDIR .
RUN gradle build --no-daemon
EXPOSE 8080
CMD ["gradle", "bootRun"]