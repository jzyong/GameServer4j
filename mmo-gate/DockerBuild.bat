
rem must copy file...
xcopy ..\mmo-gate-scripts\src mmo-gate-scripts\src\ /s /e
copy /y "%JAVA_HOME%\lib\tools.jar" target\lib\

docker image build -t mmo-gate:releases .

rd /s/q mmo-gate-scripts


