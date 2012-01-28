package balle.io.reader;

import java.util.Scanner;

public class ScannerReader extends AbstractReader {

    private Scanner scanner;

    public ScannerReader() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {
            try {
                propagateString(scanner.nextLine());
            } catch (java.util.NoSuchElementException e) {
                System.out.println("No input from camera!");
            }
        }
    }

}
