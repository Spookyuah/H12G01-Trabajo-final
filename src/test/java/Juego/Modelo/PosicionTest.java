package Juego.Modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Posicion.
 */
public class PosicionTest {

    private Posicion origen;
    private Posicion destino;

    @BeforeEach
    public void setUp() {
        origen = new Posicion(0, 0);
        destino = new Posicion(3, 4);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR Y GETTERS
    // ============================================================

    @Test
    public void testConstructorOrigen() {
        assertEquals(0, origen.getX());
        assertEquals(0, origen.getY());
    }

    @Test
    public void testConstructorDestino() {
        assertEquals(3, destino.getX());
        assertEquals(4, destino.getY());
    }

    @Test
    public void testConstructorPosicionPositiva() {
        Posicion p = new Posicion(10, 20);
        assertEquals(10, p.getX());
        assertEquals(20, p.getY());
    }

    @Test
    public void testConstructorPosicionNegativa() {
        Posicion p = new Posicion(-5, -3);
        assertEquals(-5, p.getX());
        assertEquals(-3, p.getY());
    }

    @Test
    public void testConstructorPosicionCero() {
        Posicion p = new Posicion(0, 0);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());
    }

    // ============================================================
    // TESTS DE SETTERS
    // ============================================================

    @Test
    public void testSetX() {
        origen.setX(5);
        assertEquals(5, origen.getX());
        assertEquals(0, origen.getY()); // Y no cambia
    }

    @Test
    public void testSetY() {
        origen.setY(7);
        assertEquals(0, origen.getX()); // X no cambia
        assertEquals(7, origen.getY());
    }

    @Test
    public void testSetXeY() {
        origen.setX(8);
        origen.setY(9);
        assertEquals(8, origen.getX());
        assertEquals(9, origen.getY());
    }

    @Test
    public void testSetXNegativo() {
        origen.setX(-2);
        assertEquals(-2, origen.getX());
    }

    @Test
    public void testSetYNegativo() {
        origen.setY(-4);
        assertEquals(-4, origen.getY());
    }

    // ============================================================
    // TESTS DE DISTANCIA (MANHATTAN)
    // ============================================================

    @Test
    public void testDistanciaDe() {
        // |0-3| + |0-4| = 3 + 4 = 7
        assertEquals(7, origen.distanciaDe(destino));
        assertEquals(7, destino.distanciaDe(origen)); // Simétrica
    }

    @Test
    public void testDistanciaMismaPosicion() {
        assertEquals(0, origen.distanciaDe(origen));
        assertEquals(0, new Posicion(5, 5).distanciaDe(new Posicion(5, 5)));
    }

    @Test
    public void testDistanciaAdyacenteArriba() {
        Posicion p1 = new Posicion(2, 2);
        Posicion p2 = new Posicion(1, 2);
        assertEquals(1, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaAdyacenteAbajo() {
        Posicion p1 = new Posicion(2, 2);
        Posicion p2 = new Posicion(3, 2);
        assertEquals(1, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaAdyacenteIzquierda() {
        Posicion p1 = new Posicion(2, 2);
        Posicion p2 = new Posicion(2, 1);
        assertEquals(1, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaAdyacenteDerecha() {
        Posicion p1 = new Posicion(2, 2);
        Posicion p2 = new Posicion(2, 3);
        assertEquals(1, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaPosicionesNegativas() {
        Posicion p1 = new Posicion(-1, -2);
        Posicion p2 = new Posicion(-4, -6);
        // |-1-(-4)| + |-2-(-6)| = |3| + |4| = 7
        assertEquals(7, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaPosicionesMezcladas() {
        Posicion p1 = new Posicion(-1, 3);
        Posicion p2 = new Posicion(2, -4);
        // |-1-2| + |3-(-4)| = 3 + 7 = 10
        assertEquals(10, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaDiagonal() {
        // Diagonal no es distancia 1, sino 2
        Posicion p1 = new Posicion(0, 0);
        Posicion p2 = new Posicion(1, 1);
        assertEquals(2, p1.distanciaDe(p2));
    }

    @Test
    public void testDistanciaLarga() {
        Posicion p1 = new Posicion(0, 0);
        Posicion p2 = new Posicion(100, 200);
        assertEquals(300, p1.distanciaDe(p2));
    }

    // ============================================================
    // TESTS DE EQUALS
    // ============================================================

    @Test
    public void testEqualsMismaReferencia() {
        assertEquals(origen, origen);
        assertTrue(origen.equals(origen));
    }

    @Test
    public void testEqualsMismaPosicion() {
        Posicion p1 = new Posicion(2, 3);
        Posicion p2 = new Posicion(2, 3);
        assertEquals(p1, p2);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }

    @Test
    public void testEqualsPosicionDiferente() {
        Posicion p1 = new Posicion(2, 3);
        Posicion p2 = new Posicion(3, 2);
        assertNotEquals(p1, p2);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsXDistinto() {
        Posicion p1 = new Posicion(2, 3);
        Posicion p2 = new Posicion(5, 3);
        assertNotEquals(p1, p2);
    }

    @Test
    public void testEqualsYDistinto() {
        Posicion p1 = new Posicion(2, 3);
        Posicion p2 = new Posicion(2, 7);
        assertNotEquals(p1, p2);
    }

    @Test
    public void testEqualsNull() {
        assertFalse(origen.equals(null));
        assertNotEquals(origen, null);
    }

    @Test
    public void testEqualsOtroTipo() {
        assertFalse(origen.equals("(0,0)"));
        assertFalse(origen.equals(new Object()));
    }

    @Test
    public void testEqualsPosicionesNegativas() {
        Posicion p1 = new Posicion(-2, -3);
        Posicion p2 = new Posicion(-2, -3);
        assertEquals(p1, p2);
    }

    @Test
    public void testEqualsOrigenYDestino() {
        Posicion p1 = new Posicion(0, 0);
        Posicion p2 = new Posicion(3, 4);
        assertNotEquals(p1, p2);
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToStringOrigen() {
        assertEquals("(0,0)", origen.toString());
    }

    @Test
    public void testToStringDestino() {
        assertEquals("(3,4)", destino.toString());
    }

    @Test
    public void testToStringPosicionNegativa() {
        Posicion p = new Posicion(-1, -2);
        assertEquals("(-1,-2)", p.toString());
    }

    @Test
    public void testToStringPosicionGrande() {
        Posicion p = new Posicion(100, 200);
        assertTrue(p.toString().contains("100"));
        assertTrue(p.toString().contains("200"));
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testDistanciaConValoresMaximos() {
        Posicion p1 = new Posicion(Integer.MAX_VALUE, 0);
        Posicion p2 = new Posicion(0, 0);
        assertEquals(Integer.MAX_VALUE, p1.distanciaDe(p2));
    }

    @Test
    public void testPosicionModificada() {
        Posicion p = new Posicion(1, 1);
        p.setX(5);
        assertEquals(new Posicion(5, 1), p);
        assertNotEquals(new Posicion(1, 1), p);
    }
}