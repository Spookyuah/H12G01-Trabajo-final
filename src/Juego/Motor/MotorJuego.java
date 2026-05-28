package Juego.Motor;

import Juego.Modelo.Habitacion;
import Juego.Modelo.Objetos.*;
import Structures.ColaPila.*;
import Structures.Lista;
import Juego.Modelo.*;

public class MotorJuego {
    private Mapa mapa;
    private Jugador jugador;
    private Lista<Enemigo> enemigos;
    private int turnosRestantes;
    private boolean partidaTerminada;
    private boolean victoria;
    private Cola<String> log;

    public MotorJuego(Mapa mapa, Jugador jugador, int turnosTotales) {
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
        registrar("Turno del jugador. Turnos restantes: "+turnosRestantes+", "+accion);

        if (accion.tieneMovimiento()){
            if (esMovValido(accion.getDestino())) moverJugador(accion.getDestino());
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
    verificarTrampaJugador();
    turnosRestantes--;
    verificarFin();
    registrar("Fin del turno.");
    }

    //Funciones privadas:
    private void moverJugador(Posicion destino){
        Posicion prev= jugador.getPosicion();
        jugador.setPosicion(destino);
        registrar("Jugador se mueve de "+prev+ " a "+destino);
    }

    private void ejecutarAtaqueJugador(String direccion){
        Posicion objetivo = calcularAdyacente(jugador.getPosicion(), direccion);
        if (objetivo==null){
            registrar("Error. Objetivo no valido");
            return;
        }
        Habitacion hab = mapa.getActual();
        if (!hab.esPosicionValida(objetivo)){
            registrar("Error. La celda "+objetivo+" no existe en esta habitacion");
            return;
        }
        Celda celda = hab.getCelda(objetivo);
        if (!celda.tieneEnemigo()) {
            registrar("Error. No hay enemigo en la celda " +objetivo);
            return;
        }
        Enemigo enemigo= celda.getEnemigo();
        int dmg= calcularDmg(jugador.getAtq(), enemigo.getDef());
        enemigo.takeDmg(dmg);
        registrar ("Enemigo "+enemigo.getNombre()+" recibe "+dmg+" puntos de daño. Vida restante "+enemigo.getVida());
        if (!enemigo.estaVivo()){
            registrar( enemigo.getNombre()+" derrotado.");
            celda.setEnemigo(null);
            removeEnemigo(enemigo);
        }
    }

    private void ejecutarUsar(int indiceObjeto){
        Inventario inv= jugador.getInventario();
        if (indiceObjeto<0 || indiceObjeto >= inv.getCantidad()){ //El inventario de N objetos tiene indices [0, N-1], getCantidad devuelve el numero de objetos, N, por lo que indiceObjeto=N esta vacio
            registrar("Error. Indice de objeto no valido");
            return;
        }
        Objeto objeto= inv.getObjeto(indiceObjeto);

        String nombre= objeto.getNombre(); //Almacenar nombre para Armas y Escudos
        objeto.usar(jugador);

        if (objeto.esConsumible()){
            inv.eliminarObjeto(indiceObjeto);
            registrar (objeto.getNombre()+" consumido y eliminado.");
        }
        else if (objeto instanceof Arma || objeto instanceof Escudo) registrar(nombre + " equipado.");
        else registrar("Objeto "+objeto.getNombre()+" usado.");
    }

    private void ejecutarRecoger(String direccion){
        Posicion objetivo = calcularAdyacente(jugador.getPosicion(), direccion);
        if (objetivo==null){
            registrar("Error. Objeto no alcanzable desde la posicion del jugador");
            return;
        }
        Habitacion hab = mapa.getActual();
        if (!hab.esPosicionValida(objetivo)){
            registrar("Error. La celda "+objetivo+" no existe en esta habitacion");
            return;
        }
        Celda celda= hab.getCelda(objetivo);
        if (celda.getObjeto()==null) {
            registrar("Error. No hay objeto en la celda " +objetivo);
            return;
        }
        Objeto objeto= celda.getObjeto();
        if (jugador.getInventario().estaLleno()){
            registrar("Error. El inventario del jugador esta lleno");
            return;
        }
        jugador.getInventario().cogerObjeto(objeto);
        celda.setObjeto(null);
        registrar("Jugador recoge "+objeto.getNombre()+".");
    }

    private void ejecutarAbrir(String direccion){
        Posicion objetivo = calcularAdyacente(jugador.getPosicion(), direccion);
        if (objetivo == null) {
            registrar("Error. No hay puerta cerca del jugador");
            return;
        }

        Habitacion hab = mapa.getActual();
        if (!hab.esPosicionValida(objetivo)) {
            registrar("Posición de puerta fuera de la habitación: " +objetivo);
            return;
        }

        Celda celda = hab.getCelda(objetivo);
        if (!celda.esPuerta()) {
            registrar("Error. No hay puerta en la celda " +objetivo);
            return;
        }

        int idDestino = celda.getIdHabitacionDestino();
        if (idDestino < 0) {
            registrar("Error. La puerta no tiene destino configurado.");
            return;
        }
        boolean exito=mapa.goToId(idDestino);
        if (exito) {
            registrar("Jugador pasa a la habitación " +idDestino);
            jugador.setPosicion(new Posicion(0, 0)); //Siempre 0,0 al entrar a una nueva sala
            actualizarEnemigos();
        } else {
            registrar("Error. No se pudo cambiar a la habitación " + idDestino);
        }
    }

    private void verificarTrampaJugador(){
        Habitacion hab= mapa.getActual();
        Celda actual= hab.getCelda(jugador.getPosicion());
        if (actual.esTrampa()) {
            int dmgTrampa= 10 +(int)(Math.random()*6); //dmgTrampa tiene un rango de [10,15] puntos de daño
            jugador.takeDmg(dmgTrampa); //Raw dmg, no es ataque asi que no aplica defensa
            registrar("Jugador pisa una trampa y pierde "+dmgTrampa+" hp. Vida restante "+jugador.getVida());
            actual.setTipo(Celda.TipoCelda.Vacia);
        }
    }
    private void verificarTrampaEnemigo(Enemigo enemigo){
        Habitacion hab = mapa.getActual();
        Celda celdaActual = hab.getCelda(enemigo.getPosicion());

        if (celdaActual.esTrampa()) {
            int dmgTrampa = 20; //Los enemigos reciben siempre 20 puntos de daño
            enemigo.takeDmg(dmgTrampa);
            registrar(enemigo.getNombre() + " ha caído en una trampa. Recibe " + dmgTrampa + " de daño. Vida restante: " + enemigo.getVida());
            celdaActual.setTipo(Celda.TipoCelda.Vacia);

            if (!enemigo.estaVivo()) {
                registrar(enemigo.getNombre() + " ha muerto por una trampa.");
                celdaActual.setEnemigo(null);
                removeEnemigo(enemigo);
            }
        }
    }

    private void ejecutarTurnoEnemigo(Enemigo enemigo){
        Habitacion hab =mapa.getActual();
        Posicion posJugador =jugador.getPosicion();
        registrar("Turno de "+enemigo.getNombre());

        if (enemigo.getPosicion().distanciaDe(posJugador) == 1) {   // ==1 significa que esta a una casilla de distancia, es decir adyacente
            int dmg= calcularDmg(enemigo.getAtq(), jugador.getDef());
            jugador.takeDmg(dmg);
            registrar("El enemigo "+enemigo.getNombre()+" ataca al jugador y hace "+dmg+" puntos de daño. Vida restante "+jugador.getVida());
            return;
        }
        Lista<Posicion> rango= Movimiento.rangoMovimiento(hab, enemigo.getPosicion(), enemigo.getVel()); //Si no esta adyacente

        Posicion mejorPos=  Movimiento.buscarCercano(rango, posJugador);

        if (!mejorPos.equals(enemigo.getPosicion())) { //Si existe la mejor posicion y no esta ya en ella //mejorPos != null &&
            Posicion anterior= enemigo.getPosicion();
            enemigo.setPosicion(mejorPos);
            registrar(enemigo.getNombre() + " se mueve a "+mejorPos);
            verificarTrampaEnemigo(enemigo);
        }
        if (!enemigo.estaVivo()) return; //Aunque se elimina en verificarTrampaEnemigo, sigue estando en la cola de turnos y hay que finalizarlo aqui
        if (enemigo.getPosicion().distanciaDe(posJugador) == 1) {
            int dmg = calcularDmg(enemigo.getAtq(), jugador.getDef());
            jugador.takeDmg(dmg);
            registrar("El enemigo " + enemigo.getNombre() + " ataca al jugador y hace " + dmg + " puntos de daño. Vida restante " + jugador.getVida());
        }
    }

    private Posicion calcularAdyacente(Posicion actual, String direccion){
        if (direccion ==null) return null;
        int x=actual.getX();
        int y=actual.getY();
        return switch (direccion.toUpperCase()) { //toUpperCase para evitar errores
            case "ARRIBA" -> new Posicion(x - 1, y);
            case "ABAJO" -> new Posicion(x + 1, y);
            case "IZQUIERDA" -> new Posicion(x, y - 1);
            case "DERECHA" -> new Posicion(x, y + 1);
            default -> null;    };
    }
    private int calcularDmg(int atq, int def){
        return (int) Math.max(0, atq* (Math.random() * 2) -def );
    }

    private void actualizarEnemigos(){
        enemigos= new Lista<>();
        Habitacion hab= mapa.getActual();
        for (int i = 0; i < hab.getFilas(); i++) {
            for (int j = 0; j < hab.getColumnas(); j++) {
                Celda celda= hab.getCelda(i,j);
                if (celda.tieneEnemigo()) enemigos.add(celda.getEnemigo());
            }
        }
        registrar("Enemigos en la sala: "+enemigos.size());
    }

    private void verificarFin(){
        Habitacion hab = mapa.getActual();
        if (mapa.esSalida(hab)){
            if (getEnemigosVivos().isEmpty()){
                partidaTerminada = true;
                victoria = true;
                registrar("Victoria: El jugador salio por la salida " + hab.getId());
            }
        }
        if (turnosRestantes <= 0) {
            partidaTerminada = true;
            victoria = false;
            registrar("Derrota: Se han agotado los turnos.");
            return;
        }
        if (!jugador.estaVivo()) {
            partidaTerminada = true;
            victoria = false;
            registrar("Derrota: El jugador ha muerto.");
        }
    }

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

    public Mapa getMapa() {
        return mapa;}
    public Jugador getJugador() {
        return jugador;}
    public int getTurnosRestantes() {
        return turnosRestantes;}
    public boolean esPartidaTerminada() {
        return partidaTerminada;}
    public boolean esVictoria() {
        return victoria;}
    public void limpiarEnemigos(){
        this.enemigos= new Lista<>();
    }
    public void setTurnosRestantes(int turnosRestantes) {
        this.turnosRestantes = turnosRestantes;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

    public void setPartidaTerminada(boolean partidaTerminada) {
        this.partidaTerminada = partidaTerminada;
    }

    public Lista<Posicion> getMovimientosPosibles() {
        return Movimiento.rangoMovimiento(mapa.getActual(), jugador.getPosicion(), jugador.getVel());
    }
    public Lista<Enemigo> getEnemigosVivos() {
        Lista<Enemigo> vivos = new Lista<>();
        for (int i = 0; i < enemigos.size(); i++) {
            if (enemigos.get(i).estaVivo()) {
                vivos.add(enemigos.get(i));
            }
        }
        return vivos;
    }
    @Override
    public String toString() {
        return "MotorJuego [Hab: " + mapa.getActual() + ", Jugador: " + jugador.getNombre() + ", Turnos: " + turnosRestantes + ", Terminada: " + partidaTerminada + ", Victoria: " + victoria + "]";
    }
}