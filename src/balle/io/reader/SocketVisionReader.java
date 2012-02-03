package balle.io.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/** SocketVisionReader:
 * 	Captures input from vision system.
 * 
 * Usage:
 * 		- Create an instance.
 * 		- Add the listener.
 * 
 * 	Contains an inner class SocketThread that will update
 *  listeners with world information.
 * 
 * NOTE: Reader implements AbstractVisionReader  
 */
public class SocketVisionReader extends Reader {
    
    public static final int PORT = 28546;

    public SocketVisionReader() {
        new SocketThread().start();
    }

    class SocketThread extends Thread {
    
    	@Override
    	public void run() {

			try {
				ServerSocket server = new ServerSocket(PORT);

				while (true) {
					Socket socket = server.accept();

					System.out.println("Client connected");

					Scanner scanner = new Scanner(new BufferedInputStream(
							socket.getInputStream()));

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
