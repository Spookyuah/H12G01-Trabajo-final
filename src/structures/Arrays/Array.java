package structures.Arrays;

import structures.ListasDobles.ListaDoble;

public class Array<T> {
    private Lista<Lista<T>> filas;
    private int nFilas;
    private int nColumnas;

    public Array(int filas, int columnas) {
        if (filas <= 0 || columnas <= 0) throw new IllegalArgumentException("Dimensiones deben ser positivas");
        this.nFilas = filas;
        this.nColumnas = columnas;
        this.filas = new Lista<>(); //Se crea una lista de filas
        for (int i = 0; i < filas; i++) {
            Lista<T> fila = new Lista<>();  //Se crea una nueva lista dentro de cada elemento de filas
            for (int j = 0; j < columnas; j++) {
                fila.add(null); //Se llenan los elementos de la lista de columnas con null
            }
            this.filas.add(fila);
        }
    }
    public int getFilas(){
        return this.nFilas;
    }
    public int getColumnas(){
        return this.nColumnas;
    }
    public T get(int fila, int columna) {
        validarIndices(fila,columna);
        Lista<T> filaSeleccionada =  obtenerFila(fila); //Lista de la columna entera
        return filaSeleccionada.get(columna);   //Devuelve el elemento de indice "columna" de la lista de la columna entera
    }
    public void set(int fila, int columna, T valor) {
        validarIndices(fila,columna);
        Lista<T> filaSeleccionada =  obtenerFila(fila);
        filaSeleccionada.set(columna, valor);
    }
    public Lista<T> getFila(int fila) {
        validarFila(fila);
        return obtenerFila(fila);
    }

    private Lista<T> obtenerFila(int indice){
        Lista<T> filaActual=null;
        for(int i=0;i<=indice;i++){
            filaActual=this.filas.get(i);
        }
        return filaActual; //Devuelve en una lista la columna de fila "indice"
    }
    private boolean esPosicionValida(int fila, int columna) {
        return fila >=0 && fila<nFilas && columna >=0 && columna<nColumnas; //Solo true si filas y columnas ambas estan en el rango de la matriz
    }
    private void validarIndices(int fila, int columna) {
        if (!esPosicionValida(fila,columna)) throw new IndexOutOfBoundsException("La posicion ("+fila+", "+columna+") esta fuera del rango de la matriz");
    }
    private void validarFila(int fila) {
        if (fila < 0 || fila >= nFilas) throw new IndexOutOfBoundsException("Fila " + fila + " fuera del rango de la matriz");
    }




}
