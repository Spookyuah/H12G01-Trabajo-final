package Juego.Modelo.Objetos;
import Juego.Modelo.Jugador;

public class Pocion extends Objeto {
    private int heal;

    public Pocion(String nombre, String desc, int heal, boolean equipable, boolean consumible) {
        super(nombre, desc, equipable, consumible);
        this.heal = heal;
    }

    @Override
    public void usar(Jugador jugador) {
        jugador.curar(heal);
    }

    public int getHeal() { return heal; }
}