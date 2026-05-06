package structures.ListasDobles;

public class NodoDoble<T> {
    T dato;
    NodoDoble<T> siguiente;
    NodoDoble<T> anterior;

    NodoDoble(T dato) {
        this.dato = dato;
    }
}
