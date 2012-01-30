package balle.io.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketVisionReader extends AbstractVisionReader implements Runnable {
    
    public static final int PORT = 28546;

    @Override
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
                
        try {
            ServerSocket server = new ServerSocket(PORT);
            
            while (true) {
                Socket socket = server.accept();

                System.out.println("Client connected");

                Scanner scanner = new Scanner(new BufferedInputStream(socket.getInputStream()));
    
                while (scanner.hasNextLine()) {
                    try {
                        parse(scanner.nextLine());
                    } catch (java.util.NoSuchElementException e) {
                        System.out.println("No input from camera!");
                    }
                }

                System.out.println("Client disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private void parse(String line) {

        // Ignore Comments
        if (line.charAt(0) != '#') {

            System.out.println("Updating: " + line);

            String[] tokens = line.split(" ");

            propagate(Double.parseDouble(tokens[0]),
                    Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]),

                    Double.parseDouble(tokens[3]),
                    Double.parseDouble(tokens[4]),
                    Double.parseDouble(tokens[5]),

                    Double.parseDouble(tokens[6]),
                    Double.parseDouble(tokens[7]),

                    Long.parseLong(tokens[8]));
        }
    }

}
