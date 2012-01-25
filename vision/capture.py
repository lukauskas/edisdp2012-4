import cv
import os, os.path
import subprocess
from subprocess import Popen

MPLAYER_TMPDIR = "/tmp/mplayer_frames"

class Capture:
    def __init__(self):
        self.last_frame = 1;
        self.__init_mplayer();
        self.running = True
                
    def __init_mplayer(self):
        self.mplayer = Popen(["sh", "./mplayer.sh", MPLAYER_TMPDIR])
        
    def __frame_exists(self, num):
        return os.path.exists(self.getPath(num))

    def __cleanup(self):
        #mplayer.terminate()
        # bleh :(
        subprocess.call(["killall", "mplayer"])
        self.mplayer.wait()
    
    def getPath(self, num):
        return '%s/%08d.jpg' % (MPLAYER_TMPDIR, num)
    
    """ 
    Returns the latest captured frame
    """
    def getImage(self):
        cur_frame = self.last_frame
        # Find the latest frame, and check that it is different to the previously known latest
        while True:
            if self.__frame_exists(cur_frame + 1):
                if self.__frame_exists(cur_frame - 1):
                    os.remove(self.getPath(cur_frame -  1))
                cur_frame += 1
            elif self.last_frame != cur_frame:
                # Return the frame before the last one, as the last one may not be complete yet
                frame = cv.LoadImage(self.getPath(cur_frame - 1))
                self.last_frame = cur_frame
                break
        
        return frame
            
    def stop(self):
        self.__cleanup();
        self.running = False

