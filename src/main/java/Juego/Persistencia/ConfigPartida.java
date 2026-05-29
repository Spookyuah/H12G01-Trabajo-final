package Juego.Persistencia;

import Structures.Lista;

public class ConfigPartida {
    public static class HabitacionConfig{
        public int id;
        public int filas;
        public int columnas;
        public Lista<CeldaConfig> celdas;
        public Lista<PuertaConfig> puertas;
        public Integer padreId;  //Como int, pero puede ser null, para cuando sea raiz
    }
    public static class CeldaConfig{
        public int x;
        public int y;
        public String tipo;     // Vacia, Pared, Puerta, Trampa
        public EnemigoConfig enemigo;
        public ObjetoConfig objeto;
    }
    public static class EnemigoConfig{
        public String nombre;
        public int vida;
        public int vidaMax;
        public int atq;
        public int def;
        public int vel;
        public int xInicial;
        public int yInicial;
    }
    public static class ObjetoConfig{
        public String nombre;
        public String desc;
        public String tipo;        // "POCION_VIDA", "ARMA", "ESCUDO", "LLAVE"
        public int valor;          // Puntos de curación, daño extra, etc.
        public boolean equipable;
        public boolean consumible;
    }
    public static class PuertaConfig{
        public int x;
        public int y;
        public int idHabitacionDestino;
    }
    public static class JugadorConfig{
        public String nombre;
        public int vida;
        public int vidaMax;
        public int atq;
        public int def;
        public int vel;
        public int habitacion;
        public int xInicial;
        public int yInicial;
    }
    public Lista<HabitacionConfig> habitaciones;
    public JugadorConfig jugador;
    public int turnosTotales;
}
