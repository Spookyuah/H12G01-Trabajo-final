package Juego;

import Juego.Modelo.*;
import Juego.Modelo.Objetos.*;
import Juego.Motor.*;
import Juego.Persistencia.*;
import Structures.Lista;

import java.io.IOException;

/**
 * Tester por consola para probar el juego sin interfaz gráfica.
 */
public class TesterJuego {

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  TESTER DEL MOTOR DE JUEGO");
        System.out.println("=========================================\n");

        TesterJuego tester = new TesterJuego();

        // Prueba 1: Cargar configuración
        System.out.println(">>> PRUEBA 1: Cargar configuración desde JSON");
        MotorJuego motor = tester.probarCargaConfiguracion();
        if (motor == null) {
            System.out.println("ERROR: No se pudo cargar la configuración. Abortando.");
            return;
        }
        System.out.println();

        // Prueba 2: Mostrar estado inicial
        System.out.println(">>> PRUEBA 2: Estado inicial del juego");
        tester.mostrarEstadoCompleto(motor);
        System.out.println();

        // Prueba 3: Ejecutar varios turnos automáticos
        System.out.println(">>> PRUEBA 3: Ejecutar 5 turnos automáticos");
        tester.ejecutarTurnosAutomaticos(motor, 5);
        System.out.println();

        // Prueba 4: Guardar partida
        System.out.println(">>> PRUEBA 4: Guardar partida");
        tester.probarGuardarPartida(motor);
        System.out.println();

        // Prueba 5: Cargar partida guardada
        System.out.println(">>> PRUEBA 5: Cargar partida guardada");
        MotorJuego motorCargado = tester.probarCargarPartida();
        if (motorCargado != null) {
            System.out.println("Partida cargada correctamente.");
            tester.mostrarEstadoResumido(motorCargado);
        }
        System.out.println();

        // Prueba 6: Probar acciones específicas del jugador
        System.out.println(">>> PRUEBA 6: Probar acciones del jugador");
        tester.probarAccionesJugador(motor);
        System.out.println();

