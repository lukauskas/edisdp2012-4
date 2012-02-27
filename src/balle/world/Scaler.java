package balle.world;

import balle.misc.Globals;

public class Scaler {

    private final float XSHIFTM;
    private final float YSHIFTM;

    private float       scale;

    public Scaler(float XSHIFTM, float YSHIFTM) {
        this.XSHIFTM = XSHIFTM;
        this.YSHIFTM = YSHIFTM;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int m2PX(double x) {
        return m2PX((float) x);
    }

    public int m2PX(float x) {
        return (int) ((x + XSHIFTM) * scale);
    }

    public int m2PY(double y) {
        return m2PY((float) y);
    }

    public int m2PY(float y) {
        y = Globals.PITCH_HEIGHT - y;
        return (int) ((y + YSHIFTM) * scale);
    }

    // Working backwards

    public float pX2m(double x) {
        return (((float) x) / scale) - XSHIFTM;
    }

    public float pY2m(double y) {
        return YSHIFTM - (((float) y) / scale) + Globals.PITCH_HEIGHT;
    }

}
