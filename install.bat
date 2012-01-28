@ECHO OFF
TITLE Installing Libraries 

color 2

rem OPTIONS
SET /a cleanup= 1

SET /a install_lejos= 1
SET /a install_blueTooth= 1
SET /a install_jbullet= 0
SET /a install_jbox2d= 1

rem UTIL
SET /a errors= 0

SET tempBin=tempBin34956109345670
SET lib=lib

SET src_lejos=http://downloads.sourceforge.net/project/lejos/lejos-NXJ/0.8.5beta/lejos_NXJ_0_8_5beta.tar.gz
SET src_jbullet=http://jbullet.advel.cz/download/jbullet-20101010.zip
SET src_jbox2d=http://jbox2d.googlecode.com/files/jbox2d-2.1.2.1.zip
SET src_bluetooth=http://dl.dropbox.com/u/46248986/libbluetooth.tar.gz
SET src_slf4j=http://www.slf4j.org/dist/slf4j-1.6.4.tar.gz

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
wget -P %tempBin% %src_lejos%
7za x -o%tempBin% -r -y %tempBin%\lejos_NXJ_0_8_5beta.tar.gz
7za x -o%lib%\ -r -y %tempBin%\lejos_NXJ_0_8_5beta.tar
)

rem INSTALL JBULLET
if %install_jbullet%==1 (
echo Installing JBullet.
wget -P %tempBin% %src_jbullet%
7za x -o%lib%\jbullet -r -y %tempBin%\jbullet-20101010.zip
)

rem INSTALL JBOX2D
if %install_jbox2d%==1 (
echo Installing JBox2d.
wget -P %tempBin% %src_jbox2d%
7za x -o%lib%\ -r -y %tempBin%\jbox2d-2.1.2.1.zip

echo Downloading slf4j.
wget -P %tempBin% %src_slf4j%
7za x -o%tempBin% -r -y %tempBin%\slf4j-1.6.4.tar.gz
7za x -o%lib%\ -r -y %tempBin%\slf4j-1.6.4.tar

)

rem INSTALL BLUETOOTH
if %install_bluetooth%==1 (
echo Installing Bluetooth.
wget -P %tempBin% %src_bluetooth%

rem | NOTE: Saulius:
rem | "Currently this is my dropbox acc public folder -- do not want to burden the repo with binaries
rem | TODO: Put this in the repo when publishing
rem | 2013 student, if we forget this, find a way to compile libluetooth yourself
rem | that is what is being downloaded"

7za x -o%tempBin% -r -y %tempBin%\libbluetooth.tar.gz
7za x -o%lib%\ -r -y %tempBin%\libbluetooth.tar
)

rem Delete tempory folder.
RMDIR /S /Q %tempBin%

cd %lib%
dir

echo Press any key to exit.
pause>nul