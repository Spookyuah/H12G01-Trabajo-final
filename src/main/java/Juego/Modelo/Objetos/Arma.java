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
    public void usar(Jugador jugador) { //Aplica el efecto y crea una copia del arma "equipada", para ocupar espacio en el inventario
        jugador.setAtq(jugador.getAtq() + bonusAtq);

        Arma equipada = new Arma(this.getNombre() + " equipada", "Ataque +" + bonusAtq + " (equipado)", bonusAtq, false, false);
        jugador.getInventario().cogerObjeto(equipada);

        eliminarDelInventario(jugador);
    }
    private void eliminarDelInventario(Jugador jugador) {
        for (int i = 0; i < jugador.getInventario().getCantidad(); i++) {
            if (jugador.getInventario().getObjeto(i) == this) {
                jugador.getInventario().eliminarObjeto(i);
                return;
            }
        }
    }
    public int getBonusAtq() { return bonusAtq; }
}