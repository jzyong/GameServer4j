cd ..
cd ..
cd ..
cd mmo-gate

call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-gate:releases
docker stop mmo-gate
docker rm mmo-gate
docker run --name mmo-gate -p 7010:7010 -p 7011:7011  mmo-gate:releases