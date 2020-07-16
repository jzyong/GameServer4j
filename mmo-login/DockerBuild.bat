
rem must copy file...
xcopy ..\mmo-login-scripts\src mmo-login-scripts\src\ /s /e
copy /y "%JAVA_HOME%\lib\tools.jar" target\lib\

docker image build -t mmo-login:releases .

rd /s/q mmo-login-scripts


