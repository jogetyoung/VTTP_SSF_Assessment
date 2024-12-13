FROM openjdk:23-jdk-oracle as builder

#LABEL maintainer="jojoyoung"

ARG COMPILED_DIR=/compiledir

WORKDIR ${COMPILED_DIR}

COPY src src
COPY .mvn .mvn
COPY pom.xml .
COPY mvnw .

RUN ./mvnw package -Dmaven.test.skip=true

ENV SERVER_PORT=4269
ENV NOTICEBOARD_DB_HOST=localhost
ENV NOTICEBOARD_DB_USERNAME=""
ENV NOTICEBOARD_DB_PASSWORD=""
ENV NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_DATABASE=0
ENV PUBLISH_SERVER_URL=https://publishing-production-d35a.up.railway.app/

EXPOSE ${SERVER_PORT}

#second stage
FROM openjdk:23-jdk-oracle

ARG WORK_DIR=/app

WORKDIR ${WORK_DIR}

COPY --from=builder /compiledir/target/noticeboard-0.0.1-SNAPSHOT.jar noticeboard-2.0.jar

ENV SERVER_PORT=4269
ENV NOTICEBOARD_DB_HOST=localhost
ENV NOTICEBOARD_DB_USERNAME=""
ENV NOTICEBOARD_DB_PASSWORD=""
ENV NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_DATABASE=0
ENV PUBLISH_SERVER_URL=https://publishing-production-d35a.up.railway.app/

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar noticeboard-2.0.jar

HEALTHCHECK --interval=1m --start-period=2m CMD curl -s -f http://localhost:4269/status || exit 1


#-e SPRING_DATA_REDIS_HOST=junction.proxy.rlwy.net
#-e SPRING_DATA_REDIS_USERNAME=default
#-e SPRING_DATA_REDIS_PASSWORD=HvlqUOwexFtBKSRONAvqXRAeuvCnJxod
#-e SPRING_DATA_REDIS_PORT=12122
#-e SPRING_DATA_REDIS_DATABASE=0
