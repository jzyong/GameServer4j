FROM openjdk:14-jdk-alpine

COPY target/lib /usr/src/mmo-manage/lib
COPY target/mmo-manage-releases.jar /usr/src/mmo-manage/mmo-manage-releases.jar

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/mmo-manage/lib

WORKDIR /usr/src/mmo-manage

CMD  java -jar mmo-manage-releases.jar
