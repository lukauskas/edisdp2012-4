package balle.world;

import java.util.Scanner;

public class ScannerReader implements DataReader {

    private Scanner scanner;

    public ScannerReader() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String nextLine() {
        String line = scanner.nextLine();
        System.out.println(line);
        return line;
    }
}
