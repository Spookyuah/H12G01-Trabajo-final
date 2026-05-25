package Juego.Motor;

import Juego.Modelo.Habitacion;
import Structures.ColaPila.*;
import Structures.Lista;
import Juego.Modelo.*;

public class Motor {
    private Mapa mapa;
    private Jugador jugador;
    private Lista<Enemigo> enemigos;
    private int turnosRestantes;
    private boolean partidaTerminada;
    private boolean victoria;
    private Cola<String> log;

    private Motor(Mapa mapa, Jugador jugador, int turnosTotales) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.enemigos = new Lista<>();
        this.turnosRestantes = turnosTotales;
        this.partidaTerminada = false;
        this.victoria = false;
        this.log = new Cola<>();
        registrar("Partida iniciada. Jugador:"+ jugador.getNombre() +". Turnos:"+ turnosTotales +". Habitacion inicial:"+ mapa.getActual());
    }
    public void addEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
        registrar(enemigo.getNombre()+" añadido en "+enemigo.getPosicion());
    }

    public void removeEnemigo(Enemigo enemigo) {
        for (int i = 0; i < enemigos.size(); i++) {
            if (enemigo==enemigos.get(i)) {
                enemigos.remove(i);
                registrar("Enemigo "+enemigo.getNombre()+" eliminado");
                return;
            }}}

    public void ejecutarTurnoJugador(AccionJugador accion){
        if (partidaTerminada) {
            registrar("Error. Partida ya finalizada, accion ignorada");
            return;}
        registrar("-Turnos restantes: "+turnosRestantes+", "+accion);

        if (accion.tieneMovimiento()){
            if (esMovValido(accion.getDestino())) moverJugador(destino);
            else registrar("Error. Movimiento a" +accion.getDestino()+ " no valido");
        }
        switch (accion.getTipoAccion()){
            case ATACAR:  ejecutarAtaqueJugador(accion.getDireccion()); break;
            case USAR:    ejecutarUsar(accion.getIndiceObjeto());       break;
            case RECOGER: ejecutarRecoger(accion.getDireccion());       break;
            case ABRIR:   ejecutarAbrir(accion.getDireccion());         break;
            case NADA:    break;
        }
        Cola<Enemigo> colaTurnos= new Cola<>();
        for (int i = 0; i < enemigos.size(); i++) colaTurnos.add(enemigos.get(i)); //Llenar colaTurnos con los enemigos
        while (!colaTurnos.isEmpty()){
            Enemigo enemigo = colaTurnos.remove();
            if (enemigo.estaVivo()) ejecutarTurnoEnemigo(enemigo);
        }
    verificarCeldaTrampa();
    turnosRestantes--;
    verificarFin();
    registrar("Fin del turno.");
    }

    //Funciones privadas:

    //todo private void moverJugador(Posicion destino){}
    //todo private void ejecutarAtaqueJugador(Posicion destino){}
    //todo private void ejecutarUsar(int indiceObjeto){}
    //todo private void ejecutarRecoger(Posicion destino){}
    //todo private void ejecutarAbrir(Posicion destino){}
    //todo private void ejecutarTurnoEnemigo(Enemigo enemigo){}
    //todo private void verificarCeldaTrampa(Posicion posicion){}

    private boolean esMovValido(Posicion destino){
        Lista<Posicion> rango = Movimiento.rangoMovimiento(mapa.getActual(), destino, jugador.getVel());
        for (int i = 0; i < rango.size(); i++) {
            if (rango.get(i).equals(destino)) return true;
        }
        return false;
    }

    private void registrar(String mensaje){
        log.add(mensaje);
        System.out.println("[LOG]-" + mensaje);
    }

    public Lista<String>getLog() {
        Lista<String> logLista = new Lista<>();
        Cola<String> aux = new Cola<>();
        while(!log.isEmpty()){
            String entry = log.remove();    //Vaciar log y añadirlo tanto a logLista como aux, log se elimina
            logLista.add(entry);
            aux.add(entry);
        }
        while(!aux.isEmpty()) log.add(aux.remove());    //Rehacer log con los valores de aux

        return logLista;
    }
}
