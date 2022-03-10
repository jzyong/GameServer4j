cd ..
cd ..
cd ..
cd mmo-cluster

rem call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-cluster:releases
docker stop mmo-cluster
docker rm mmo-cluster
docker run -p 10000:10000 -p 10001:10001 --name mmo-cluster  mmo-cluster:releases