package Juego.Modelo;
import Structures.ListasDobles.*;

public class Inventario {
    private ListaDoble<Objeto> objetos;
    private int capacidad;

    public Inventario(){
        this.objetos = new ListaDoble<>();
        this.capacidad = 10; // Valor por defecto
    }
    public boolean cogerObjeto(Objeto objeto){
        if (objetos.getSize() >= capacidad) return false;
        objetos.add(objeto);
        return true;
    }
    public Objeto eliminarObjeto(int indice){
        return objetos.remove(indice);
    }
    public Objeto obtenerObjeto(int indice){
        return objetos.get(indice);
    }
    public int getCantidad(){
        return objetos.getSize();
    }
    public boolean estaLleno() {
        return objetos.getSize() >= capacidad;
    }
    public boolean estaVacio() {
        return objetos.isEmpty();
    }
    public ListaDoble<Objeto> getTodos() {
        return objetos;
    }
}
