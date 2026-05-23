package Juego.Modelo;
import Structures.Array;

public class Habitacion {
    private int id;
    private Array<Celda> tablero;

    public Habitacion(int id, int filas, int columnas) {
        this.id=id;
        this.tablero=new Array<>(filas,columnas);

        for (int i=0;i<filas;i++){  //Crear tablero con celdas vacias
            for (int j=0;j<columnas;j++){
                tablero.set(i,j,new Celda());
            }
        }
    }

    public int getId() {
        return id;
    }
    public int getFilas() {
        return tablero.getFilas();
    }
    public int getColumnas() {
        return tablero.getColumnas();
    }
    public Celda getCelda(int fila, int columna) {
        return tablero.get(fila,columna);}
    public Celda getCelda(Posicion pos){
        return tablero.get(pos.getX(),pos.getY());}
    public void setCelda(int fila, int columna, Celda c){
        tablero.set(fila,columna,c);}
    public void setCelda(Posicion pos, Celda c){
        tablero.set(pos.getX(),pos.getY(),c);}
    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < getFilas() && columna >= 0 && columna < getColumnas();
    }
    public boolean esPosicionValida(Posicion pos) {
        return pos.getX() >= 0 && pos.getX() < getFilas() && pos.getY() >= 0 && pos.getY() < getColumnas(); // pos.getX() es filas, pos.getY() es columnas
    }





}
