package Juego.Modelo;

public abstract class Objeto implements Comparable<Objeto> {
    private String nombre;
    private String desc;
    private boolean equipable;
    private boolean consumible; // De usos limitados

    public Objeto(String nombre, String desc, boolean equipable, boolean consumible) {
        this.nombre = nombre;
        this.desc = desc;
        this.equipable = equipable;
        this.consumible = consumible;
    }
    public String getNombre() { return nombre; }
    public String getDesc() { return desc; }
    public boolean esEquipable() { return equipable; }
    public boolean esConsumible() { return consumible; }

    public abstract void usar(Jugador jugador); // Funcion Usar personalizable para cada tipo de objeto
}
