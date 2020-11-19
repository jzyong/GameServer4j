cd ..
cd ..
cd ..
cd mmo-manage

call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-gate:releases
docker stop mmo-manage
docker rm mmo-manage
docker run --name mmo-manage -p 7020:7020  mmo-manage:releases