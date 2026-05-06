package structures.Arrays;

public class Lista<T> {
    private class Nodo{
        T dato;
        Nodo siguiente;

        Nodo (T dato){
            this.dato=dato;
            this.siguiente=null;
        }
    }
    private Nodo primero;
    private int size;

    public Lista(){
        this.primero=null;
        this.size=0;
    }
    public void add(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (primero == null) {
            primero = nuevo;
        } else {
            Nodo aux = primero;
            while (aux.siguiente != null) aux = aux.siguiente;
            aux.siguiente = nuevo;
        }
        size++;
    }
    public T get(int indice) {
        validarIndice(indice);
        Nodo aux = primero;
        for (int i=0; i<indice; i++){
            aux=aux.siguiente;
        }
        return aux.dato;
    }
    public T set(int indice, T dato) {
        validarIndice(indice);
        Nodo aux = primero;
        for (int i=0; i<indice; i++){
            aux=aux.siguiente;
        }
        T anterior = aux.dato;
        aux.dato = dato;
        return anterior;
    }
    public T remove(int indice) {
        validarIndice(indice);
        if (indice ==0){
            T dato=primero.dato;
            primero=primero.siguiente;
            size--;
            return dato;
        }
        Nodo aux = primero;
        for (int i=0; i<indice; i++){
            aux=aux.siguiente;
        }
        T anterior = aux.siguiente.dato;
        aux.siguiente=aux.siguiente.siguiente;
        size--;
        return anterior;
    }
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size == 0;
    }

    private void validarIndice(int indice){
        if (indice < 0 || indice >= size){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera del rango (0-"+(size-1)+")");
        }
    }

    public void forEach(java.util.function.Consumer<T> accion) {    //forEach recibe una lista, y para todos sus elementos ejecuta la accion dada
        Nodo actual = primero;
        while (actual != null) {
            accion.accept(actual.dato);
            actual = actual.siguiente;
        }
    }
}
