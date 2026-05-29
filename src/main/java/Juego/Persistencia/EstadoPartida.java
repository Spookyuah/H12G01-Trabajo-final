package Juego.Persistencia;

import Structures.Lista;

public class EstadoPartida {
    public static class JugadorEstado {
        public String nombre;
        public int vida;
        public int vidaMax;
        public int atq;
        public int def;
        public int vel;
        public int habitacionActualId;
        public int x;
        public int y;
        public Lista<ObjetoEstado> inventario;
    }
    public static class ObjetoEstado {
        public String nombre;
        public String desc;
        public String tipo;
        public int valor;
        public boolean equipable;
        public boolean consumible;
    }
    public static class EnemigoEstado {
        public String nombre;
        public int vida;
        public int vidaMax;
        public int atq;
        public int def;
        public int vel;
        public int habitacionId;
        public int x;
        public int y;
    }
    public static class CeldaEstado {
        public int habitacionId;
        public int x;
        public int y;
        public String tipo;
        public boolean tieneObjeto;
        public ObjetoEstado objeto;
        public boolean tieneEnemigo;
        public EnemigoEstado enemigo;
    }

    public int turnosRestantes;
    public JugadorEstado jugador;
    public Lista<EnemigoEstado> enemigos;
    public Lista<CeldaEstado> celdasModificadas;
    public boolean partidaTerminada;
    public boolean victoria;

}
