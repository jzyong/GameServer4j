FROM openjdk:14-jdk

COPY target/lib /usr/src/game-hall/lib
COPY target/game-hall-releases.jar /usr/src/game-hall/game-hall-releases.jar
COPY game-hall-scripts/src /usr/src/game-hall-scripts/src

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/game-hall/lib

WORKDIR /usr/src/game-hall

CMD  java -jar game-hall-releases.jar
