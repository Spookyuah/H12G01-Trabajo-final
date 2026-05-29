package Juego.Modelo;
import Juego.Modelo.Objetos.Objeto;
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
    public Objeto getObjeto(int indice){
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
    public void vaciar(){
        while (!estaVacio()) objetos.remove(0);
    }
    public ListaDoble<Objeto> getTodos() {
        return objetos;
    }

    @Override
    public String toString() {
        if (estaVacio()) return "Inventario vacío";
        StringBuilder sb = new StringBuilder("Inventario (" + objetos.getSize() + "/" + capacidad + "):");
        for (int i = 0; i < objetos.getSize(); i++) {
            sb.append("\n  ").append(i).append(". ").append(objetos.get(i).getNombre());
        }
        return sb.toString();
    }
}
