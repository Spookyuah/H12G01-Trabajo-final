package Juego.Modelo.Objetos;
import Juego.Modelo.Jugador;

public class Arma extends Objeto {
    private int bonusAtq;

    public Arma(String nombre, String descripcion, int bonusAtq,
                boolean equipable, boolean consumible) {
        super(nombre, descripcion, equipable, consumible);
        this.bonusAtq = bonusAtq;
    }

    @Override
    public void usar(Jugador jugador) {
        jugador.setAtq(jugador.getAtq() + bonusAtq);
    }

    public int getBonusAtq() { return bonusAtq; }
}