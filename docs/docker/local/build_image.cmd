echo off

echo "step 1 ====> please check java home is right, must jdk14"
set JAVA_HOME=D:\Program Files\Java\jdk-14.0.1
echo %JAVA_HOME%

echo "step 2 ====> maven build"
cd ../../../
call mvn clean package -Pdefault -DskipTests

echo "step 3 ====> build image"
echo "---------------<api image>--------------"
cd game-api
call DockerBuild.bat
echo ""

echo "---------------<gate image>--------------"
cd ../game-gate
call DockerBuild.bat
echo ""

echo "---------------<hall image>--------------"
cd ../game-hall
call DockerBuild.bat
echo ""

echo "---------------<manage image>--------------"
cd ../game-manage
call DockerBuild.bat
echo ""


cd ../