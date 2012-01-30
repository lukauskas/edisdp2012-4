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
                        propagateString(scanner.nextLine());
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

}
