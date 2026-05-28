package Juego.Persistencia;

import Juego.Modelo.*;
import Juego.Modelo.Objetos.*;
import Juego.Motor.*;
import Structures.Lista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

/**
 * Tests unitarios para la clase GestorJSON.
 * Requiere archivos JSON de prueba en resources/.
 */
public class GestorJSONTest { // Cubre ListaAdapter tambien

    private GestorJSON gestor;
    private static final String RUTA_CONFIG_TEST = "resources/Config_partidaTest.json";
    private static final String RUTA_PARTIDA_TEST = "resources/test_partida_guardada.json";

    @BeforeEach
    public void setUp() {
        gestor = new GestorJSON();
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructor() {
        assertNotNull(gestor);
    }

    // ============================================================
    // TESTS DE CARGAR CONFIGURACIÓN
    // ============================================================

    @Test
    public void testCargarConfig() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        assertNotNull(motor);
        assertNotNull(motor.getMapa());
        assertNotNull(motor.getJugador());
        assertFalse(motor.esPartidaTerminada());
        assertFalse(motor.esVictoria());
    }

    @Test
    public void testCargarConfigJugador() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        Jugador jugador = motor.getJugador();
        assertNotNull(jugador);
        assertNotNull(jugador.getNombre());
        assertTrue(jugador.getVida() > 0);
        assertTrue(jugador.getVidaMax() > 0);
        assertTrue(jugador.getAtq() >= 0);
        assertTrue(jugador.getDef() >= 0);
        assertTrue(jugador.getVel() >= 0);
        assertNotNull(jugador.getPosicion());
        assertNotNull(jugador.getInventario());
    }

    @Test
    public void testCargarConfigMapa() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        Mapa mapa = motor.getMapa();
        assertNotNull(mapa);
        assertTrue(mapa.getNumHabitaciones() >= 1);
        assertNotNull(mapa.getActual());
    }

    @Test
    public void testCargarConfigHabitaciones() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        assertTrue(motor.getMapa().getNumHabitaciones() >= 1);
        assertNotNull(motor.getMapa().getAllHabitaciones());
        assertFalse(motor.getMapa().getAllHabitaciones().isEmpty());
    }

    @Test
    public void testCargarConfigTurnos() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        assertTrue(motor.getTurnosRestantes() > 0);
    }

    @Test
    public void testCargarConfigHabitacionInicialValida() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        Habitacion actual = motor.getMapa().getActual();
        assertNotNull(actual);
        assertTrue(actual.getFilas() > 0);
        assertTrue(actual.getColumnas() > 0);
        // El jugador debe estar en una posición válida
        assertTrue(actual.esPosicionValida(motor.getJugador().getPosicion()));
    }

    @Test
    public void testCargarConfigArchivoNoExiste() {
        assertThrows(IOException.class, () -> {
            gestor.cargarConfig("resources/no_existe.json");
        });
    }

    // ============================================================
    // TESTS DE GUARDAR PARTIDA
    // ============================================================

    @Test
    public void testGuardarPartida() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        // Mover al jugador para cambiar el estado
        Lista<Posicion> alcances = motor.getMovimientosPosibles();
        if (alcances.size() > 1) {
            motor.ejecutarTurnoJugador(AccionJugador.mover(alcances.get(1)));
        }

        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        // Verificar que el archivo se creó
        java.io.File archivo = new java.io.File(RUTA_PARTIDA_TEST);
        assertTrue(archivo.exists());
    }

    @Test
    public void testGuardarPartidaNoLanzaExcepcion() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        assertDoesNotThrow(() -> {
            gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);
        });
    }

    // ============================================================
    // TESTS DE CARGAR PARTIDA GUARDADA
    // ============================================================

    @Test
    public void testCargarPartida() throws IOException {
        // Primero guardar una partida
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        Lista<Posicion> alcances = motor.getMovimientosPosibles();
        if (alcances.size() > 1) {
            motor.ejecutarTurnoJugador(AccionJugador.mover(alcances.get(1)));
        }
        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        // Cargar la partida guardada
        MotorJuego motorCargado = gestor.cargarPartida(RUTA_PARTIDA_TEST, RUTA_CONFIG_TEST);

        assertNotNull(motorCargado);
        assertNotNull(motorCargado.getJugador());
        assertNotNull(motorCargado.getMapa());
    }

    @Test
    public void testCargarPartidaMantienePosicion() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        Lista<Posicion> alcances = motor.getMovimientosPosibles();
        Posicion destino = null;
        if (alcances.size() > 1) {
            destino = alcances.get(1);
            motor.ejecutarTurnoJugador(AccionJugador.mover(destino));
        }
        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        MotorJuego motorCargado = gestor.cargarPartida(RUTA_PARTIDA_TEST, RUTA_CONFIG_TEST);

        if (destino != null) {
            assertEquals(destino, motorCargado.getJugador().getPosicion());
        }
    }

    @Test
    public void testCargarPartidaMantieneTurnos() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        motor.ejecutarTurnoJugador(AccionJugador.nada());
        int turnosAntesDeGuardar = motor.getTurnosRestantes();

        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        MotorJuego motorCargado = gestor.cargarPartida(RUTA_PARTIDA_TEST, RUTA_CONFIG_TEST);

        assertEquals(turnosAntesDeGuardar, motorCargado.getTurnosRestantes());
    }

    @Test
    public void testCargarPartidaMantieneVida() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        // Añadir un enemigo y recibir daño
        Enemigo goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 1));
        motor.addEnemigo(goblin);
        motor.getJugador().takeDmg(25);
        int vidaAntesDeGuardar = motor.getJugador().getVida();

        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        MotorJuego motorCargado = gestor.cargarPartida(RUTA_PARTIDA_TEST, RUTA_CONFIG_TEST);

        assertEquals(vidaAntesDeGuardar, motorCargado.getJugador().getVida());
    }

    @Test
    public void testCargarPartidaArchivoNoExiste() {
        assertThrows(IOException.class, () -> {
            gestor.cargarPartida("resources/no_existe.json", RUTA_CONFIG_TEST);
        });
    }

    // ============================================================
    // TESTS DE LISTAR NIVELES
    // ============================================================

    @Test
    public void testListarNiveles() {
        Lista<String> niveles = gestor.listarNiveles("resources/niveles");

        assertNotNull(niveles);
        // Si la carpeta existe y tiene archivos .json, debería devolver algo
        // Si no existe, devuelve lista vacía
    }

    @Test
    public void testListarNivelesCarpetaNoExiste() {
        Lista<String> niveles = gestor.listarNiveles("resources/carpeta_que_no_existe");

        assertNotNull(niveles);
        assertEquals(0, niveles.size());
    }

    // ============================================================
    // TESTS DE CONVERSIÓN DE TIPOS
    // ============================================================

    @Test
    public void testExtraerTipoVacia() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);
        Habitacion hab = motor.getMapa().getActual();

        // Debería haber al menos una celda VACIA
        boolean encontrada = false;
        for (int i = 0; i < hab.getFilas(); i++) {
            for (int j = 0; j < hab.getColumnas(); j++) {
                if (hab.getCelda(i, j).getTipo() == Celda.TipoCelda.Vacia) {
                    encontrada = true;
                    break;
                }
            }
        }
        assertTrue(encontrada);
    }

    @Test
    public void testExtraerObjetoPocion() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);
        Habitacion hab = motor.getMapa().getActual();

        // Buscar si hay algún objeto en la habitación inicial
        boolean tienePocion = false;
        for (int i = 0; i < hab.getFilas(); i++) {
            for (int j = 0; j < hab.getColumnas(); j++) {
                if (hab.getCelda(i, j).tieneObjeto()) {
                    Objeto obj = hab.getCelda(i, j).getObjeto();
                    if (obj instanceof Pocion) {
                        tienePocion = true;
                        assertTrue(((Pocion) obj).getHeal() > 0);
                    }
                }
            }
        }
        // Puede que no haya poción en la habitación inicial
    }

    // ============================================================
    // TESTS DE INTEGRACIÓN
    // ============================================================

    @Test
    public void testCicloCompletoGuardarYCargar() throws IOException {
        // Cargar configuración
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        // Realizar varias acciones
        motor.ejecutarTurnoJugador(AccionJugador.nada());
        motor.ejecutarTurnoJugador(AccionJugador.nada());
        motor.ejecutarTurnoJugador(AccionJugador.nada());

        int turnosEsperados = motor.getTurnosRestantes();
        Posicion posEsperada = motor.getJugador().getPosicion();
        int vidaEsperada = motor.getJugador().getVida();

        // Guardar
        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        // Cargar
        MotorJuego motorCargado = gestor.cargarPartida(RUTA_PARTIDA_TEST, RUTA_CONFIG_TEST);

        // Verificar
        assertEquals(turnosEsperados, motorCargado.getTurnosRestantes());
        assertEquals(posEsperada, motorCargado.getJugador().getPosicion());
        assertEquals(vidaEsperada, motorCargado.getJugador().getVida());
        assertFalse(motorCargado.esPartidaTerminada());
        assertFalse(motorCargado.esVictoria());
    }

    @Test
    public void testGuardarPartidaConInventario() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        // Añadir objetos al inventario
        motor.getJugador().getInventario().cogerObjeto(
                new Pocion("Poción test", "Cura 20", 20, false, true)
        );
        motor.getJugador().getInventario().cogerObjeto(
                new Arma("Espada test", "Ataque +5", 5, true, false)
        );

        gestor.guardarPartida(motor, RUTA_PARTIDA_TEST);

        MotorJuego motorCargado = gestor.cargarPartida(RUTA_PARTIDA_TEST, RUTA_CONFIG_TEST);

        assertEquals(2, motorCargado.getJugador().getInventario().getCantidad());
    }

    @Test
    public void testCargarConfigDiferentesMapas() throws IOException {
        MotorJuego motorEasy = gestor.cargarConfig("resources/niveles/Dungeon_Easy.json");

        assertNotNull(motorEasy);
        assertTrue(motorEasy.getMapa().getNumHabitaciones() >= 1);
    }

    @Test
    public void testMotorJuegoFuncionalDespuesDeCargar() throws IOException {
        MotorJuego motor = gestor.cargarConfig(RUTA_CONFIG_TEST);

        // Verificar que se puede jugar normalmente
        Lista<Posicion> movimientos = motor.getMovimientosPosibles();
        assertNotNull(movimientos);
        assertFalse(movimientos.isEmpty());

        motor.ejecutarTurnoJugador(AccionJugador.nada());
        assertTrue(motor.getTurnosRestantes() >= 0);
    }
}