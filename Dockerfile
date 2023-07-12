FROM gradle:jdk19 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle distTar --no-daemon

FROM eclipse-temurin:20-jre-alpine

RUN mkdir /app
WORKDIR "/app"

COPY --from=build /home/gradle/src/build/distributions/*.tar ./app.tar
RUN tar --strip-components=1 -xf app.tar && rm app.tar
COPY *config.toml ./

ENTRYPOINT ["/bin/sh", "./bin/pal-machine"]