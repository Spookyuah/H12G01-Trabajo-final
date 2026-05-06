package structures;

public class Pila<T> {

    private Nodo<T> cima;
    private int size;

    public Pila() {
        cima = null;
        size = 0;
    }

    public void push(T dato) {
        Nodo<T> temp = new Nodo<>(dato);
        temp.siguiente = cima;
        cima = temp;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T dato = cima.dato;
        cima = cima.siguiente;
        size--;
        return dato;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return cima.dato;
    }

    public boolean isEmpty() {
        return cima == null;
    }

    public int getSize() {
        return size;
    }
}
