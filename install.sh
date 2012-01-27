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
cleanup
install_lejos
install_bluetooth
install_jbullet

