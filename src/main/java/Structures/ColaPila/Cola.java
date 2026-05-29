package Structures.ColaPila;

public class Cola<T> {

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int size;

    public Cola() {
        primero = null;
        ultimo = null;
        size = 0;
    }

    public void add(T dato) {
        Nodo<T> temp = new Nodo<>(dato);

        if (isEmpty()) {
            primero = temp;
            ultimo = temp;
        } else {
            ultimo.siguiente = temp;
            ultimo = temp;
        }

        size++;
    }

    public T remove() {
        if (isEmpty()) {
            return null;
        }

        T dato = primero.dato;
        primero = primero.siguiente;

        if (primero == null) {
            ultimo = null;
        }

        size--;
        return dato;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return primero.dato;
    }

    public boolean isEmpty() {
        return primero == null;
    }

    public int getSize() {
        return size;
    }
}
