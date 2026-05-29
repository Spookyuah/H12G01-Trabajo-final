package Juego.Modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Enemigo.
 */
public class EnemigoTest {

    private Enemigo goblin;
    private Enemigo orco;

    @BeforeEach
    public void setUp() {
        goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(1, 2));
        orco = new Enemigo("Orco", 60, 60, 15, 6, 1, new Posicion(3, 4));
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorGoblin() {
        assertEquals("Goblin", goblin.getNombre());
        assertEquals(30, goblin.getVida());
        assertEquals(30, goblin.getVidaMax());
        assertEquals(8, goblin.getAtq());
        assertEquals(2, goblin.getDef());
        assertEquals(2, goblin.getVel());
        assertEquals(new Posicion(1, 2), goblin.getPosicion());
    }

    @Test
    public void testConstructorOrco() {
        assertEquals("Orco", orco.getNombre());
        assertEquals(60, orco.getVida());
        assertEquals(60, orco.getVidaMax());
        assertEquals(15, orco.getAtq());
        assertEquals(6, orco.getDef());
        assertEquals(1, orco.getVel());
        assertEquals(new Posicion(3, 4), orco.getPosicion());
    }

    @Test
    public void testConstructorEnemigoDebil() {
        Enemigo debil = new Enemigo("Slime", 5, 5, 2, 0, 1, new Posicion(0, 0));

        assertEquals(5, debil.getVida());
        assertEquals(5, debil.getVidaMax());
        assertEquals(2, debil.getAtq());
        assertEquals(0, debil.getDef());
        assertEquals(1, debil.getVel());
    }

    // ============================================================
    // TESTS DE GETTERS
    // ============================================================

    @Test
    public void testGetNombre() {
        assertEquals("Goblin", goblin.getNombre());
        assertEquals("Orco", orco.getNombre());
    }

    @Test
    public void testGetVida() {
        assertEquals(30, goblin.getVida());
        assertEquals(60, orco.getVida());
    }

    @Test
    public void testGetVidaMax() {
        assertEquals(30, goblin.getVidaMax());
        assertEquals(60, orco.getVidaMax());
    }

    @Test
    public void testGetAtq() {
        assertEquals(8, goblin.getAtq());
        assertEquals(15, orco.getAtq());
    }

    @Test
    public void testGetDef() {
        assertEquals(2, goblin.getDef());
        assertEquals(6, orco.getDef());
    }

    @Test
    public void testGetVel() {
        assertEquals(2, goblin.getVel());
        assertEquals(1, orco.getVel());
    }

    @Test
    public void testGetPosicion() {
        assertEquals(new Posicion(1, 2), goblin.getPosicion());
        assertEquals(new Posicion(3, 4), orco.getPosicion());
    }

    // ============================================================
    // TESTS DE SETPOSICION
    // ============================================================

    @Test
    public void testSetPosicion() {
        Posicion nuevaPos = new Posicion(5, 5);
        goblin.setPosicion(nuevaPos);

        assertEquals(new Posicion(5, 5), goblin.getPosicion());
        assertEquals(5, goblin.getPosicion().getX());
        assertEquals(5, goblin.getPosicion().getY());
    }

    @Test
    public void testSetPosicionVariasVeces() {
        goblin.setPosicion(new Posicion(0, 1));
        assertEquals(new Posicion(0, 1), goblin.getPosicion());

        goblin.setPosicion(new Posicion(2, 3));
        assertEquals(new Posicion(2, 3), goblin.getPosicion());

        goblin.setPosicion(new Posicion(4, 0));
        assertEquals(new Posicion(4, 0), goblin.getPosicion());
    }

    // ============================================================
    // TESTS DE DAÑO
    // ============================================================

    @Test
    public void testTakeDmgNormal() {
        goblin.takeDmg(10);
        assertEquals(20, goblin.getVida());
        assertTrue(goblin.estaVivo());
    }

    @Test
    public void testTakeDmgMultiple() {
        goblin.takeDmg(10);
        assertEquals(20, goblin.getVida());

        goblin.takeDmg(15);
        assertEquals(5, goblin.getVida());

        goblin.takeDmg(3);
        assertEquals(2, goblin.getVida());
    }

    @Test
    public void testTakeDmgExacto() {
        goblin.takeDmg(30);
        assertEquals(0, goblin.getVida());
        assertFalse(goblin.estaVivo());
    }

    @Test
    public void testTakeDmgExcesivo() {
        goblin.takeDmg(100);
        assertEquals(0, goblin.getVida());
        assertFalse(goblin.estaVivo());
    }

    @Test
    public void testTakeDmgCero() {
        goblin.takeDmg(0);
        assertEquals(30, goblin.getVida());
        assertTrue(goblin.estaVivo());
    }


    @Test
    public void testTakeDmgNoBajaDeCero() {
        goblin.takeDmg(40);
        assertEquals(0, goblin.getVida());

        goblin.takeDmg(10);
        assertEquals(0, goblin.getVida()); // Sigue en 0
    }

    // ============================================================
    // TESTS DE ESTA VIVO
    // ============================================================

    @Test
    public void testEstaVivoInicial() {
        assertTrue(goblin.estaVivo());
        assertTrue(orco.estaVivo());
    }

    @Test
    public void testEstaVivoConDanio() {
        goblin.takeDmg(25);
        assertTrue(goblin.estaVivo());
    }

    @Test
    public void testEstaVivoMuerto() {
        goblin.takeDmg(30);
        assertFalse(goblin.estaVivo());
    }

    @Test
    public void testEstaVivoConVida1() {
        goblin.takeDmg(29);
        assertEquals(1, goblin.getVida());
        assertTrue(goblin.estaVivo());
    }

    @Test
    public void testEstaVivoDespuesDeMuerte() {
        goblin.takeDmg(50);
        assertFalse(goblin.estaVivo());

        // No revive
        assertFalse(goblin.estaVivo());
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToString() {
        String str = goblin.toString();
        assertTrue(str.contains("Goblin"));
        assertTrue(str.contains("30"));
        assertTrue(str.contains("8"));
        assertTrue(str.contains("2"));
    }

    @Test
    public void testToStringDespuesDeDanio() {
        goblin.takeDmg(10);
        String str = goblin.toString();
        assertTrue(str.contains("20"));
        assertTrue(str.contains("30")); // vidaMax sigue siendo 30
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testEnemigoConVidaCero() {
        Enemigo muerto = new Enemigo("Fantasma", 0, 10, 5, 2, 1, new Posicion(0, 0));

        assertEquals(0, muerto.getVida());
        assertEquals(10, muerto.getVidaMax());
        assertFalse(muerto.estaVivo());
    }

    @Test
    public void testEnemigoConAtaqueCero() {
        Enemigo inofensivo = new Enemigo("Conejo", 10, 10, 0, 0, 3, new Posicion(0, 0));

        assertEquals(0, inofensivo.getAtq());
        assertTrue(inofensivo.estaVivo());
    }

    @Test
    public void testEnemigoRapido() {
        Enemigo rapido = new Enemigo("Veloz", 20, 20, 5, 1, 5, new Posicion(0, 0));

        assertEquals(5, rapido.getVel());
    }

    @Test
    public void testEnemigoLento() {
        Enemigo lento = new Enemigo("Tortuga", 100, 100, 2, 10, 0, new Posicion(0, 0));

        assertEquals(0, lento.getVel());
    }
}