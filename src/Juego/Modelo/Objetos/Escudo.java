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
    public void usar(Jugador jugador) { //Aplicar efecto y crear copia "equipada" que ocupa espacio
        jugador.setDef(jugador.getDef() + bonusDef);

        Escudo equipado = new Escudo(this.getNombre() + " equipado", "Defensa +" + bonusDef + " (equipado)", bonusDef, false, false);
        jugador.getInventario().cogerObjeto(equipado);
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

    public int getBonusDef() { return bonusDef; }
}