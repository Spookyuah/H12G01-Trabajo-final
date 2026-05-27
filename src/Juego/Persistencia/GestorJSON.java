package Juego.Persistencia;

import Structures.Lista;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Juego.Modelo.*;
import Juego.Motor.MotorJuego;

import java.io.FileReader;
import java.io.IOException;

public class GestorJSON {
    private Gson gson;
    public GestorJSON() {
        gson = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printing lo hace legible
    }
    public MotorJuego cargarConfig(String rutaArchivo) throws IOException { // Input-Output exception
        FileReader reader = new FileReader(rutaArchivo);
        ConfigPartida config =gson.fromJson(reader, ConfigPartida.class);
        reader.close();
        return construirMotorJuego(config);

    }

    private MotorJuego construirMotorJuego(ConfigPartida config){
        Lista<Habitacion> habitaciones = new Lista<>();
        for (int i = 0; i < config.habitaciones.size(); i++) { //--Recrear habitaciones
            ConfigPartida.HabitacionConfig hc= config.habitaciones.get(i); //Obtener la habitacion de indice i de config.habitaciones y darselo a hc (habitacion config)
            Habitacion hab= new Habitacion(hc.id, hc.filas, hc.columnas);  //Crear habitacion hab a partir de los datos de hc
            habitaciones.add(hab); //Añadir hab a la lista de habitaciones
        }

        for (int i = 0; i < config.habitaciones.size(); i++) { //--Rellenar celdas
            ConfigPartida.HabitacionConfig hc= config.habitaciones.get(i); //Obtener la habitacion de indice i de config
            Habitacion hab= habitaciones.get(i);                           //Y la habitacion de indice i de la lista de habitaciones
            if (hc.celdas!=null) {
                for (int c = 0; c < hc.celdas.size(); c++) {
                    ConfigPartida.CeldaConfig cc= hc.celdas.get(c);     //cc almacena la config de cada celda
                    Celda celda= hab.getCelda(cc.x,cc.y);               //Se crea una celda con dichos datos
                    celda.setTipo(extraerTipo(cc.tipo));                //Otorgar tipo a cada celda
                    if (cc.objeto!=null) celda.setObjeto(extraerObjeto(cc.objeto));
                    if (cc.enemigo!=null) { //Recrear enemigos
                        ConfigPartida.EnemigoConfig ec= cc.enemigo;
                        Enemigo enemigo =new Enemigo(ec.nombre, ec.vida, ec.vidaMax, ec.atq, ec.def, ec.vel, new Posicion(ec.xInicial, ec.yInicial));
                        celda.setEnemigo(enemigo);
                    }
                }
            }
            if (hc.puertas != null) {                                   //Habilitar puertas
                for (int p = 0; p < hc.puertas.size(); p++) {
                    ConfigPartida.PuertaConfig pc= hc.puertas.get(p); //pc almacena el valor de puertas de hc indice p
                    Celda celda= hab.getCelda(pc.x,pc.y);
                    celda.setIdHabitacionDestino(pc.idDestino);
                }
            }
        }

        Mapa mapa=new Mapa();   //--Recrear mapa
        for (int i = 0; i < config.habitaciones.size(); i++) { //for para encontrar raiz
            ConfigPartida.HabitacionConfig hc= config.habitaciones.get(i);
            Habitacion hab= habitaciones.get(i);
            if (hc.padreId==null) mapa.setInicial(hab); //padreId==null significa nodo raiz
        }
        for (int i = 0; i < config.habitaciones.size(); i++) { //for para enlazar las habitaciones
            ConfigPartida.HabitacionConfig hc= config.habitaciones.get(i);
            Habitacion hab= habitaciones.get(i);
            if (hc.padreId != null) {
                Habitacion padre = buscarHabitacionId(habitaciones, hc.padreId);
                if (padre != null) mapa.link(padre, hab);
            }
        }
        ConfigPartida.JugadorConfig jc= config.jugador; //--Recrear jugador
        Posicion posJugador= new Posicion(jc.xInicial, jc.yInicial);
        Jugador jugador= new Jugador(jc.nombre, jc.vida, jc.vidaMax, jc.ataque, jc.defensa, jc.velocidad, posJugador);
        mapa.goToId(jc.habitacion); //Colocar en la habitacion actual

        MotorJuego motor= new MotorJuego(mapa, jugador, config.turnosTotales); //--Crear motor de juego
        Habitacion habInicial= mapa.getActual();
        for (int i = 0; i < habInicial.getFilas(); i++) {
            for (int j= 0; j < habInicial.getColumnas(); j++) {
                Celda celda= habInicial.getCelda(i,j);
                if (celda.tieneEnemigo()) motor.addEnemigo(celda.getEnemigo());
            }
        }
        return motor;
    }

    public void guardarPartida(MotorJuego motor, String rutaArchivo) {

    }



    //todo private tipo extraerTipo(){}
    //todo private Objeto extraerObjeto(){}
    //todo private Habitacion buscarHabitacionId(ListaEnlazada<Habitacion> habitaciones, int id) {

}
