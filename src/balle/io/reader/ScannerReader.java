package balle.io.reader;

import java.io.BufferedInputStream;
import java.util.Scanner;

public class ScannerReader extends AbstractReader {

    private final Scanner scanner;

    public ScannerReader() {
        scanner = new Scanner(new BufferedInputStream(System.in));
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
