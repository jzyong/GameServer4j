
rem must copy file...
xcopy ..\mmo-cluster-scripts\src mmo-cluster-scripts\src\ /s /e
copy /y "%JAVA_HOME%\lib\tools.jar" target\lib\

docker image build -t mmo-cluster:releases .

rd /s/q mmo-cluster-scripts


