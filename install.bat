@ECHO OFF
TITLE Installing Libraries 

color 2

rem OPTIONS
SET /a end_pause= 0
SET /a cleanup= 1

SET /a install_lejos= 1
SET /a install_blueTooth= 1
SET /a install_jbullet= 0
SET /a install_jbox2d= 1
SET /a install_mockito= 1
SET /a install_joptsimple= 1
SET /a install_reflections= 1

rem UTIL
SET /a errors= 0

SET tempBin=tempBin34956109345670
SET lib=lib

rem Create Tempory folder.
if not exist %tempBin% (
echo Creating tempory folder.
MKDIR %tempBin%
) else (
echo Recreating tempory folder.
RMDIR /S /Q %tempBin%
MKDIR %tempBin%
)

rem CLEANUP
if %cleanup%==1 (
echo Cleaning.
echo Deleting directory %lib%
RMDIR /S /Q %lib%
MKDIR %lib%
)

rem INSTALL LEJOS
if %install_lejos%==1 (
echo Installing Lejos.

wget -P %tempBin% http://downloads.sourceforge.net/project/lejos/lejos-NXJ/0.8.5beta/lejos_NXJ_0_8_5beta.tar.gz

7za x -o%tempBin% -r -y %tempBin%\lejos_NXJ_0_8_5beta.tar.gz
7za x -o%lib%\ -r -y %tempBin%\lejos_NXJ_0_8_5beta.tar
)

rem INSTALL JBULLET
if %install_jbullet%==1 (
echo Installing JBullet.

wget -P %tempBin% http://jbullet.advel.cz/download/jbullet-20101010.zip

7za x -o%lib%\jbullet -r -y %tempBin%\jbullet-20101010.zip
)

rem INSTALL JBOX2D
if %install_jbox2d%==1 (
echo Installing JBox2d.

wget -P %tempBin% http://jbox2d.googlecode.com/files/jbox2d-2.1.2.1.zip

7za x -o%tempBin% -r -y %tempBin%\jbox2d-2.1.2.1.zip

copy %tempBin%\jbox2d-library\target\jbox2d-library-2.1.2.1-SNAPSHOT-jar-with-dependencies.jar %lib%
copy %tempBin%\jbox2d-testbed\target\jbox2d-testbed-2.1.2.1-SNAPSHOT-jar-with-dependencies.jar %lib%

echo Downloading slf4j.

wget -P %tempBin% http://www.slf4j.org/dist/slf4j-1.6.4.tar.gz

7za x -o%tempBin% -r -y %tempBin%\slf4j-1.6.4.tar.gz
7za x -o%lib%\ -r -y %tempBin%\slf4j-1.6.4.tar

)

rem INSTALL BLUETOOTH
if %install_bluetooth%==1 (
echo Installing Bluetooth.

wget -P %tempBin% http://dl.dropbox.com/u/46248986/libbluetooth.tar.gz

rem | NOTE: Saulius:
rem | "Currently this is my dropbox acc public folder -- do not want to burden the repo with binaries
rem | TODO: Put this in the repo when publishing
rem | 2013 student, if we forget this, find a way to compile libluetooth yourself
rem | that is what is being downloaded"

7za x -o%tempBin% -r -y %tempBin%\libbluetooth.tar.gz
7za x -o%lib%\ -r -y %tempBin%\libbluetooth.tar
)

rem INSTALL_MOCKITO
if %install_mockito%==1 (
echo Installing Mockito

wget -P %lib% http://mockito.googlecode.com/files/mockito-all-1.9.0.jar
)

rem INSTALL_JOPTSIMPLE
if %install_joptsimple%==1 (
echo Installing JoptSimple

wget -U "SomeUserAgent/1.0" -P %lib% http://repo1.maven.org/maven2/net/sf/jopt-simple/jopt-simple/4.3/jopt-simple-4.3.jar

)

rem INSTALL_REFLECTIONS
if %install_reflections%==1 (
echo Installing Reflections

wget -P %tempBin% "http://reflections.googlecode.com/files/reflections-0.9.5.one-jar.jar"
rem cd %tempBin%
rem jar xf reflections-0.9.5.one-j
rem cd ..
copy %tempBin%\reflections-0.9.5.one-jar.jar %lib%
)

rem Delete tempory folder.
rem RMDIR /S /Q %tempBin%

cd %lib%
dir

if %end_pause%==1 (
echo Press any key to exit.
pause>nul
)