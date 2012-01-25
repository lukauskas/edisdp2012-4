rm balle/brick/*.class 
rm balle/bluetooth/*.class
rm balle/controller/BluetoothController.class

nxjc balle/brick/Controller.java balle/brick/BrickController.java balle/brick/Roboto.java \
&& nxjlink balle.brick.Roboto -o Roboto.nxj \
&& nxjpcc balle/bluetooth/Communicator.java balle/controller/BluetoothController.java balle/bluetooth/TestBluetooth.java \

