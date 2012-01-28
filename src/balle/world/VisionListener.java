package balle.world;

public class VisionListener implements Listener, DataReader {
    String newestLine = null;

    @Override
    public void propagate(Object item) {
        newestLine = (String) item;

    }

    @Override
    public String nextLine() {
        return newestLine;
    }

}
