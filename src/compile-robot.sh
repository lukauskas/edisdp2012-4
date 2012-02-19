rm balle/brick/*.class 
rm balle/bluetooth/*.class
rm balle/bluetooth/messages/*.class
rm balle/controller/BluetoothController.class

nxjc balle/bluetooth/messages/*.java balle/controller/Controller.java balle/brick/BrickController.java balle/brick/Roboto.java balle/brick/Kick.java balle/brick/milestone1/RollAndKick.java balle/brick/milestone1/RollThroughField.java balle/brick/PenaltyKick.java balle/brick/TestThroughput.java \
&& nxjlink balle.brick.Roboto -o Roboto.nxj \
&& nxjlink balle.brick.Kick -o M1Kick.nxj \
&& nxjlink balle.brick.PenaltyKick -o M1PenaltyKick.nxj \
&& nxjlink balle.brick.milestone1.RollAndKick -o M1RollAndKick.nxj \
&& nxjlink balle.brick.milestone1.RollThroughField -o M1Roll.nxj \
&& nxjlink balle.brick.TestThroughput -o TestThroughput.nxj \
&& nxjpcc balle/bluetooth/Communicator.java balle/controller/BluetoothController.java balle/bluetooth/TestBluetooth.java \

