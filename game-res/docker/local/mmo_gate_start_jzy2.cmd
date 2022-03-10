cd ..
cd ..
cd ..
cd mmo-gate

call mvn clean package -Pjzy2 -DskipTests

call DockerBuild.bat


rem docker pull mmo-gate:releases
docker stop mmo-gate2
docker rm mmo-gate2
docker run --name mmo-gate2 -p 7012:7012 -p 7013:7013  mmo-gate:releases