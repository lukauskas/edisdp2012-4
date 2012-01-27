rm balle/brick/*.class 
rm balle/bluetooth/*.class
rm balle/controller/BluetoothController.class

nxjc balle/brick/Controller.java balle/brick/BrickController.java balle/brick/Roboto.java balle/brick/Kick.java balle/brick/milestone1/RollAndKick.java balle/brick/milestone1/RollThroughField.java \
&& nxjlink balle.brick.Roboto -o Roboto.nxj \
&& nxjlink balle.brick.Kick -o M1Kick.nxj \
&& nxjlink balle.brick.milestone1.RollAndKick -o M1RollAndKick.nxj \
&& nxjlink balle.brick.milestone1.RollThroughField -o M1RollThroughField.nxj \
&& nxjpcc balle/bluetooth/Communicator.java balle/controller/BluetoothController.java balle/bluetooth/TestBluetooth.java \

