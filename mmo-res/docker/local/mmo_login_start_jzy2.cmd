cd ..
cd ..
cd ..
cd mmo-login

call mvn clean package -Pjzy2 -DskipTests

call DockerBuild.bat


rem docker pull mmo-login:releases
docker stop mmo-login2
docker rm mmo-login2
docker run --name mmo-login2  mmo-login:releases