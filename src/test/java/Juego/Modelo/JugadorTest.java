package Juego.Modelo;

import Juego.Modelo.Objetos.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Jugador.
 */
public class JugadorTest {

    private Jugador jugador;
    private Posicion posInicial;

    @BeforeEach
    public void setUp() {
        posInicial = new Posicion(0, 0);
        jugador = new Jugador("Aventurero", 100, 15, 5, 3, posInicial);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorNombre() {
        assertEquals("Aventurero", jugador.getNombre());
    }

    @Test
    public void testConstructorVida() {
        assertEquals(100, jugador.getVida());
        assertEquals(100, jugador.getVidaMax()); // vidaMax = vida inicialmente
    }

    @Test
    public void testConstructorAtributos() {
        assertEquals(15, jugador.getAtq());
        assertEquals(5, jugador.getDef());
        assertEquals(3, jugador.getVel());
    }

    @Test
    public void testConstructorPosicion() {
        assertEquals(new Posicion(0, 0), jugador.getPosicion());
    }

    @Test
    public void testConstructorInventarioVacio() {
        assertNotNull(jugador.getInventario());
        assertTrue(jugador.getInventario().estaVacio());
    }

    @Test
    public void testConstructorJugadorDebil() {
        Jugador debil = new Jugador("Novato", 50, 5, 2, 2, new Posicion(2, 3));

        assertEquals("Novato", debil.getNombre());
        assertEquals(50, debil.getVida());
        assertEquals(50, debil.getVidaMax());
        assertEquals(5, debil.getAtq());
        assertEquals(2, debil.getDef());
        assertEquals(2, debil.getVel());
    }

    // ============================================================
    // TESTS DE GETTERS
    // ============================================================

    @Test
    public void testGetNombre() {
        assertEquals("Aventurero", jugador.getNombre());
    }

    @Test
    public void testGetVida() {
        assertEquals(100, jugador.getVida());
    }

    @Test
    public void testGetVidaMax() {
        assertEquals(100, jugador.getVidaMax());
    }

    @Test
    public void testGetAtq() {
        assertEquals(15, jugador.getAtq());
    }

    @Test
    public void testGetDef() {
        assertEquals(5, jugador.getDef());
    }

    @Test
    public void testGetVel() {
        assertEquals(3, jugador.getVel());
    }

    @Test
    public void testGetPosicion() {
        assertEquals(posInicial, jugador.getPosicion());
    }

    @Test
    public void testGetInventario() {
        assertNotNull(jugador.getInventario());
        assertEquals(0, jugador.getInventario().getCantidad());
    }

    // ============================================================
    // TESTS DE SETTERS
    // ============================================================

    @Test
    public void testSetNombre() {
        jugador.setNombre("Héroe");
        assertEquals("Héroe", jugador.getNombre());
    }

    @Test
    public void testSetVida() {
        jugador.setVida(50);
        assertEquals(50, jugador.getVida());
    }

    @Test
    public void testSetVidaSuperaMax() {
        jugador.setVida(150);
        assertEquals(100, jugador.getVida()); // Capado en vidaMax
    }

    @Test
    public void testSetVidaNegativa() {
        jugador.setVida(-10);
        assertEquals(0, jugador.getVida()); // No puede ser negativa
    }

    @Test
    public void testSetVidaMax() {
        jugador.setVidaMax(150);
        assertEquals(150, jugador.getVidaMax());
        assertEquals(100, jugador.getVida()); // La vida actual no cambia
    }

    @Test
    public void testSetVidaMaxMenorQueVida() {
        jugador.setVidaMax(50);
        assertEquals(50, jugador.getVidaMax());
        // La vida actual no se ajusta automáticamente (depende de implementación)
    }

    @Test
    public void testSetAtq() {
        jugador.setAtq(20);
        assertEquals(20, jugador.getAtq());
    }

    @Test
    public void testSetAtqCero() {
        jugador.setAtq(0);
        assertEquals(0, jugador.getAtq());
    }

    @Test
    public void testSetDef() {
        jugador.setDef(10);
        assertEquals(10, jugador.getDef());
    }

    @Test
    public void testSetVel() {
        jugador.setVel(5);
        assertEquals(5, jugador.getVel());
    }

    @Test
    public void testSetVelCero() {
        jugador.setVel(0);
        assertEquals(0, jugador.getVel());
    }

    @Test
    public void testSetPosicion() {
        Posicion nueva = new Posicion(5, 7);
        jugador.setPosicion(nueva);
        assertEquals(new Posicion(5, 7), jugador.getPosicion());
    }

    @Test
    public void testSetPosicionVariasVeces() {
        jugador.setPosicion(new Posicion(1, 1));
        assertEquals(new Posicion(1, 1), jugador.getPosicion());

        jugador.setPosicion(new Posicion(3, 4));
        assertEquals(new Posicion(3, 4), jugador.getPosicion());
    }

    // ============================================================
    // TESTS DE DAÑO
    // ============================================================

    @Test
    public void testTakeDmgNormal() {
        jugador.takeDmg(30);
        assertEquals(70, jugador.getVida());
        assertTrue(jugador.estaVivo());
    }

    @Test
    public void testTakeDmgMultiple() {
        jugador.takeDmg(20);
        assertEquals(80, jugador.getVida());

        jugador.takeDmg(35);
        assertEquals(45, jugador.getVida());

        jugador.takeDmg(10);
        assertEquals(35, jugador.getVida());
    }

    @Test
    public void testTakeDmgExacto() {
        jugador.takeDmg(100);
        assertEquals(0, jugador.getVida());
        assertFalse(jugador.estaVivo());
    }

    @Test
    public void testTakeDmgExcesivo() {
        jugador.takeDmg(200);
        assertEquals(0, jugador.getVida());
        assertFalse(jugador.estaVivo());
    }

    @Test
    public void testTakeDmgCero() {
        jugador.takeDmg(0);
        assertEquals(100, jugador.getVida());
        assertTrue(jugador.estaVivo());
    }

    @Test
    public void testTakeDmgNoBajaDeCero() {
        jugador.takeDmg(150);
        assertEquals(0, jugador.getVida());

        jugador.takeDmg(50);
        assertEquals(0, jugador.getVida()); // Sigue en 0
    }

    // ============================================================
    // TESTS DE CURACIÓN
    // ============================================================

    @Test
    public void testCurarNormal() {
        jugador.takeDmg(50);
        assertEquals(50, jugador.getVida());

        jugador.curar(20);
        assertEquals(70, jugador.getVida());
    }

    @Test
    public void testCurarCompleto() {
        jugador.takeDmg(80);
        assertEquals(20, jugador.getVida());

        jugador.curar(80);
        assertEquals(100, jugador.getVida()); // Vuelve al máximo
    }

    @Test
    public void testCurarSuperaMax() {
        jugador.takeDmg(10);
        assertEquals(90, jugador.getVida());

        jugador.curar(50);
        assertEquals(100, jugador.getVida()); // Capado en vidaMax
    }

    @Test
    public void testCurarConVidaLlena() {
        jugador.curar(30);
        assertEquals(100, jugador.getVida()); // No supera vidaMax
    }

    @Test
    public void testCurarConVidaMaxAumentada() {
        jugador.setVidaMax(150);
        jugador.takeDmg(80);
        assertEquals(20, jugador.getVida());

        jugador.curar(200);
        assertEquals(150, jugador.getVida()); // Capado en nueva vidaMax
    }

    @Test
    public void testCurarCero() {
        jugador.takeDmg(30);
        jugador.curar(0);
        assertEquals(70, jugador.getVida());
    }

    // ============================================================
    // TESTS DE ESTA VIVO
    // ============================================================

    @Test
    public void testEstaVivoInicial() {
        assertTrue(jugador.estaVivo());
    }

    @Test
    public void testEstaVivoConDanio() {
        jugador.takeDmg(99);
        assertEquals(1, jugador.getVida());
        assertTrue(jugador.estaVivo());
    }

    @Test
    public void testEstaVivoMuerto() {
        jugador.takeDmg(100);
        assertFalse(jugador.estaVivo());
    }

    @Test
    public void testEstaVivoDespuesDeCurar() {
        jugador.takeDmg(100);
        assertFalse(jugador.estaVivo());

        // No debería poder curarse si está muerto, pero si se pudiera:
        // jugador.curar(50);
        // Depende de la lógica del juego
    }

    // ============================================================
    // TESTS DE INVENTARIO
    // ============================================================

    @Test
    public void testInventarioInicial() {
        Inventario inv = jugador.getInventario();
        assertNotNull(inv);
        assertTrue(inv.estaVacio());
        assertEquals(0, inv.getCantidad());
    }

    @Test
    public void testCogerObjetoEnInventario() {
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);
        assertTrue(jugador.getInventario().cogerObjeto(pocion));
        assertEquals(1, jugador.getInventario().getCantidad());
    }

