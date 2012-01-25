function install_lejos {
    local lLOCATION=/tmp/lejos_NXJ_0_8_5beta.tar.gz

    wget http://downloads.sourceforge.net/project/lejos/lejos-NXJ/0.8.5beta/lejos_NXJ_0_8_5beta.tar.gz?r=&ts=1327495476&use_mirror=kent -O "$lLOCATION"

    tar xvf $lLOCATION
    
}
