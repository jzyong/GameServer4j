cd ..
cd ..
cd ..
cd mmo-login

call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-login:releases
docker stop mmo-login
docker rm mmo-login
docker run --name mmo-login  mmo-login:releases