SDP Group  4
=========

Installation
---------------------------
All dependancies you will need should be installed by `install.sh` script.

After the script is run verify that contents of the lib dir contain all the libraries specified in the bottom of the script.

Software on the brick
----------------------------------------------
The software that has to be running on the brick in order for the
robot to be controlled by our system can be compiled by ./compile-robot.sh .
The main control program is `Roboto.nxj`, the `M1*.nxj` are a smaller submodules designed to pass milestone 1.

The system design
-----------------------------------------------

The system is split into two modules that communicate between each other over TCP: the control subsystem and the vision subsystem.

Running the control subsystem.
------------------------------------------------
The script `run.sh` handles the the start/stop of the control subsystem.
The list of available options is given by just running `run.sh` without any parameters.

To run the standalone system without a vision system, that would play against a simulator, use:

    ./run.sh -s

The pitch setting must be passed in in order to run the code on pitch.
This is used to correct for the camera height angle distorting the distances.

To run the control subsystem for the main pitch use

    ./run.sh -p0

The system tries to connect to the robot automatically when the system is started. `Roboto.nxj` has to be running on the robot before the run script is started.
If you do not want the controller to connect to the robot at all, this can be bypassed by passing the --dummy parameter to `run.sh`.

You can specify the color and the goal with --color and --goal parameters respectively. This can also be done via the GUI.

In order for the system to be fully functional on the pitch the vision subsystem has to be running.

To run the vision code:
------------------------
 
Run "runvision.sh"

    ./vision/runvision.sh -p(0|1)

where -p is 0 for the main pitch, or 1 for the other one. See vision.py --help for more options.
    
To switch between thresholding options for yellow/blue robots and the ball, press `y`, `b` and `r` respectively.
To toggle between the raw image or the channel currently being thresholded, press `t`.
