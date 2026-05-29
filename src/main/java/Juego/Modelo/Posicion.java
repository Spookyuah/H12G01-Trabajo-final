package Juego.Modelo;

public class Posicion {
    private int x;
    private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int distanciaDe(Posicion objetivo){
        return Math.abs(this.x - objetivo.x) + Math.abs(this.y - objetivo.y);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Posicion otra = (Posicion) obj; // Cast de Object a Posicion, crear otra para poder utilizarlo
        return this.x == otra.x && this.y == otra.y;
    }
    @Override
    public String toString() {
        return "(" +x+ "," +y+ ")";
    }
}
