
rem must copy file...
xcopy ..\mmo-game-scripts\src mmo-game-scripts\src\ /s /e
copy /y "%JAVA_HOME%\lib\tools.jar" target\lib\

docker image build -t mmo-game:releases .

rd /s/q mmo-game-scripts


