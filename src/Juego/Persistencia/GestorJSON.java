package Juego.Persistencia;

import Structures.Lista;
import Structures.ListasDobles.ListaDoble;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Juego.Modelo.*;
import Juego.Modelo.Objetos.*;
import Juego.Motor.MotorJuego;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestorJSON {
    private Gson gson;

    public GestorJSON() {
        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Lista.class, new ListaAdapter()).create(); //Pretty Printing lo hace legible, registerTypeAdapter permite que pueda leer las Listas del package Structures
    }

    public MotorJuego cargarConfig(String rutaArchivo) throws IOException { // Input-Output exception
        FileReader reader = new FileReader(rutaArchivo);
        ConfigPartida config = gson.fromJson(reader, ConfigPartida.class);
        reader.close();
        return construirMotorJuego(config);

    }

    private MotorJuego construirMotorJuego(ConfigPartida config) {
        Lista<Habitacion> habitaciones = new Lista<>();
        for (int i = 0; i < config.habitaciones.size(); i++) { //--Recrear habitaciones
            ConfigPartida.HabitacionConfig hc = config.habitaciones.get(i); //Obtener la habitacion de indice i de config.habitaciones y darselo a hc (habitacion config)
            Habitacion hab = new Habitacion(hc.id, hc.filas, hc.columnas);  //Crear habitacion hab a partir de los datos de hc
            habitaciones.add(hab); //Añadir hab a la lista de habitaciones
        }

        for (int i = 0; i < config.habitaciones.size(); i++) { //--Rellenar celdas
            ConfigPartida.HabitacionConfig hc = config.habitaciones.get(i); //Obtener la habitacion de indice i de config
            Habitacion hab = habitaciones.get(i);                           //Y la habitacion de indice i de la lista de habitaciones
            if (hc.celdas != null) {
                for (int c = 0; c < hc.celdas.size(); c++) {
                    ConfigPartida.CeldaConfig cc = hc.celdas.get(c);     //cc almacena la config de cada celda
                    Celda celda = hab.getCelda(cc.x, cc.y);               //Se crea una celda con dichos datos
                    celda.setTipo(extraerTipo(cc.tipo));                //Otorgar tipo a cada celda
                    if (cc.objeto != null) celda.setObjeto(extraerObjetoConfig(cc.objeto));
                    if (cc.enemigo != null) { //Recrear enemigos
                        ConfigPartida.EnemigoConfig ec = cc.enemigo;
                        Enemigo enemigo = new Enemigo(ec.nombre, ec.vida, ec.vidaMax, ec.atq, ec.def, ec.vel, new Posicion(cc.x, cc.y));
                        celda.setEnemigo(enemigo);
                    }
                }
            }
            if (hc.puertas != null) {                                   //Habilitar puertas
                for (int p = 0; p < hc.puertas.size(); p++) {
                    ConfigPartida.PuertaConfig pc = hc.puertas.get(p); //pc almacena el valor de puertas de hc indice p
                    Celda celda = hab.getCelda(pc.x, pc.y);
                    celda.setIdHabitacionDestino(pc.idHabitacionDestino);
                }
            }
        }

        Mapa mapa = new Mapa();
        // Lista auxiliar para controlar habitaciones ya en el árbol
        Lista<Integer> idsConectados = new Lista<>();

        // Encontrar raíz
        for (int i = 0; i < config.habitaciones.size(); i++) {
            ConfigPartida.HabitacionConfig hc = config.habitaciones.get(i);
            Habitacion hab = habitaciones.get(i);
            if (hc.padreId == null) {
                mapa.setInicial(hab);
                idsConectados.add(hc.id);
            }
        }
        // Enlazar en orden
        int conectadas = idsConectados.size();
        int total = config.habitaciones.size();

        while (conectadas < total) {
            boolean algunaConectada = false;
            for (int i = 0; i < config.habitaciones.size(); i++) {
                ConfigPartida.HabitacionConfig hc = config.habitaciones.get(i);
                Habitacion hab = habitaciones.get(i);
                if (hc.padreId == null) continue;

                // Verificar si ya está conectada usando la lista auxiliar
                boolean yaConectada = false;
                for (int j = 0; j < idsConectados.size(); j++) {
                    if (idsConectados.get(j) == hc.id) {
                        yaConectada = true;
                        break;
                    }
                }
                if (yaConectada) continue;

                // Buscar el padre
                Habitacion padre = buscarHabitacionId(habitaciones, hc.padreId);
                if (padre == null) continue;

                // Verificar si el padre ya está conectado usando la lista auxiliar
                boolean padreConectado = false;
                for (int j = 0; j < idsConectados.size(); j++) {
                    if (idsConectados.get(j).equals(hc.padreId)) {
                        padreConectado = true;
                        break;
                    }
                }
                if (!padreConectado) continue;

                mapa.link(padre, hab);
                idsConectados.add(hc.id);
                conectadas++;
                algunaConectada = true;
            }

            if (!algunaConectada) break;

        }

        ConfigPartida.JugadorConfig jc = config.jugador; //--Recrear jugador
        Posicion posJugador = new Posicion(jc.xInicial, jc.yInicial);
        Jugador jugador = new Jugador(jc.nombre, jc.vida, jc.atq, jc.def, jc.vel, posJugador);
        mapa.goToId(jc.habitacion); //Colocar en la habitacion actual

        MotorJuego motor = new MotorJuego(mapa, jugador, config.turnosTotales); //--Crear motor de juego
        Habitacion habInicial = mapa.getActual();
        for (int i = 0; i < habInicial.getFilas(); i++) {
            for (int j = 0; j < habInicial.getColumnas(); j++) {
                Celda celda = habInicial.getCelda(i, j);
                if (celda.tieneEnemigo()) motor.addEnemigo(celda.getEnemigo());
            }
        }
        return motor;
    }

    public void guardarPartida(MotorJuego motor, String rutaArchivo) throws IOException {
        EstadoPartida estado = extraerEstado(motor);
        FileWriter writer = new FileWriter(rutaArchivo); //Crea archivo de escritura en la ruta dada
        gson.toJson(estado, writer); //Convierte estado a Json
        writer.close();
    }

    private EstadoPartida extraerEstado(MotorJuego motor) {
        EstadoPartida estado = new EstadoPartida();
        estado.turnosRestantes = motor.getTurnosRestantes();
        estado.partidaTerminada = motor.esPartidaTerminada();
        estado.victoria = motor.esVictoria();

        Jugador jugador = motor.getJugador();
        estado.jugador = new EstadoPartida.JugadorEstado();
        estado.jugador.nombre = jugador.getNombre();
        estado.jugador.vida = jugador.getVida();
        estado.jugador.vidaMax = jugador.getVidaMax();
        estado.jugador.atq = jugador.getAtq();
        estado.jugador.def = jugador.getDef();
        estado.jugador.vel = jugador.getVel();
        estado.jugador.habitacionActualId = motor.getMapa().getActual().getId();
        estado.jugador.x = jugador.getPosicion().getX();
        estado.jugador.y = jugador.getPosicion().getY();

        // Inventario
        estado.jugador.inventario = new Lista<>();
        ListaDoble<Objeto> objetos = jugador.getInventario().getTodos();
        for (int i = 0; i < objetos.getSize(); i++) {
            estado.jugador.inventario.add(convertirObjetoEstado(objetos.get(i)));
        }

        // Enemigos vivos
        estado.enemigos = new Lista<>();
        Lista<Enemigo> enemigos = motor.getEnemigosVivos();
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigo = enemigos.get(i);
            EstadoPartida.EnemigoEstado ee = new EstadoPartida.EnemigoEstado();
            ee.nombre = enemigo.getNombre();
            ee.vida = enemigo.getVida();
            ee.vidaMax = enemigo.getVidaMax();
            ee.atq = enemigo.getAtq();
            ee.def = enemigo.getDef();
            ee.vel = enemigo.getVel();
            ee.habitacionId = motor.getMapa().getActual().getId();
            ee.x = enemigo.getPosicion().getX();
            ee.y = enemigo.getPosicion().getY();
            estado.enemigos.add(ee);
        }
        estado.celdasModificadas = new Lista<>();
        Lista<Habitacion> todasHab = motor.getMapa().getAllHabitaciones();
        for (int h = 0; h < todasHab.size(); h++) { //Por cada habitacion
            Habitacion hab = todasHab.get(h);
            for (int i = 0; i < hab.getFilas(); i++) {  //Recorrer cada fila
                for (int j = 0; j < hab.getColumnas(); j++) {   //Recorrer cada celda
                    Celda celda = hab.getCelda(i, j);
                    EstadoPartida.CeldaEstado ce = new EstadoPartida.CeldaEstado();
                    ce.habitacionId = hab.getId();
                    ce.x = i;
                    ce.y = j;
                    ce.tipo = celda.getTipo().name();   //.name() da el identificador del tipo, sirve para estandarizar
                    ce.tieneObjeto = celda.tieneObjeto();
                    if (celda.tieneObjeto()) {
                        ce.objeto = convertirObjetoEstado(celda.getObjeto());
                    }
                    ce.tieneEnemigo = celda.tieneEnemigo();
                    if (celda.tieneEnemigo()) {
                        Enemigo e = celda.getEnemigo();
                        ce.enemigo = new EstadoPartida.EnemigoEstado();
                        ce.enemigo.nombre = e.getNombre();
                        ce.enemigo.vida = e.getVida();
                        ce.enemigo.vidaMax = e.getVidaMax();
                        ce.enemigo.atq = e.getAtq();
                        ce.enemigo.def = e.getDef();
                        ce.enemigo.vel = e.getVel();
                        ce.enemigo.habitacionId = hab.getId();
                        ce.enemigo.x = i;
                        ce.enemigo.y = j;
                    }
                    estado.celdasModificadas.add(ce);
                }
            }
        }
        return estado;
    }

    public MotorJuego cargarPartida(String rutaArchivo, String rutaConfig) throws IOException {
        MotorJuego motor = cargarConfig(rutaConfig);
        FileReader reader = new FileReader(rutaArchivo);
        EstadoPartida estado = gson.fromJson(reader, EstadoPartida.class);
        reader.close();
        aplicarEstado(motor, estado);
        return motor;
    }

    private void aplicarEstado(MotorJuego motor, EstadoPartida estado) {
        motor.setTurnosRestantes(estado.turnosRestantes);
        motor.setPartidaTerminada(estado.partidaTerminada);
        motor.setVictoria(estado.victoria);

        motor.getMapa().goToId(estado.jugador.habitacionActualId);
        motor.getJugador().setPosicion(new Posicion(estado.jugador.x, estado.jugador.y));
        motor.getJugador().setVidaMax(estado.jugador.vidaMax);
        motor.getJugador().setVida(estado.jugador.vida);
        motor.getJugador().setAtq(estado.jugador.atq);
        motor.getJugador().setDef(estado.jugador.def);
        motor.getJugador().setVel(estado.jugador.vel);

        motor.getJugador().getInventario().vaciar();
        for (int i = 0; i < estado.jugador.inventario.size(); i++) {
            EstadoPartida.ObjetoEstado oe = estado.jugador.inventario.get(i);
            Objeto objeto = extraerObjetoEstado(oe);
            motor.getJugador().getInventario().cogerObjeto(objeto); //cogerObjeto añade objetos
        }
        Lista<Habitacion> todasHab = motor.getMapa().getAllHabitaciones(); //Vaciar todas las casillas primero, para reconstruir despues sin restos
        for (int h = 0; h < todasHab.size(); h++) {
            Habitacion hab = todasHab.get(h);
            for (int i = 0; i < hab.getFilas(); i++) {
                for (int j = 0; j < hab.getColumnas(); j++) {
                    Celda celda = hab.getCelda(i, j);
                    celda.setTipo(Celda.TipoCelda.Vacia);
                    celda.setObjeto(null);
                    celda.setEnemigo(null);
                }
            }
        }
        for (int c = 0; c < estado.celdasModificadas.size(); c++) { //Reconstruir con las celdas que hayan cambiado
            EstadoPartida.CeldaEstado ce = estado.celdasModificadas.get(c);
            Habitacion hab = buscarHabitacionId(todasHab, ce.habitacionId);
            if (hab == null) continue;

            Celda celda = hab.getCelda(ce.x, ce.y);
            celda.setTipo(extraerTipo(ce.tipo));
            if (ce.tieneObjeto && ce.objeto != null) {
                celda.setObjeto(extraerObjetoEstado(ce.objeto));
            }
            if (ce.tieneEnemigo && ce.enemigo != null) {
                Enemigo enemigo = new Enemigo(
                        ce.enemigo.nombre,
                        ce.enemigo.vida,
                        ce.enemigo.vidaMax,
                        ce.enemigo.atq,
                        ce.enemigo.def,
                        ce.enemigo.vel,
                        new Posicion(ce.x, ce.y)
                );
                celda.setEnemigo(enemigo);
            }
        }
        motor.limpiarEnemigos();    //Eliminar los enemigos que tiene motor y restaurar con los creados en celda.setEnemigo(enemigo)
        Habitacion habActual = motor.getMapa().getActual();
        for (int i = 0; i < habActual.getFilas(); i++) {
            for (int j = 0; j < habActual.getColumnas(); j++) {
                Celda celda = habActual.getCelda(i, j);
                if (celda.tieneEnemigo()) {
                    motor.addEnemigo(celda.getEnemigo());
                }
            }
        }
    }

    private Objeto extraerObjetoEstado(EstadoPartida.ObjetoEstado oe) {
        return crearObjeto(oe.nombre, oe.desc, oe.tipo, oe.valor, oe.equipable, oe.consumible);
    }

    private Objeto extraerObjetoConfig(ConfigPartida.ObjetoConfig oc) {
        return crearObjeto(oc.nombre, oc.desc, oc.tipo, oc.valor, oc.equipable, oc.consumible);
    }

    private Objeto crearObjeto(String nombre, String desc, String tipo, int valor, boolean equipable, boolean consumible) {
        return switch (tipo.toUpperCase()) {
            case "POCION" -> new Pocion(nombre, desc, valor, equipable, consumible);
            case "ARMA" -> new Arma(nombre, desc, valor, equipable, consumible);
            case "ESCUDO" -> new Escudo(nombre, desc, valor, equipable, consumible);
            default ->

                    new Objeto(nombre, desc, equipable, consumible) {// Objeto genérico sin efecto especial
                        @Override
                        public void usar(Jugador jugador) {}// Sin efecto
                    };
            };
    }
    private Celda.TipoCelda extraerTipo (String tipo) {
        if (tipo==null) return Celda.TipoCelda.Vacia;
        return switch (tipo.toUpperCase()) {
            case "PARED" -> Celda.TipoCelda.Pared;
            case "PUERTA" -> Celda.TipoCelda.Puerta;
            case "TRAMPA" -> Celda.TipoCelda.Trampa;
            default -> Celda.TipoCelda.Vacia;
        };
    }
    private EstadoPartida.ObjetoEstado convertirObjetoEstado(Objeto objeto){
        EstadoPartida.ObjetoEstado oe = new EstadoPartida.ObjetoEstado();
        oe.nombre = objeto.getNombre();
        oe.desc = objeto.getDesc();
        oe.equipable = objeto.esEquipable();
        oe.consumible = objeto.esConsumible();

        if (objeto instanceof Pocion) {// Determinar tipo y valor según la subclase
            oe.tipo = "POCION";
            oe.valor = ((Pocion) objeto).getHeal();
        } else if (objeto instanceof Arma) {
            oe.tipo = "ARMA";
            oe.valor = ((Arma) objeto).getBonusAtq();
        } else if (objeto instanceof Escudo) {
            oe.tipo = "ESCUDO";
            oe.valor = ((Escudo) objeto).getBonusDef();
        } else {
            oe.tipo = "GENERICO";
            oe.valor = 0;
        }
        return oe;
    }

    private Habitacion buscarHabitacionId(Lista<Habitacion> habitaciones, int id) {
        for (int i = 0; i < habitaciones.size(); i++) {
            if (habitaciones.get(i).getId()== id) {
                return habitaciones.get(i);
            }
        }
        return null;
    }

    public Lista<String> listarNiveles(String carpetaNiveles) { //Para el selector de niveles
        Lista<String> niveles = new Lista<>();

        java.io.File carpeta = new java.io.File(carpetaNiveles);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            return niveles;
        }
        java.io.File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (java.io.File archivo : archivos) {
                if (archivo.getName().endsWith(".json")) {
                    niveles.add(archivo.getName());
                }
            }
        }
        return niveles;
    }
}
