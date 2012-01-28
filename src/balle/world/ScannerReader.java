package balle.world;

import java.util.Scanner;

public class ScannerReader extends Thread implements DataReader {

    private Scanner scanner;
    private String  newestLine = null;

    public ScannerReader() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {
            newestLine = scanner.nextLine();
        }
    }

    @Override
    public String nextLine() {
        return newestLine;
    }
}
