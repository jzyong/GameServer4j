cd ..
cd ..
cd ..
cd mmo-game

call mvn clean package -Pjzy2 -DskipTests

call DockerBuild.bat


rem docker pull mmo-game:releases
docker stop mmo-game2
docker rm mmo-game2
docker run --name mmo-game2 mmo-game:releases