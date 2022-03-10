cd ..
cd ..
cd ..
cd mmo-cluster

call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat

docker tag mmo-cluster:releases 192.168.0.1:5000/outer-test/mmo-cluster:releases
docker push 192.168.0.1:5000/outer-test/mmo-cluster:releases

