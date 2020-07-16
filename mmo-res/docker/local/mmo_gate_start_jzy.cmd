cd ..
cd ..
cd ..
cd mmo-gate

rem call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-gate:releases
docker stop mmo-gate
docker rm mmo-gate
docker run --name mmo-gate  mmo-gate:releases