package Juego.Modelo;

import Juego.Modelo.Objetos.Objeto;

public class Celda {
    public enum TipoCelda { Vacia, Pared, Puerta, Trampa } // Los tipos de celdas que puede haber en una habitacion
    private TipoCelda tipo;
    private Objeto objeto;
    private Enemigo enemigo;
    private int idHabitacionDestino; // -1 si la celda no es una puerta, identificador de la habitacion a la que la puerta lleva si != -1

    public Celda(){ //Celda vacia
        this.tipo=TipoCelda.Vacia;
        this.objeto=null;
        this.enemigo=null;
        this.idHabitacionDestino=-1;
    }
    public Celda(TipoCelda tipo){
        this(); //Crear celda vacia, cambiar tipo a uno dado
        this.tipo=tipo;
    }
    public boolean traversable(){
        return this.tipo!=TipoCelda.Pared;
    }
    public boolean tieneEnemigo(){
        return this.enemigo!=null;
    }
    public boolean tieneObjeto(){
        return this.objeto!=null;
    }
    public boolean esPuerta(){
        return this.tipo==TipoCelda.Puerta;
    }
    public boolean esTrampa(){
        return this.tipo==TipoCelda.Trampa;
    }

    public TipoCelda getTipo() {
        return tipo;
    }

    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public Enemigo getEnemigo() {
        return enemigo;
    }

    public void setEnemigo(Enemigo enemigo) {
        this.enemigo = enemigo;
    }

    public int getIdHabitacionDestino() {
        return idHabitacionDestino;
    }

    public void setIdHabitacionDestino(int idHabitacionDestino) {
        this.idHabitacionDestino = idHabitacionDestino;
    }
}
