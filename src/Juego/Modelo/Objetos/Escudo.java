package Juego.Modelo.Objetos;
import Juego.Modelo.Jugador;

public class Escudo extends Objeto {
    private int bonusDef;

    public Escudo(String nombre, String desc, int bonusDef,
                  boolean equipable, boolean consumible) {
        super(nombre, desc, equipable, consumible);
        this.bonusDef = bonusDef;
    }

    @Override
    public void usar(Jugador jugador) {
        jugador.setDef(jugador.getDef() + bonusDef);
    }

    public int getBonusDef() { return bonusDef; }
}