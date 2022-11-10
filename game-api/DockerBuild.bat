
rem must copy file...
xcopy ..\game-api-scripts\src game-api-scripts\src\ /s /e

docker image build -t game-api:releases .

rd /s/q game-api-scripts


