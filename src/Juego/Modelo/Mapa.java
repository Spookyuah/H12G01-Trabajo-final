package Juego.Modelo;

import Structures.Arbol;
import Structures.Lista;

public class Mapa {
    private Arbol<Habitacion> arbolMapa;
    private Habitacion actual;
    public Mapa(){
        this.arbolMapa = new Arbol<>();
        this.actual = null;
    }
    public void setInicial(Habitacion raiz){
        arbolMapa.setRaiz(raiz);
        this.actual = raiz;
    }
    public void link(Habitacion padre, Habitacion hijo){
        arbolMapa.addHijo(padre,hijo);
    }
    public boolean goToId(int Id){
        Lista<Habitacion> lista = arbolMapa.recorridoPreorden();
        for (int i = 0; i <lista.size(); i++){
            Habitacion buscando = lista.get(i);
            if (buscando.getId() == Id){
                this.actual = buscando;
                return true;
            }
        }
        return false; //Return false si no existe dicha id
    }
    public Lista<Habitacion> getSalidas(){
        return arbolMapa.getHojas();
    }
    public boolean esSalida(Habitacion actual){
        return arbolMapa.esHoja(actual);
    }
    public Habitacion getActual(){
        return actual;
    }
    public Lista<Habitacion> getSiguientes(Habitacion h){ //Devuelve lista con los siguientes nodos visitables
        return arbolMapa.getHijos(h);
    }
    public Lista<Habitacion> getAllHabitaciones(){
        return arbolMapa.recorridoPreorden();
    }
    public Lista<Habitacion> caminoA(Habitacion h){
        return arbolMapa.trayecto(h);
    }
    public boolean esInicial(Habitacion h){
        return arbolMapa.esRaiz(h);
    }
    public int getNumHabitaciones(){
        if (arbolMapa==null) return 0;
        return arbolMapa.getNumNodos();
    }
    @Override
    public String toString() {
        return "Mapa con " + getNumHabitaciones() + " habitaciones, actual: " + (actual != null ? actual : "ninguna"); //Si no esta en una habitacion, devuelve "ninguna"
    }
}
