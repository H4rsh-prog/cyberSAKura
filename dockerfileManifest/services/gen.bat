@echo off
setlocal EnableDelayedExpansion
for /D %%x in (*) do (
set "DIRECTORY="
set "PORT="
set "NAME="
set "IMAGE="
for /F "tokens=* delims=" %%D in (%%x\path) DO set "DIRECTORY=%%D"
for /F "tokens=* delims=" %%P in (%%x\port) DO set "PORT=%%P"
for /F "tokens=* delims=" %%N in (%%x\name) DO set "NAME=%%N"
for /F "tokens=* delims=" %%I in (%%x\image) DO set "IMAGE=%%I"
echo "!DIRECTORY!"
echo "!PORT!"
echo "!NAME!"
echo "!IMAGE!"
(
echo FROM !IMAGE!
echo.
echo WORKDIR /app
echo.
echo COPY . .
echo.
echo RUN ./mvnw clean package -DskipTests
echo.
echo EXPOSE !PORT!
echo.
echo ENTRYPOINT ["java", "-jar", "!NAME!.jar"]
) > "!DIRECTORY!\Dockerfile"
)