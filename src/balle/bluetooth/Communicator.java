package balle.bluetooth;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

public class Communicator {

    private final static String NAME      = "4s";
    private final static String MAC       = "00:16:53:0A:07:1D";

    private boolean             connected = false;
    NXTConnector                conn;
    DataOutputStream            dos;
    int                         m         = 5;
    int                         m2        = 0;

    /**
     * Constructor method, initializes connections.
     */
    public Communicator() {
        conn = connect();
        dos = conn.getDataOut();
    }

    /**
     * Connect method, initializes bluetooth connection to the NXT.
     * 
     * @return The connection object.
     */
    private NXTConnector connect() {
        NXTConnector conn = new NXTConnector();
        conn.setDebug(true);
        System.out.println("Connecting to " + NAME + "...");
        connected = conn.connectTo(NAME, MAC, NXTCommFactory.BLUETOOTH);

        if (!connected) {
            System.err.println("Failed to connect to any NXT");
            System.exit(1);
            return null;
        } else {
            System.out.println("Connection initialised\n");
            return conn;
        }
    }

    /**
     * The public sender method. Sends commands.
     * 
     * @param message
     *            The command to send
     * @see balle.brick.BotCommunication balle.brick.BotCommunication for
     *      meanings
     */
    public boolean send(Integer message) {
        try {
            // System.err.println("Sending: " + Integer.toHexString(message));
            dos.writeInt(message);
            dos.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Sending failed, IOException: ");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Closes the data connection, required if the robot is still waiting for a
     * command.
     * 
     * @param dataIn
     *            The DataInputStream from the bluetooth connection.
     * @param dataOut
     *            The DataOutputStream from the bluetooth connection.
     */
    public void close() {
        try {
            dos.writeInt(-1);
            dos.flush();
            dos.close();
            conn.close();
            connected = false;
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Checks whether we are okay to send commands. In other words checks if the
     * connector has initialised and is working fine.
     * 
     * @return true if connector is currently connected
     */
    public boolean isConnected() {
        return connected;
    }
}
