SDP_HOME=`pwd`
LIB="$SDP_HOME/lib"

function install_lejos {
    local lLOCATION="`mktemp /tmp/lejos_NXJ_0_8_5beta.XXXXXX.tar.gz`"

    echo "> Downloading lejos to $lLOCATION"

    wget -O "$lLOCATION" "http://downloads.sourceforge.net/project/lejos/lejos-NXJ/0.8.5beta/lejos_NXJ_0_8_5beta.tar.gz?r=&ts=1327495476&use_mirror=kent" \
    && echo "> extracting $lLOCATION" \
    && cd $LIB \
    && tar xvf $lLOCATION \
    && rm $lLOCATION
}
function cleanup {
    rm -rf $LIB 
    mkdir $LIB
}
cleanup
install_lejos
