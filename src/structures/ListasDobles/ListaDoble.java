package structures.ListasDobles;

public class ListaDoble<T extends Comparable<T>> {
    protected NodoDoble<T> primero, ultimo;
    protected int size = 0;

    public void add(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);
        if (isEmtpy()) {
            primero = ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            ultimo = nuevo;
        }
        size++;
    }

    public T get(T dato) {
        NodoDoble<T> temp = primero;
        while (temp != null) {
            if (temp.dato.compareTo(dato) == 0) return temp.dato;
            temp = temp.siguiente;
        }
        return null;
    }

    public T del(T dato) {
        NodoDoble<T> temp = primero;
        while (temp != null) {
            if (temp.dato.compareTo(dato) == 0) {
                if (temp.anterior != null) temp.anterior.siguiente = temp.siguiente;
                else primero = temp.siguiente;

                if (temp.siguiente != null) temp.siguiente.anterior = temp.anterior;
                else ultimo = temp.anterior;

                size--;
                return temp.dato;
            }
            temp = temp.siguiente;
        }
        return null;
    }

    public boolean isEmtpy() {
        return primero == null;
    }

    public int getSize() {
        return size;
    }

    public IteradorDoble<T> getIterador() {
        return new IteradorDoble<>(this.primero);
    }
}
