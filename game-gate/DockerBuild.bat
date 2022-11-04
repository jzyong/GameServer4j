
rem must copy file...
xcopy ..\game-gate-scripts\src game-gate-scripts\src\ /s /e

docker image build -t game-gate:releases .

rd /s/q game-gate-scripts


