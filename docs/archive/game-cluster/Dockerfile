FROM openjdk:14-jdk-alpine

COPY target/lib /usr/src/mmo-cluster/lib
COPY target/mmo-cluster-releases.jar /usr/src/mmo-cluster/mmo-cluster-releases.jar
COPY mmo-cluster-scripts/src /usr/src/mmo-cluster-scripts/src

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/mmo-cluster/lib

WORKDIR /usr/src/mmo-cluster

CMD  java -jar mmo-cluster-releases.jar