    @Test
    public void testUsarObjetoDelInventario() {
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);
        jugador.getInventario().cogerObjeto(pocion);

        jugador.takeDmg(50);
        assertEquals(50, jugador.getVida());

        // Usar la poción
        pocion.usar(jugador);
        assertEquals(70, jugador.getVida());
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToString() {
        String str = jugador.toString();
        assertTrue(str.contains("Aventurero"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("15"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("(0,0)"));
    }

    @Test
    public void testToStringDespuesDeDanio() {
        jugador.takeDmg(30);
        String str = jugador.toString();
        assertTrue(str.contains("70"));
        assertTrue(str.contains("100")); // vidaMax sigue igual
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testJugadorConVidaCero() {
        Jugador muerto = new Jugador("Fantasma", 0, 5, 2, 1, new Posicion(0, 0));

        assertEquals(0, muerto.getVida());
        assertEquals(0, muerto.getVidaMax());
        assertFalse(muerto.estaVivo());
    }

    @Test
    public void testJugadorConAtaqueCero() {
        Jugador inofensivo = new Jugador("Pacifista", 50, 0, 5, 2, new Posicion(0, 0));

        assertEquals(0, inofensivo.getAtq());
        assertTrue(inofensivo.estaVivo());
    }

    @Test
    public void testJugadorConDefensaCero() {
        Jugador fragil = new Jugador("Frágil", 50, 10, 0, 3, new Posicion(0, 0));

        assertEquals(0, fragil.getDef());
    }

    @Test
    public void testJugadorConVelocidadCero() {
        Jugador inmobil = new Jugador("Estatua", 100, 10, 10, 0, new Posicion(0, 0));

        assertEquals(0, inmobil.getVel());
    }

    @Test
    public void testModificarTodosLosAtributos() {
        jugador.setNombre("Super Héroe");
        jugador.setVidaMax(200);
        jugador.setVida(200);
        jugador.setAtq(30);
        jugador.setDef(20);
        jugador.setVel(5);
        jugador.setPosicion(new Posicion(10, 10));

        assertEquals("Super Héroe", jugador.getNombre());
        assertEquals(200, jugador.getVidaMax());
        assertEquals(200, jugador.getVida());
        assertEquals(30, jugador.getAtq());
        assertEquals(20, jugador.getDef());
        assertEquals(5, jugador.getVel());
        assertEquals(new Posicion(10, 10), jugador.getPosicion());
    }
}