package Juego.Modelo;

import Juego.Modelo.Objetos.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Celda.
 */
public class CeldaTest {

    private Celda celdaVacia;
    private Celda celdaPared;
    private Celda celdaPuerta;
    private Celda celdaTrampa;

    @BeforeEach
    public void setUp() {
        celdaVacia = new Celda();
        celdaPared = new Celda(Celda.TipoCelda.Pared);
        celdaPuerta = new Celda(Celda.TipoCelda.Puerta);
        celdaTrampa = new Celda(Celda.TipoCelda.Trampa);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR VACÍO
    // ============================================================

    @Test
    public void testConstructorVacio() {
        assertEquals(Celda.TipoCelda.Vacia, celdaVacia.getTipo());
        assertNull(celdaVacia.getObjeto());
        assertNull(celdaVacia.getEnemigo());
        assertEquals(-1, celdaVacia.getIdHabitacionDestino());
    }

    @Test
    public void testConstructorConTipo() {
        assertEquals(Celda.TipoCelda.Pared, celdaPared.getTipo());
        assertEquals(Celda.TipoCelda.Puerta, celdaPuerta.getTipo());
        assertEquals(Celda.TipoCelda.Trampa, celdaTrampa.getTipo());

        assertNull(celdaPared.getObjeto());
        assertNull(celdaPared.getEnemigo());
        assertEquals(-1, celdaPared.getIdHabitacionDestino());
    }

    // ============================================================
    // TESTS DE TRANSITABILIDAD
    // ============================================================

    @Test
    public void testTraversableCeldaVacia() {
        assertTrue(celdaVacia.traversable());
    }

    @Test
    public void testTraversableCeldaPared() {
        assertFalse(celdaPared.traversable());
    }

    @Test
    public void testTraversableCeldaPuerta() {
        assertTrue(celdaPuerta.traversable());
    }

    @Test
    public void testTraversableCeldaTrampa() {
        assertTrue(celdaTrampa.traversable());
    }

    // ============================================================
    // TESTS DE TIENE ENEMIGO
    // ============================================================

    @Test
    public void testTieneEnemigoVacia() {
        assertFalse(celdaVacia.tieneEnemigo());
    }

    @Test
    public void testTieneEnemigoConEnemigo() {
        Enemigo enemigo = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 0));
        celdaVacia.setEnemigo(enemigo);

        assertTrue(celdaVacia.tieneEnemigo());
    }

    @Test
    public void testTieneEnemigoDespuesDeEliminar() {
        Enemigo enemigo = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 0));
        celdaVacia.setEnemigo(enemigo);
        celdaVacia.setEnemigo(null);

        assertFalse(celdaVacia.tieneEnemigo());
    }

    // ============================================================
    // TESTS DE TIENE OBJETO
    // ============================================================

    @Test
    public void testTieneObjetoVacia() {
        assertFalse(celdaVacia.tieneObjeto());
    }

    @Test
    public void testTieneObjetoConObjeto() {
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);
        celdaVacia.setObjeto(pocion);

        assertTrue(celdaVacia.tieneObjeto());
    }

    @Test
    public void testTieneObjetoDespuesDeEliminar() {
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);
        celdaVacia.setObjeto(pocion);
        celdaVacia.setObjeto(null);

        assertFalse(celdaVacia.tieneObjeto());
    }

    // ============================================================
    // TESTS DE ES PUERTA
    // ============================================================

    @Test
    public void testEsPuerta() {
        assertFalse(celdaVacia.esPuerta());
        assertFalse(celdaPared.esPuerta());
        assertTrue(celdaPuerta.esPuerta());
        assertFalse(celdaTrampa.esPuerta());
    }

    // ============================================================
    // TESTS DE ES TRAMPA
    // ============================================================

    @Test
    public void testEsTrampa() {
        assertFalse(celdaVacia.esTrampa());
        assertFalse(celdaPared.esTrampa());
        assertFalse(celdaPuerta.esTrampa());
        assertTrue(celdaTrampa.esTrampa());
    }

    // ============================================================
    // TESTS DE GETTERS Y SETTERS
    // ============================================================

    @Test
    public void testSetTipo() {
        celdaVacia.setTipo(Celda.TipoCelda.Trampa);
        assertEquals(Celda.TipoCelda.Trampa, celdaVacia.getTipo());
        assertTrue(celdaVacia.esTrampa());
    }

    @Test
    public void testSetObjeto() {
        Objeto arma = new Arma("Espada oxidada", "Ataque +5", 5, true, false);
        celdaVacia.setObjeto(arma);

        assertNotNull(celdaVacia.getObjeto());
        assertEquals("Espada oxidada", celdaVacia.getObjeto().getNombre());
        assertTrue(celdaVacia.tieneObjeto());
    }

    @Test
    public void testSetEnemigo() {
        Enemigo orco = new Enemigo("Orco", 50, 50, 12, 4, 1, new Posicion(2, 3));
        celdaVacia.setEnemigo(orco);

        assertNotNull(celdaVacia.getEnemigo());
        assertEquals("Orco", celdaVacia.getEnemigo().getNombre());
        assertEquals(50, celdaVacia.getEnemigo().getVida());
        assertTrue(celdaVacia.tieneEnemigo());
    }

    @Test
    public void testIdHabitacionDestino() {
        assertEquals(-1, celdaVacia.getIdHabitacionDestino());

        celdaVacia.setIdHabitacionDestino(5);
        assertEquals(5, celdaVacia.getIdHabitacionDestino());

        celdaVacia.setIdHabitacionDestino(-1);
        assertEquals(-1, celdaVacia.getIdHabitacionDestino());
    }

    // ============================================================
    // TESTS DE COMBINACIONES
    // ============================================================

    @Test
    public void testCeldaConEnemigoYObjeto() {
        Enemigo goblin = new Enemigo("Goblin", 25, 25, 6, 2, 2, new Posicion(1, 1));
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);

        celdaVacia.setEnemigo(goblin);
        celdaVacia.setObjeto(pocion);

        assertTrue(celdaVacia.tieneEnemigo());
        assertTrue(celdaVacia.tieneObjeto());
        assertEquals("Goblin", celdaVacia.getEnemigo().getNombre());
        assertEquals("Poción", celdaVacia.getObjeto().getNombre());
    }

    @Test
    public void testPuertaConDestino() {
        celdaPuerta.setIdHabitacionDestino(3);

        assertTrue(celdaPuerta.esPuerta());
        assertTrue(celdaPuerta.traversable());
        assertEquals(3, celdaPuerta.getIdHabitacionDestino());
    }

    @Test
    public void testTrampaConEnemigo() {
        Enemigo goblin = new Enemigo("Goblin", 25, 25, 6, 2, 2, new Posicion(1, 1));
        celdaTrampa.setEnemigo(goblin);

        assertTrue(celdaTrampa.esTrampa());
        assertTrue(celdaTrampa.traversable());
        assertTrue(celdaTrampa.tieneEnemigo());
    }
}