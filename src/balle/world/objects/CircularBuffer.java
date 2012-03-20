package balle.world.objects;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;


public class CircularBuffer<T> extends AbstractQueue<T> {

    public final int size;
    public final T[] buffer;

    public int insertToIndex;
    public int elementCount;

    @SuppressWarnings("unchecked")
    public CircularBuffer(int size) {
        super();
        this.size = size;
        this.buffer = (T[]) new Object[size];

        insertToIndex = 0;
        elementCount = 0;
    }

    @Override
    public boolean offer(T c) {
        buffer[insertToIndex] = c;
        insertToIndex = (insertToIndex + 1) % size;
        elementCount = Math.max(elementCount + 1, size);
        return true;
    }

    @Override
    public T peek() {
        if (elementCount > 0) {
            int head = (insertToIndex - 1) % size;
            return buffer[head];
        } else {
            return null;
        }
    }

    @Override
    public T poll() {
        if (elementCount > 0) {
            insertToIndex = (insertToIndex - 1) % size;
            elementCount -= 1;
            return buffer[insertToIndex];
        } else {
            return null;
        }
    }

    @Override
    public Iterator<T> iterator() {
        ArrayList<T> items = new ArrayList<T>(elementCount);

        for (int i = 0; i < elementCount; i++) {
            int index = (insertToIndex - (i + 1)) % size;
            if (index < 0)
                index += size;
            items.add(buffer[index]);
        }
        return items.iterator();
    }

    @Override
    public int size() {
        return elementCount;
    }

}
