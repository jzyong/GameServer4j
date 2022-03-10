cd ..
cd ..
cd ..
cd mmo-manage

call mvn clean package -Pjzy2 -DskipTests

call DockerBuild.bat


rem docker pull mmo-gate:releases
docker stop mmo-manage2
docker rm mmo-manage2
docker run --name mmo-manage2 -p 7021:7021  mmo-manage:releases