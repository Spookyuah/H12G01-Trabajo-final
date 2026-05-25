package Juego.Modelo;
import Structures.ListasDobles.*;

public class Jugador {
    private String nombre;
    private int vida;
    private int vidaMax;
    private int atq;
    private int def;
    private int vel;
    private Posicion posicion;
    private Inventario inventario;

    public  Jugador(String nombre,  int vida, int vidaMax, int atq, int def, int vel, Posicion posicion) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMax = vidaMax;
        this.atq = atq;
        this.def = def;
        this.vel = vel;
        this.posicion = posicion;
        this.inventario = new Inventario(); // Inventario vacio
    }

    public String getNombre() {
        return nombre;
    }
    public int getVida() {
        return vida;
    }
    public int getVidaMax() {
        return vidaMax;
    }
    public int getAtq() {
        return atq;
    }
    public int getDef() {
        return def;
    }
    public int getVel() {
        return vel;
    }
    public Posicion getPosicion() {
        return posicion;
    }
    public Inventario getInventario() {
        return inventario;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
    public void takeDmg(int dmg){
        this.vida = Math.max(0, this.vida - dmg); // Toma -dmg- cantidad de daño, se queda en 0 si sobrepasa su vida restante. El calculo del daño se hace previamente
    }
    public void curar(int heal){
        this.vida = Math.min(vidaMax, this.vida + heal); // Cura -heal- puntos de vida, capado en -vidaMax- puntos
    }
    public boolean estaVivo(){
        return vida>0;
    }
    @Override
    public String toString() {
        return nombre + " [Vida: " + vida + "/" + vidaMax + "  Atq: " + atq + "  Def: " + def + "  Vel: " + vel + "  Posición: " + posicion + "]";
    }



}
