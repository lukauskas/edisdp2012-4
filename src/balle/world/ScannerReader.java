package balle.world;

import java.util.Scanner;

public class ScannerReader extends Thread {

    private Scanner  scanner;
    private String   newestLine = null;
    private Listener listener;

    public ScannerReader(Listener listener) {
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {
            try {
                listener.propagate(scanner.nextLine());
            } catch (java.util.NoSuchElementException e) {
                System.out.println("No input from camera!");
            }
        }
    }

}