        System.out.println("=========================================");
        System.out.println("  TODOS LOS TESTS COMPLETADOS");
        System.out.println("=========================================");
    }

    // ============================================================
    // PRUEBAS
    // ============================================================

    private MotorJuego probarCargaConfiguracion() {
        try {
            GestorJSON gestor = new GestorJSON();
            MotorJuego motor = gestor.cargarConfig("resources/Config_partidaTest.json");
            System.out.println("  ✅ Configuración cargada correctamente.");
            System.out.println("  Habitaciones: " + motor.getMapa().getNumHabitaciones());
            System.out.println("  Habitación actual: " + motor.getMapa().getActual());
            System.out.println("  Jugador: " + motor.getJugador());
            System.out.println("  Turnos restantes: " + motor.getTurnosRestantes());
            return motor;
        } catch (IOException e) {
            System.out.println("  ❌ Error al cargar configuración: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void ejecutarTurnosAutomaticos(MotorJuego motor, int numTurnos) {
        for (int t = 1; t <= numTurnos; t++) {
            if (motor.esPartidaTerminada()) {
                System.out.println("  La partida terminó en el turno " + (t - 1));
                if (motor.esVictoria()) {
                    System.out.println("  ¡VICTORIA!");
                } else {
                    System.out.println("  DERROTA.");
                }
                break;
            }

            System.out.println("  --- Turno " + t + " ---");

            // Decidir acción automática según la situación
            AccionJugador accion = decidirAccionAutomatica(motor);
            System.out.println("  Acción: " + accion);

            // Ejecutar turno del jugador (los enemigos se ejecutan dentro)
            motor.ejecutarTurnoJugador(accion);

            // Mostrar estado resumido
            mostrarEstadoResumido(motor);

            // Mostrar log de este turno
            mostrarUltimoLog(motor);

            System.out.println();
        }
    }

    /**
     * Decide una acción automática simple para el jugador.
     * Prioridad:
     *   1. Atacar enemigo adyacente.
     *   2. Recoger objeto adyacente.
     *   3. Abrir puerta adyacente.
     *   4. Moverse hacia una dirección con contenido o aleatoria.
     */
    private AccionJugador decidirAccionAutomatica(MotorJuego motor) {
        Habitacion hab = motor.getMapa().getActual();
        Posicion posJugador = motor.getJugador().getPosicion();
        Lista<Posicion> alcances = motor.getMovimientosPosibles();

        // Buscar en las 4 direcciones adyacentes
        String[] direcciones = {"ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"};
        for (String dir : direcciones) {
            Posicion adyacente = calcularAdyacente(posJugador, dir);
            if (adyacente != null && hab.esPosicionValida(adyacente)) {
                Celda celda = hab.getCelda(adyacente);

                // Prioridad 1: Atacar enemigo
                if (celda.tieneEnemigo()) {
                    return AccionJugador.atacar(dir);
                }
            }
        }

        for (String dir : direcciones) {
            Posicion adyacente = calcularAdyacente(posJugador, dir);
            if (adyacente != null && hab.esPosicionValida(adyacente)) {
                Celda celda = hab.getCelda(adyacente);

                // Prioridad 2: Abrir puerta
                if (celda.esPuerta()) {
                    return AccionJugador.abrir(dir);
                }

                // Prioridad 3: Recoger objeto
                if (celda.tieneObjeto()) {
                    return AccionJugador.recoger(dir);
                }
            }
        }

        // Prioridad 4: Usar objeto del inventario si hay (poción de vida si vida < 50%)
        if (!motor.getJugador().getInventario().estaVacio()) {
            Inventario inv = motor.getJugador().getInventario();
            for (int i = 0; i < inv.getCantidad(); i++) {
                Objeto obj = inv.getObjeto(i);
                if (obj instanceof Pocion && motor.getJugador().getVida() < motor.getJugador().getVidaMax() / 2) {
                    // Moverse a una celda segura y usar poción
                    if (alcances.size() > 1) {
                        Posicion destino = alcances.get(1); // La primera después del origen
                        return AccionJugador.moverYUsar(destino, i);
                    } else {
                        return AccionJugador.usar(i);
                    }
                }
            }
        }

        // Prioridad 5: Moverse a una celda aleatoria entre las alcanzables
        if (alcances.size() > 1) {
            int indiceAleatorio = 1 + (int)(Math.random() * (alcances.size() - 1));
            Posicion destino = alcances.get(indiceAleatorio);
            return AccionJugador.mover(destino);
        }

        // Sin opciones: pasar turno
        return AccionJugador.nada();
    }

    private Posicion calcularAdyacente(Posicion origen, String direccion) {
        int x = origen.getX();
        int y = origen.getY();
        switch (direccion.toUpperCase()) {
            case "ARRIBA":    return new Posicion(x - 1, y);
            case "ABAJO":     return new Posicion(x + 1, y);
            case "IZQUIERDA": return new Posicion(x, y - 1);
            case "DERECHA":   return new Posicion(x, y + 1);
            default:          return null;
        }
    }

    private void probarAccionesJugador(MotorJuego motor) {
        if (motor.esPartidaTerminada()) {
            System.out.println("  La partida ya terminó.");
            return;
        }

        // Mostrar inventario
        System.out.println("  " + motor.getJugador().getInventario());

        // Si hay objetos en el inventario, usar el primero
        if (!motor.getJugador().getInventario().estaVacio()) {
            System.out.println("  Probando usar objeto del inventario (índice 0)...");
            motor.ejecutarTurnoJugador(AccionJugador.usar(0));
            mostrarEstadoResumido(motor);
        } else {
            System.out.println("  Inventario vacío. Moviendo a celda aleatoria...");
            Lista<Posicion> alcances = motor.getMovimientosPosibles();
            if (alcances.size() > 1) {
                Posicion destino = alcances.get(1);
                motor.ejecutarTurnoJugador(AccionJugador.mover(destino));
                mostrarEstadoResumido(motor);
            }
        }
    }

    private void probarGuardarPartida(MotorJuego motor) {
        try {
            GestorJSON gestor = new GestorJSON();
            gestor.guardarPartida(motor, "resources/partida_guardada.json");
            System.out.println("  ✅ Partida guardada en 'resources/partida_guardada.json'");
        } catch (IOException e) {
            System.out.println("  ❌ Error al guardar partida: " + e.getMessage());
        }
    }

    private MotorJuego probarCargarPartida() {
        try {
            GestorJSON gestor = new GestorJSON();
            MotorJuego motor = gestor.cargarPartida("resources/partida_guardada.json", "resources/Config_partidaTest.json");
            System.out.println("  ✅ Partida cargada correctamente.");
            return motor;
        } catch (IOException e) {
            System.out.println("  ❌ Error al cargar partida: " + e.getMessage());
            return null;
        }
    }

    // ============================================================
    // VISUALIZACIÓN
    // ============================================================

    private void mostrarEstadoCompleto(MotorJuego motor) {
        mostrarEstadoResumido(motor);
        System.out.println();
        mostrarTablero(motor);
    }

    private void mostrarEstadoResumido(MotorJuego motor) {
        Jugador j = motor.getJugador();
        System.out.println("  ┌─────────────────────────────────────────┐");
        System.out.println("  │ Jugador: " + String.format("%-31s", j.getNombre()) + "│");
        System.out.println("  │ Vida: " + String.format("%-3d", j.getVida()) + "/" + String.format("%-3d", j.getVidaMax())
                + "  Atq: " + String.format("%-3d", j.getAtq())
                + "  Def: " + String.format("%-3d", j.getDef())
                + "  Vel: " + String.format("%-2d", j.getVel()) + "  │");
        System.out.println("  │ Pos: " + String.format("%-7s", j.getPosicion())
                + "  Turnos: " + String.format("%-3d", motor.getTurnosRestantes())
                + "  Hab: " + String.format("%-2d", motor.getMapa().getActual().getId()) + "      │");
        System.out.println("  │ Enemigos vivos: " + String.format("%-2d", motor.getEnemigosVivos().size())
                + "  Terminada: " + String.format("%-5s", motor.esPartidaTerminada())
                + "  Victoria: " + String.format("%-5s", motor.esVictoria()) + " │");
        System.out.println("  └─────────────────────────────────────────┘");
    }

    private void mostrarTablero(MotorJuego motor) {
        Habitacion hab = motor.getMapa().getActual();
        Lista<Posicion> alcances = motor.getMovimientosPosibles();

        System.out.println("  Tablero de " + hab + " (• = alcanzable):");
        System.out.print("    ");
        for (int j = 0; j < hab.getColumnas(); j++) {
            System.out.print("  " + j + " ");
        }
        System.out.println();

        System.out.print("    +");
        for (int j = 0; j < hab.getColumnas(); j++) {
            System.out.print("---+");
        }
        System.out.println();

        for (int i = 0; i < hab.getFilas(); i++) {
            System.out.print("  " + i + " ");
            for (int j = 0; j < hab.getColumnas(); j++) {
                Celda celda = hab.getCelda(i, j);
                String simbolo = obtenerSimboloCelda(celda);

                // Marcar si el jugador está en esta celda
                if (motor.getJugador().getPosicion().equals(new Posicion(i, j))) {
                    simbolo = "J";
                }

                // Marcar si es alcanzable
                boolean esAlcanzable = false;
                for (int a = 0; a < alcances.size(); a++) {
                    if (alcances.get(a).equals(new Posicion(i, j))) {
                        esAlcanzable = true;
                        break;
                    }
                }
                String prefijo = esAlcanzable ? "•" : " ";

                System.out.print("|" + prefijo + simbolo + " ");
            }
            System.out.println("|");

            System.out.print("    +");
            for (int j = 0; j < hab.getColumnas(); j++) {
                System.out.print("---+");
            }
            System.out.println();
        }
        System.out.println("    J = Jugador  E = Enemigo  O = Objeto");
        System.out.println("    P = Puerta   T = Trampa   # = Pared   • = Alcanzable");
    }

    private String obtenerSimboloCelda(Celda celda) {
        if (celda.tieneEnemigo()) return "E";
        if (celda.tieneObjeto())  return "O";
        switch (celda.getTipo()) {
            case Pared:  return "#";
            case Puerta: return "P";
            case Trampa: return "T";
            default:     return " ";
        }
    }

    //Muestra las últimas entradas del log del juego.
    private void mostrarUltimoLog(MotorJuego motor) {
        Lista<String> log = motor.getLog();
        System.out.println("  ┌─ Log del turno ─────────────────────────┐");
        // Mostrar solo las últimas 5 entradas
        int inicio = Math.max(0, log.size() - 5);
        for (int i = inicio; i < log.size(); i++) {
            System.out.println("  │ " + log.get(i));
        }
        System.out.println("  └──────────────────────────────────────────┘");
    }
}