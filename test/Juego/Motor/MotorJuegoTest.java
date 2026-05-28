package Juego.Motor;

import Juego.Modelo.*;
import Juego.Modelo.Objetos.*;
import Structures.Lista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase MotorJuego.
 * Usa un mapa simple construido manualmente.
 */
public class MotorJuegoTest {

    private MotorJuego motor;
    private Mapa mapa;
    private Jugador jugador;
    private Habitacion hab1;
    private Habitacion hab2;

    @BeforeEach
    public void setUp() {
        // Construir mapa manualmente
        mapa = new Mapa();
        hab1 = new Habitacion(1, 3, 3);
        hab2 = new Habitacion(2, 3, 3);

        // Configurar habitación 1
        hab1.setCelda(1, 1, new Celda(Celda.TipoCelda.Pared));      // Pared en el centro
        hab1.setCelda(2, 2, crearCeldaPuerta(2));                    // Puerta a hab2

        // Configurar habitación 2 (salida)
        hab2.setCelda(0, 0, crearCeldaConObjeto("Poción", 20));

        // Construir árbol
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);

        // Crear jugador
        jugador = new Jugador("Aventurero", 100, 15, 5, 3, new Posicion(0, 0));

        // Crear motor
        motor = new MotorJuego(mapa, jugador, 25);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructor() {
        assertEquals(mapa, motor.getMapa());
        assertEquals(jugador, motor.getJugador());
        assertEquals(25, motor.getTurnosRestantes());
        assertFalse(motor.esPartidaTerminada());
        assertFalse(motor.esVictoria());
        assertNotNull(motor.getLog());
    }

    @Test
    public void testToString() {
        String str = motor.toString();
        assertTrue(str.contains("MotorJuego"));
        assertTrue(str.contains("Aventurero"));
        assertTrue(str.contains("25"));
    }

    // ============================================================
    // TESTS DE ENEMIGOS
    // ============================================================

