FROM openjdk:14-jdk-alpine

COPY target/lib /usr/src/mmo-login/lib
COPY target/mmo-login-releases.jar /usr/src/mmo-login/mmo-login-releases.jar
COPY mmo-login-scripts/src /usr/src/mmo-login-scripts/src

ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/src/mmo-login/lib

WORKDIR /usr/src/mmo-login

CMD  java -jar mmo-login-releases.jar
