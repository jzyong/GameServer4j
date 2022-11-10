FROM openjdk:14-jdk

COPY target/lib /usr/src/game-manage/lib
COPY target/game-manage-releases.jar /usr/src/game-manage/game-manage-releases.jar

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/game-manage/lib

WORKDIR /usr/src/game-manage

CMD  java -jar game-manage-releases.jar
