package balle.bluetooth;

import balle.controller.BluetoothController;

public class TestThroughput {
    BluetoothController controller = null;

    public TestThroughput(BluetoothController bluetoothController) {
        controller = bluetoothController;
    }

    public boolean isReady() {
        return controller.isReady();
    }

    public void send(int count) {
        long startedSending = System.currentTimeMillis();
        System.out.printf("Started sending : %s\n", startedSending);
        for (int i = 0; i < count; i++) {
            controller.setWheelSpeeds(200, 200);
        }
        long finishedSending = System.currentTimeMillis();
        System.out.printf("Finished sending : %s\n", finishedSending);
        long timeTaken = finishedSending - startedSending;
        System.out.printf("Took %s ms (%s ms per msg)\n", timeTaken,
                (double) timeTaken / count);
    }

    public static void main(String[] args) {
        TestThroughput test = new TestThroughput(new BluetoothController(
                new Communicator()));

        while (!test.isReady()) {
            continue;
        }

        System.out.println("Ready");
        test.send(500);
    }
}
