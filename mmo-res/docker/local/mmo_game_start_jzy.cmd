cd ..
cd ..
cd ..
cd mmo-game

call mvn clean package -Pjzy -DskipTests

call DockerBuild.bat


rem docker pull mmo-game:releases
docker stop mmo-game
docker rm mmo-game
docker run --name mmo-game mmo-game:releases