@echo off
setlocal enabledelayedexpansion

echo ��ʼ����proto����...
echo.

FOR %%p in (*.proto) do (����
	set proto=!proto!%%p 
)

echo %proto%
protoc --java_out C:/new/game-server/game-message/src/main/java ./%proto%


echo.
echo ִ�����...
echo.
PAUSE 