    @Test
    public void testAddEnemigo() {
        Enemigo goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 2));
        motor.addEnemigo(goblin);

        Lista<Enemigo> vivos = motor.getEnemigosVivos();
        assertEquals(1, vivos.size());
        assertEquals("Goblin", vivos.get(0).getNombre());
    }

    @Test
    public void testAddVariosEnemigos() {
        motor.addEnemigo(new Enemigo("G1", 20, 20, 5, 1, 2, new Posicion(0, 1)));
        motor.addEnemigo(new Enemigo("G2", 20, 20, 5, 1, 2, new Posicion(0, 2)));
        motor.addEnemigo(new Enemigo("G3", 20, 20, 5, 1, 2, new Posicion(1, 0)));

        assertEquals(3, motor.getEnemigosVivos().size());
    }

    @Test
    public void testRemoveEnemigo() {
        Enemigo goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 2));
        motor.addEnemigo(goblin);
        motor.removeEnemigo(goblin);

        assertEquals(0, motor.getEnemigosVivos().size());
    }

    @Test
    public void testLimpiarEnemigos() {
        motor.addEnemigo(new Enemigo("G1", 20, 20, 5, 1, 2, new Posicion(0, 1)));
        motor.addEnemigo(new Enemigo("G2", 20, 20, 5, 1, 2, new Posicion(0, 2)));
        motor.limpiarEnemigos();

        assertEquals(0, motor.getEnemigosVivos().size());
    }

    @Test
    public void testGetEnemigosVivosSoloVivos() {
        Enemigo vivo = new Enemigo("Vivo", 30, 30, 5, 1, 2, new Posicion(0, 1));
        Enemigo muerto = new Enemigo("Muerto", 0, 30, 5, 1, 2, new Posicion(0, 2)); // Vida 0

        motor.addEnemigo(vivo);
        motor.addEnemigo(muerto);

        Lista<Enemigo> vivos = motor.getEnemigosVivos();
        assertEquals(1, vivos.size());
        assertEquals("Vivo", vivos.get(0).getNombre());
    }

    // ============================================================
    // TESTS DE TURNOS
    // ============================================================

    @Test
    public void testEjecutarTurnoNada() {
        int turnosAntes = motor.getTurnosRestantes();
        motor.ejecutarTurnoJugador(AccionJugador.nada());

        assertEquals(turnosAntes - 1, motor.getTurnosRestantes());
    }

    @Test
    public void testEjecutarTurnoMover() {
        Posicion destino = new Posicion(0, 1);
        motor.ejecutarTurnoJugador(AccionJugador.mover(destino));

        assertEquals(destino, jugador.getPosicion());
        assertEquals(24, motor.getTurnosRestantes());
    }

    @Test
    public void testEjecutarVariosTurnos() {
        motor.ejecutarTurnoJugador(AccionJugador.nada());
        motor.ejecutarTurnoJugador(AccionJugador.nada());
        motor.ejecutarTurnoJugador(AccionJugador.nada());

        assertEquals(22, motor.getTurnosRestantes());
    }

    @Test
    public void testTurnoConEnemigo() {
        Enemigo goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 2));
        motor.addEnemigo(goblin);

        motor.ejecutarTurnoJugador(AccionJugador.nada());
        assertEquals(24, motor.getTurnosRestantes());
        // El enemigo debería seguir vivo
        assertTrue(goblin.estaVivo());
    }

    @Test
    public void testPartidaTerminadaIgnoraAccion() {
        motor.setPartidaTerminada(true);

        motor.ejecutarTurnoJugador(AccionJugador.mover(new Posicion(0, 1)));
        // No debería moverse porque la partida terminó
        assertEquals(new Posicion(0, 0), jugador.getPosicion());
    }

    // ============================================================
    // TESTS DE ATAQUE
    // ============================================================

    @Test
    public void testAtacarEnemigo() {
        Enemigo goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 1));
        motor.addEnemigo(goblin);

        motor.ejecutarTurnoJugador(AccionJugador.atacar("DERECHA")); // Jugador en (0,0), enemigo en (0,1)
    }

    @Test
    public void testAtacarSinEnemigo() {
        motor.ejecutarTurnoJugador(AccionJugador.atacar("DERECHA"));
        // No debería fallar, simplemente no hace nada
        assertEquals(24, motor.getTurnosRestantes());
    }


    // ============================================================
    // TESTS DE RECOGER
    // ============================================================

    @Test
    public void testRecogerObjeto() {
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);
        hab1.setCelda(0, 1, crearCeldaConObjeto("Poción", 20));

        motor.ejecutarTurnoJugador(AccionJugador.recoger("DERECHA"));

        assertEquals(1, jugador.getInventario().getCantidad());
        assertEquals("Poción", jugador.getInventario().getObjeto(0).getNombre());
    }

    @Test
    public void testRecogerSinObjeto() {
        motor.ejecutarTurnoJugador(AccionJugador.recoger("DERECHA"));
        assertEquals(0, jugador.getInventario().getCantidad());
    }

    @Test
    public void testRecogerInventarioLleno() {
        // Llenar inventario
        for (int i = 0; i < 10; i++) {
            jugador.getInventario().cogerObjeto(new Pocion("P" + i, "Cura", 10, false, true));
        }
        hab1.setCelda(0, 1, crearCeldaConObjeto("Poción", 20));

        motor.ejecutarTurnoJugador(AccionJugador.recoger("DERECHA"));
        assertEquals(10, jugador.getInventario().getCantidad()); // No debería añadir
    }

    // ============================================================
    // TESTS DE USAR OBJETO
    // ============================================================

    @Test
    public void testUsarObjeto() {
        jugador.getInventario().cogerObjeto(new Pocion("Poción", "Cura 20", 20, false, true));
        jugador.takeDmg(30);
        assertEquals(70, jugador.getVida());

        motor.ejecutarTurnoJugador(AccionJugador.usar(0));

        assertEquals(90, jugador.getVida());
    }

    @Test
    public void testUsarObjetoIndiceInvalido() {
        motor.ejecutarTurnoJugador(AccionJugador.usar(5));
        assertEquals(24, motor.getTurnosRestantes()); // No pasa nada
    }

    // ============================================================
    // TESTS DE ABRIR PUERTA
    // ============================================================

    @Test
    public void testAbrirPuerta() {
        // Jugador en (0,0), puerta en (2,2). Hay que moverse cerca.
        // Colocar al jugador en (2,1) para que la puerta esté a la DERECHA
        jugador.setPosicion(new Posicion(2, 1));

        motor.ejecutarTurnoJugador(AccionJugador.abrir("DERECHA"));

        assertEquals(hab2, mapa.getActual());
        assertEquals(new Posicion(0, 0), jugador.getPosicion()); // Resetea a (0,0)
    }

    @Test
    public void testAbrirPuertaSinPuerta() {
        motor.ejecutarTurnoJugador(AccionJugador.abrir("DERECHA"));
        assertEquals(hab1, mapa.getActual()); // No cambió
    }

    // ============================================================
    // TESTS DE TRAMPAS
    // ============================================================

    @Test
    public void testPisarTrampa() {
        hab1.setCelda(0, 1, new Celda(Celda.TipoCelda.Trampa));
        motor.ejecutarTurnoJugador(AccionJugador.mover(new Posicion(0, 1)));

        assertTrue(jugador.getVida() < 100); // Recibió daño
        assertEquals(Celda.TipoCelda.Vacia, hab1.getCelda(0, 1).getTipo()); // Trampa desaparece
    }

    // ============================================================
    // TESTS DE GETTERS Y SETTERS
    // ============================================================

    @Test
    public void testGetMovimientosPosibles() {
        Lista<Posicion> movimientos = motor.getMovimientosPosibles();
        assertNotNull(movimientos);
        assertFalse(movimientos.isEmpty());
        // El origen (0,0) siempre está incluido
        assertTrue(contienePosicion(movimientos, new Posicion(0, 0)));
    }

    @Test
    public void testSetTurnosRestantes() {
        motor.setTurnosRestantes(10);
        assertEquals(10, motor.getTurnosRestantes());
    }

    @Test
    public void testSetVictoria() {
        motor.setVictoria(true);
        assertTrue(motor.esVictoria());
    }

    @Test
    public void testSetPartidaTerminada() {
        motor.setPartidaTerminada(true);
        assertTrue(motor.esPartidaTerminada());
    }

    // ============================================================
    // TESTS DE VICTORIA / DERROTA
    // ============================================================

    @Test
    public void testVictoriaAlLlegarASalida() {
        // Colocar al jugador en hab2 (que es hoja/salida)
        mapa.goToId(2);
        jugador.setPosicion(new Posicion(0, 0));

        motor.ejecutarTurnoJugador(AccionJugador.nada());

        assertTrue(motor.esPartidaTerminada());
        assertTrue(motor.esVictoria());
    }

    @Test
    public void testDerrotaSinTurnos() {
        motor.setTurnosRestantes(1);
        motor.ejecutarTurnoJugador(AccionJugador.nada());

        assertTrue(motor.esPartidaTerminada());
        assertFalse(motor.esVictoria());
    }

    @Test
    public void testDerrotaPorMuerte() {
        jugador.takeDmg(100);
        motor.ejecutarTurnoJugador(AccionJugador.nada());

        assertTrue(motor.esPartidaTerminada());
        assertFalse(motor.esVictoria());
    }

    // ============================================================
    // TESTS DE LOG
    // ============================================================

    @Test
    public void testLogNoVacio() {
        Lista<String> log = motor.getLog();
        assertNotNull(log);
        assertTrue(log.size() > 0);
    }

    @Test
    public void testLogContieneInicio() {
        Lista<String> log = motor.getLog();
        String primeraEntrada = log.get(0);
        assertTrue(primeraEntrada.contains("Partida iniciada"));
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private Celda crearCeldaPuerta(int idDestino) {
        Celda celda = new Celda(Celda.TipoCelda.Puerta);
        celda.setIdHabitacionDestino(idDestino);
        return celda;
    }

    private Celda crearCeldaConObjeto(String nombre, int valor) {
        Celda celda = new Celda();
        celda.setObjeto(new Pocion(nombre, "Cura " + valor, valor, false, true));
        return celda;
    }

    private boolean contienePosicion(Lista<Posicion> lista, Posicion buscada) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equals(buscada)) return true;
        }
        return false;
    }
}