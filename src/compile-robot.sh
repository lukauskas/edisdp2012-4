rm balle/brick/*.class 
rm balle/bluetooth/*.class
rm balle/controller/BluetoothController.class
rm BotCommunication.nxj

nxjc balle/brick/Controller.java balle/brick/Sensors.java balle/brick/BrickController.java balle/brick/BotCommunication.java balle/brick/Roboto.java
nxjlink balle.brick.BotCommunication -o BotCommunication.nxj
nxjlink balle.brick.Roboto -o Roboto.nxj
nxjpcc balle/bluetooth/Communicator.java balle/controller/BluetoothController.java balle/bluetooth/TestBluetooth.java 

