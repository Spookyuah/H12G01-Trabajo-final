package Juego.Motor;

import Juego.Modelo.Posicion;

public class AccionJugador {
    public enum TipoAccion { NADA, ATACAR, USAR, RECOGER, ABRIR} //Usar y Recoger objetos, Abrir puertas

    private Posicion destino; //Movimiento
    private TipoAccion tipoAccion;
    private int indiceObjeto;
    private String direccion; //"ARRIBA","ABAJO",etc

    private AccionJugador(Posicion destino, TipoAccion Accion, String direccion, int indiceObjeto){
        this.destino = destino;
        this.tipoAccion = Accion;
        this.direccion = direccion;
        this.indiceObjeto = indiceObjeto;
    }
    public Posicion getDestino() {
        return destino;}

    public TipoAccion getTipoAccion() {
        return tipoAccion;}

    public int getIndiceObjeto() {
        return indiceObjeto;}

    public String getDireccion() {
        return direccion;}

    public boolean tieneMovimiento(){
        return destino !=null;}

    //Funciones para cada combinacion, ya que solo se puede usar una por turno
    //Son todos static para usarlos sin tener que hacer new AccionJugador(...) en cada accion, facilita la lectura
    public static AccionJugador mover           (Posicion destino) {
        return new AccionJugador(destino, TipoAccion.NADA, null, -1);}

    public static AccionJugador moverYAtacar    (Posicion destino, String direccion) {
        return new AccionJugador(destino, TipoAccion.ATACAR, direccion, -1);}

    public static AccionJugador moverYUsar      (Posicion destino, int indObjeto) {
        return new AccionJugador(destino, TipoAccion.USAR, null, indObjeto);}

    public static AccionJugador moverYRecoger   (Posicion destino, String direccion) {
        return new AccionJugador(destino, TipoAccion.RECOGER, direccion, -1);}

    public static AccionJugador moverYAbrir     (Posicion destino, String direccion) {
        return new AccionJugador(destino, TipoAccion.ABRIR, direccion, -1);}

    public static AccionJugador atacar          (String direccion) {
        return new AccionJugador(null, TipoAccion.ATACAR, direccion, -1);}

    public static AccionJugador usar            (int indObjeto) {
        return new AccionJugador(null, TipoAccion.USAR, null, indObjeto);}

    public static AccionJugador recoger         (String direccion) {
        return new AccionJugador(null, TipoAccion.RECOGER, direccion, -1);}

    public static AccionJugador abrir           (String direccion) {
        return new AccionJugador(null, TipoAccion.ABRIR, direccion, -1);}

    public static AccionJugador nada() {
        return new AccionJugador(null, TipoAccion.NADA, null, -1);}

    public boolean esAtaque()   { return tipoAccion == TipoAccion.ATACAR;}
    public boolean esUsar()     { return tipoAccion == TipoAccion.USAR;}
    public boolean esRecoger()  { return tipoAccion == TipoAccion.RECOGER;}
    public boolean esAbrir()    { return tipoAccion == TipoAccion.ABRIR;}
    public boolean esNada()     { return tipoAccion == TipoAccion.NADA;}
}
