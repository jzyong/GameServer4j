cd ..
cd ..
cd ..
cd mmo-gate

call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-gate:releases
docker stop mmo-gate2
docker rm mmo-gate2
docker run --name mmo-gate2  mmo-gate:releases