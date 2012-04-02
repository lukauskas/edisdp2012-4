SDP_HOME=`pwd`
LIB="$SDP_HOME/lib"
LD_LIBRARY_DIR="$LIB/lib"
function add_to_bashrc {
    grep -Fxq "$*" ~/.bashrc || echo "$*" >> ~/.bashrc
}
function install_lejos {
    local lLOCATION="`mktemp /tmp/lejos_NXJ_0_8_5beta.XXXXXX.tar.gz`"

    echo "> Downloading lejos to $lLOCATION"

    wget -O "$lLOCATION" "http://downloads.sourceforge.net/project/lejos/lejos-NXJ/0.8.5beta/lejos_NXJ_0_8_5beta.tar.gz?r=&ts=1327495476&use_mirror=kent" \
    && echo "> extracting $lLOCATION" \
    && cd $LIB \
    && tar xvf $lLOCATION \
    && chmod +x lejos_nxj/bin/* \
    && rm $lLOCATION \
    && STR="export PATH=\$PATH:$LIB/lejos_nxj/bin" \
    && add_to_bashrc $STR
}

function install_jbullet {
    local lLOCATION="`mktemp /tmp/jbullet.XXXXXX.zip`"
    local lEXTRACT_DIR="`mktemp -d /tmp/jbullet_extracted.XXXXXX`"

    echo "> Downloading jbullet to $lLOCATION"

    wget -O "$lLOCATION" "http://jbullet.advel.cz/download/jbullet-20101010.zip" \
    && echo "> extracting $lLOCATION" \
    && cd $lEXTRACT_DIR \
    && unzip $lLOCATION \
    && cp -R $lEXTRACT_DIR/jbullet-20101010/lib/* $LIB \
    && rm $lLOCATION \
    && rm -rf $lEXTRACT_DIR
}

function install_jbox2d {
    local lLOCATION="`mktemp /tmp/jbox2d.XXXXXX.zip`"
    local lEXTRACT_DIR="`mktemp -d /tmp/jbox2d_extracted.XXXXXX`"

    echo "> Downloading jbox2d to $lLOCATION"

    wget -O "$lLOCATION" "http://jbox2d.googlecode.com/files/jbox2d-2.1.2.1.zip" \
    && echo "> extracting $lLOCATION" \
    && cd $lEXTRACT_DIR \
    && unzip $lLOCATION \
    && cp $lEXTRACT_DIR/jbox2d-library/target/jbox2d-library-2.1.2.1-SNAPSHOT-jar-with-dependencies.jar $LIB \
    && cp $lEXTRACT_DIR/jbox2d-testbed/target/jbox2d-testbed-2.1.2.1-SNAPSHOT-jar-with-dependencies.jar $LIB \
    && rm $lLOCATION \
    && rm -rf $lEXTRACT_DIR
}

function install_slf4j {
   local lLOCATION="`mktemp /tmp/slf4j.XXXXXXX.tar.gz`"
   local lEXTRACT_DIR="`mktemp -d /tmp/slf4j_extracted.XXXXXX`"

   wget -O "$lLOCATION" "http://www.slf4j.org/dist/slf4j-1.6.4.tar.gz" \
   && echo "> extractiong $lLOCATION" \
   && cd $lEXTRACT_DIR \
   && tar xvf $lLOCATION \
   && cp "$lEXTRACT_DIR/slf4j-1.6.4/slf4j-api-1.6.4.jar" $LIB \
   && rm $lLOCATION \
   && rm -rf $lEXTRACT_DIR
}

function cleanup {
    rm -rf $LIB 
    mkdir -p $LIB
    mkdir -p $LD_LIBRARY_DIR
    STR="export LD_LIBRARY_PATH=\$LD_LIBRARY_PATH:$LD_LIBRARY_DIR"
    add_to_bashrc $STR
}
function install_bluetooth {
    local lTMP_BLUETOOTH_LIB="`mktemp /tmp/libbluetooth.XXXXX.tar.gz`"
    # Currently this is my dropbox acc public folder -- do not want to burden the repo with binaries -- Saulius  
    # TODO: Put this in the repo when publishing
    # 2013 student, if we forget this, find a way to compile libluetooth yourself
    # that is what is being downloaded
    wget "http://dl.dropbox.com/u/46248986/libbluetooth.tar.gz" -O $lTMP_BLUETOOTH_LIB \
    && cd $LIB && tar xvf $lTMP_BLUETOOTH_LIB && cd $SDP_HOME && rm $lTMP_BLUETOOTH_LIB
}

function install_mockito {
    wget "http://mockito.googlecode.com/files/mockito-all-1.9.0.jar" -O "$LIB/mockito-all-1.9.0.jar"
}

function install_joptsimple {
    wget -U "SomeUserAgent/1.0" "http://repo1.maven.org/maven2/net/sf/jopt-simple/jopt-simple/4.3/jopt-simple-4.3.jar" -O "$LIB/jopt-simple-4.3.jar"
}

function install_reflections {
    local lTMP_REFLECTIONS_INSTALL_DIR="`mktemp -d /tmp/reflections.XXXXX`"
    wget "http://reflections.googlecode.com/files/reflections-0.9.5.one-jar.jar" -O "$lTMP_REFLECTIONS_INSTALL_DIR/reflections-0.9.5.one-jar.jar" \
    && cd $lTMP_REFLECTIONS_INSTALL_DIR \
    && jar xf reflections-0.9.5.one-jar.jar \
    && cp reflections-0.9.5.jar $LIB \
    && rm -f $lTMP_REFLECTIONS_INSTALL_DIR/lib/slf4j* \
    && cp lib/* $LIB \
    && rm -rf $lTMP_REFLECTIONS_INSTALL_DIR
}

function install_neuroph {
    local lTMP_NEUROPH="`mktemp -d /tmp/neuroph.XXXXX`"
    wget -O "$lTMP_NEUROPH/neuroph-2.6.zip"  "http://sourceforge.net/projects/neuroph/files/neuroph%202.6/neuroph-2.6.zip/download"
    unzip "$lTMP_NEUROPH/neuroph-2.6.zip" -d $LIB
}

cleanup
install_lejos
install_bluetooth
install_slf4j # Seems to be required for the physics demo and reflections
#install_jbullet
install_jbox2d
install_mockito   # Mocking framework -- used in some JUnit tests
install_joptsimple # Simple arguments parser
install_reflections # For processing @Annotations
install_neuroph # For bezier neural network controller
