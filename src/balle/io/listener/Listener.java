package balle.io.listener;

public interface Listener {

    public void update(double yPosX, double yPosY, double yRad,
                       double bPosX, double bPosY, double bRad, double ballPosX,
                       double ballPosY, long timestamp);
}