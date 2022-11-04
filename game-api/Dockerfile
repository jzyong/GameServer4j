FROM openjdk:14-jdk

COPY target/lib /usr/src/game-api/lib
COPY target/game-api-releases.jar /usr/src/game-api/game-api-releases.jar
COPY game-api-scripts/src /usr/src/game-api-scripts/src

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/game-api/lib

WORKDIR /usr/src/game-api

CMD  java -jar game-api-releases.jar
