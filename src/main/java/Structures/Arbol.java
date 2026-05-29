package Structures;

import Structures.ColaPila.*;

public class Arbol<N> {
    private class Nodo{
        N dato;
        Nodo padre; //Almacena el nodo previo, null si el nodo es la raiz
        Lista<Nodo> hijos;
        Nodo(N dato){
            this.dato = dato;
            this.padre = null;
            this.hijos = new Lista<>();
        }}
    private Nodo raiz;
    private int numNodos;
    public Arbol(){
        this.raiz = null;
        this.numNodos = 0;
    }
    public void setRaiz(N dato) {
        if (raiz != null) {
            throw new IllegalStateException("El árbol ya tiene raíz");
        }
        raiz = new Nodo(dato);
        numNodos = 1;
    }
    public void addHijo(N padre, N hijo) {
        Nodo nodoPadre = buscarNodo(raiz, padre);

        if (nodoPadre == null) throw new IllegalArgumentException("El nodo padre no existe en el árbol: " + padre);

        // Verificar que el hijo no existe ya en el árbol
        if (buscarNodo(raiz, hijo) != null) {
            throw new IllegalArgumentException("El nodo hijo ya existe en el árbol: " + hijo);
        }

        // Crear el nuevo nodo hijo y enlazarlo
        Nodo nodoHijo = new Nodo(hijo);
        nodoHijo.padre = nodoPadre;
        nodoPadre.hijos.add(nodoHijo);
        numNodos++;
    }
    //-------


    public Lista<N> getHijos(N dato){
        Nodo nodo= buscarNodo(raiz,dato);
        if (nodo==null) return new Lista<>(); //Lista vacia si no hay hijos

        Lista<N> hijos = new Lista<>();
        for (int i=0; i<nodo.hijos.size(); i++){ //Llenar la lista de los valores de cada hijo
            hijos.add(nodo.hijos.get(i).dato);
        }
        return hijos;
    }

    public N getPadre(N dato){
        Nodo nodo= buscarNodo(raiz,dato);
        if (nodo==null || nodo.padre==null) return null; //Return null si el nodo o su padre son null
        return nodo.padre.dato;
    }

    public boolean esHoja(N dato) {
        Nodo nodo = buscarNodo(raiz, dato);
        return nodo != null && nodo.hijos.isEmpty();
    }

    public boolean esRaiz(N dato) {
        return raiz != null && raiz.dato.equals(dato);
    }

    public N getRaiz(){
        return raiz!=null? raiz.dato : null; //raiz.dato si raiz!=null, null si raiz==null
    }
    public int getNumNodos(){
        return numNodos;
    }
    
    public Lista<N> recorridoPreorden() {
        Lista<N> resultado = new Lista<>();
        preordenInterno(raiz, resultado);
        return resultado;
    }

    private void preordenInterno(Nodo nodo, Lista<N> resultado) {   //Funcion recursiva interna para preorden
        if (nodo == null) return;
        resultado.add(nodo.dato);
        for (int i = 0; i < nodo.hijos.size(); i++) {
            preordenInterno(nodo.hijos.get(i), resultado);
        }
    }

    public Lista<N> recorridoPostorden() {
        Lista<N> resultado = new Lista<>();
        postordenInterno(raiz, resultado);
        return resultado;
    }

    private void postordenInterno(Nodo nodo, Lista<N> resultado) {  //Funcion recursiva interna para postorden
        if (nodo == null) return;
        for (int i = 0; i < nodo.hijos.size(); i++) {
            postordenInterno(nodo.hijos.get(i), resultado);
        }
        resultado.add(nodo.dato);
    }

    public Lista<N> recorridoPorNiveles() {
        Lista<N> resultado = new Lista<>();
        if (raiz == null) return resultado;

        Cola<Nodo> cola = new Cola<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            Nodo actual = cola.remove();
            resultado.add(actual.dato);

            for (int i = 0; i < actual.hijos.size(); i++) {
                cola.add(actual.hijos.get(i));
            }
        }
        return resultado;
    }

    private Nodo buscarNodo(Nodo actual, N dato) {
        if (actual == null) return null;

        if (actual.dato.equals(dato)) return actual;

        // Buscar en los hijos
        for (int i = 0; i < actual.hijos.size(); i++) {
            Nodo encontrado = buscarNodo(actual.hijos.get(i), dato);
            if (encontrado != null) {
                return encontrado;
            }
        }

        return null;
    }

    public Lista<N> trayecto(N objetivo){
        Nodo nodo= buscarNodo(raiz, objetivo);
        if (nodo==null) return new Lista<>();

        Pila<N> pila =new Pila<>();
        Nodo actual= nodo;
        while (actual!=null){   //Se llena la pila con los valores para invertir el orden
            pila.push(actual.dato);
            actual =actual.padre;}

        Lista<N> trayecto = new Lista<>();
        while (!pila.isEmpty()){
            trayecto.add(pila.pop());
        }
        return trayecto;
    }

    public int profundidad(N dato){
        Nodo nodo= buscarNodo(raiz, dato);
        if (nodo==null) return -1;
        int depth=0;
        Nodo actual= nodo;
        while (actual.padre !=null){ //Añadir 1 a profundidad por cada nodo hasta alcanzar el objetivo
            depth++;
            actual=actual.padre;
        }
        return depth;
    }

    public int altura() {
        return alturaInterna(raiz);
    }

    private int alturaInterna(Nodo nodo) { //Funcion recursiva interna para obtener la altura del arbol (la profundidad maxima
        if (nodo == null) return -1;
        if (nodo.hijos.isEmpty()) return 0;

        int maxAltura = -1;
        for (int i = 0; i < nodo.hijos.size(); i++) {
            int alturaHijo = alturaInterna(nodo.hijos.get(i));
            if (alturaHijo > maxAltura) {
                maxAltura = alturaHijo;
            }
        }
        return maxAltura + 1;
    }

    public Lista<N> getHojas() {
        Lista<N> hojas = new Lista<>();
        getHojasInterno(raiz, hojas);
        return hojas;
    }

    private void getHojasInterno(Nodo nodo, Lista<N> hojas) { //Funcion recursiva interna para obtener una lista con todas las hojas del arbol
        if (nodo == null) return;
        if (nodo.hijos.isEmpty()) {
            hojas.add(nodo.dato);
        }
        for (int i = 0; i < nodo.hijos.size(); i++) {
            getHojasInterno(nodo.hijos.get(i), hojas);
        }
    }








}
