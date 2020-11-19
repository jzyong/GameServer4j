FROM openjdk:14-jdk-alpine

COPY target/lib /usr/src/mmo-game/lib
COPY target/mmo-game-releases.jar /usr/src/mmo-game/mmo-game-releases.jar
COPY mmo-game-scripts/src /usr/src/mmo-game-scripts/src

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/mmo-game/lib

WORKDIR /usr/src/mmo-game

CMD  java -jar mmo-game-releases.jar
