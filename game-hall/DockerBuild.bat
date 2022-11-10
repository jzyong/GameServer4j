
rem must copy file...
xcopy ..\game-hall-scripts\src game-hall-scripts\src\ /s /e

docker image build -t game-hall:releases .

rd /s/q game-hall-scripts


