package balle.world.objects;

import balle.world.Coord;


public class CircularBuffer {
    /**
     * The variable to hold the buffer
     */
    public double[][]       circularBuffer;

    /**
     * Holds the length of the buffer
     */
    private final int       bufferLength;

    /**
     * Current position is the place in the buffer where the last point was
     * added
     */
    private final int       currentPos;

    public static final int X_POS    = 0;
    public static final int Y_POS    = 1;

    public static final int POSITION = 0;

    /**
     * Constructor for CircularBuffer
     * 
     * @param bufferLength
     */
    public CircularBuffer(int bufferLength) {
        this.bufferLength = bufferLength;
        this.currentPos = bufferLength;
        // 2-dimensional array to hold x and y values
        circularBuffer = new double[2][bufferLength + 1];
        // initialize position of buffer to 0
        circularBuffer[POSITION][currentPos] = 0;
    }

    /**
     * Add a point (x, y) to the current position of the buffer and increment
     * the current position
     * 
     * @param xPos
     * @param yPos
     */
    public void addCoord(double xPos, double yPos) {
        circularBuffer[X_POS][getCurrentPosition()] = xPos;
        circularBuffer[Y_POS][getCurrentPosition()] = yPos;
        incrementCurrentPosition();
    }

    /**
     * Add a point (x, y) to the current position of the buffer and increment
     * the current position
     * 
     * @param xPos
     * @param yPos
     */
    public void addCoord(Coord point) {
        circularBuffer[X_POS][getCurrentPosition()] = point.getX();
        circularBuffer[Y_POS][getCurrentPosition()] = point.getY();
        incrementCurrentPosition();
    }

    /**
     * Returns the length of the buffer
     * 
     * @return
     */
    public int getBufferLength() {
        return bufferLength;
    }

    /**
     * Get all the x values of the buffer
     * 
     * @return
     */
    public double[] getXBuffer() {
        double[] xBuffer = new double[getBufferLength()];
        for (int i = 0; i < getBufferLength(); i++) {
            xBuffer[i] = getXPosAt(i);
        }
        return xBuffer;
    }

    /**
     * Get all the y values of the buffer
     * 
     * @return
     */
    public double[] getYBuffer() {
        double[] yBuffer = new double[getBufferLength()];
        for (int i = 0; i < getBufferLength(); i++) {
            yBuffer[i] = getYPosAt(i);
        }
        return yBuffer;
    }

    /**
     * Get the x coordinate of a point stored at position
     * 
     * @param position
     * @return
     */
    public double getXPosAt(int position) {
        return circularBuffer[X_POS][position];
    }

    /**
     * Get the y coordinate of a point stored at position
     * 
     * @param position
     * @return
     */
    public double getYPosAt(int position) {
        return circularBuffer[Y_POS][position];
    }

    /**
     * Get the point stored at position
     * 
     * @param position
     * @return
     */
    public Coord getCoordAt(int position) {
        return new Coord(getXPosAt(position), getYPosAt(position));
    }

    /**
     * Get the current position of the buffer
     * 
     * @return
     */
    public int getCurrentPosition() {
        return (int) circularBuffer[POSITION][currentPos];
    }

    /**
     * Get the last position of the buffer Should be the one in front of the
     * current If it is the end of the buffer it wraps around
     * 
     * @return
     */
    public int getLastPosition() {
        return (int) (circularBuffer[POSITION][currentPos] + 1) % getBufferLength();
    }

    /**
     * Increment the position to point to the next one
     */
    public void incrementCurrentPosition() {
        circularBuffer[POSITION][currentPos]++;
        circularBuffer[POSITION][currentPos] = circularBuffer[POSITION][currentPos] % bufferLength;
    }
}
