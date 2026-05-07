package Structures.ListasDobles;

public class ListaDoble<T extends Comparable<T>> {
    protected NodoDoble<T> primero, ultimo;
    protected int size = 0;

    public void add(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);
        if (isEmpty()) {
            primero = ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            ultimo = nuevo;
        }
        size++;
    }

    public T get(int indice) {
        if (indice < 0 || indice >= size) throw new IndexOutOfBoundsException("Indice fuera de rango");
        NodoDoble<T> aux = primero;
        for (int i = 0; i < indice; i++) {
            aux = aux.siguiente;
        }
        return aux.dato;
    }

    public T remove(int indice) {
        if (indice < 0 || indice >= size) throw new IndexOutOfBoundsException("Indice fuera de rango");
        NodoDoble<T> aux= primero;
        for (int i = 0; i < indice; i++) aux= aux.siguiente;
        if (aux.anterior != null) {
            aux.anterior.siguiente = aux.siguiente;
        } else {
            primero = aux.siguiente;  // Era el primero
        }

        if (aux.siguiente != null) {
            aux.siguiente.anterior = aux.anterior;}
        else {
            ultimo = aux.anterior;}  // Era el último

        size--;
        return aux.dato;
    }

    public boolean isEmpty() {
        return primero == null;
    }

    public int getSize() {
        return size;
    }

    public IteradorDoble<T> getIterador() {
        return new IteradorDoble<>(this.primero);
    }
}
