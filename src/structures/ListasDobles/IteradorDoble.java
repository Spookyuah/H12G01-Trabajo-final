package structures.ListasDobles;

public class IteradorDoble<T> {
    private NodoDoble<T> actual;

    public IteradorDoble(NodoDoble<T> comienzo) {
        this.actual = comienzo;
    }

    public boolean hasNext() {
        return actual != null;
    }

    public T next() {
        if (!hasNext()) return null;
        T dato = actual.dato;
        actual = actual.siguiente;
        return dato;
    }
}
