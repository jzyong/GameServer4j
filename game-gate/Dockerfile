FROM openjdk:14-jdk-alpine

COPY target/lib /usr/src/mmo-gate/lib
COPY target/mmo-gate-releases.jar /usr/src/mmo-gate/mmo-gate-releases.jar
COPY mmo-gate-scripts/src /usr/src/mmo-gate-scripts/src

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/mmo-gate/lib

WORKDIR /usr/src/mmo-gate

CMD  java -jar mmo-gate-releases.jar
