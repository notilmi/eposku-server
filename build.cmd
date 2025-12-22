@echo off
setlocal enabledelayedexpansion

REM Navigate to the directory of this script
cd /d "%~dp0"

REM Usage help
if /I "%~1"=="/h" goto :help
if /I "%~1"=="-h" goto :help
if /I "%~1"=="--help" goto :help

set IMAGE_TAG=%~1
if "%IMAGE_TAG%"=="" set IMAGE_TAG=eposku-server:prod

echo [1/2] Building Java application with Maven wrapper...
.\mvnw.cmd --file .\pom.xml clean package -DskipTests
if errorlevel 1 goto :maven_fail

echo [2/2] Building Docker image: %IMAGE_TAG% ...
docker --version >nul 2>&1
if errorlevel 1 (
  echo ERROR: Docker is not installed or not available in PATH.
  goto :docker_fail
)

docker build -t %IMAGE_TAG% -f Dockerfile .
if errorlevel 1 goto :docker_fail

echo SUCCESS: Build completed. Docker image '%IMAGE_TAG%' is ready.
exit /b 0

:help
echo Usage: %~n0 [image_tag]
echo.
echo   image_tag   Optional. Docker image tag to use. Default is "eposku-server:prod".
echo.
echo Example:
echo   %~n0
echo   %~n0 myrepo/eposku-server:1.0.0
exit /b 0

:maven_fail
echo ERROR: Maven build failed.
exit /b 1

:docker_fail
echo ERROR: Docker build failed.
exit /b 1
