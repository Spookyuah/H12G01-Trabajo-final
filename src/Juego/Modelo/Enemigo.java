package Juego.Modelo;

public class Enemigo {
    private String nombre;
    private int vida;
    private int vidaMax;
    private int atq;
    private int def;
    private int spd;
    private Posicion posicion;
    public  Enemigo(String nombre,  int vida, int vidaMax, int atq, int def, int spd, Posicion posicion) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMax = vidaMax;
        this.atq = atq;
        this.def = def;
        this.spd = spd;
        this.posicion = posicion;
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
    public int getSpd() {
        return spd;
    }
    public Posicion getPosicion() {
        return posicion;
    }
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
    public void takeDmg(int dmg){
        this.vida = Math.max(0, this.vida - dmg); // Toma -dmg- cantidad de daño, se queda en 0 si sobrepasa su vida restante
    }
    public boolean estaVivo(){
        return vida>0;
    }
    @Override
    public String toString() {
        return nombre + " [Vida: " + vida + "/" + vidaMax + "  Atq: " + atq + "  Def: " + def + "  Vel: " + spd + "  Posición: " + posicion + "]";
    }
}
