package Juego.Modelo;

import Juego.Modelo.Objetos.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Habitacion.
 */
public class HabitacionTest {

    private Habitacion hab3x3;
    private Habitacion hab4x5;

    @BeforeEach
    public void setUp() {
        hab3x3 = new Habitacion(1, 3, 3);
        hab4x5 = new Habitacion(2, 4, 5);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorId() {
        assertEquals(1, hab3x3.getId());
        assertEquals(2, hab4x5.getId());
    }

    @Test
    public void testConstructorDimensiones() {
        assertEquals(3, hab3x3.getFilas());
        assertEquals(3, hab3x3.getColumnas());

        assertEquals(4, hab4x5.getFilas());
        assertEquals(5, hab4x5.getColumnas());
    }

    @Test
    public void testConstructorCeldasInicializadas() {
        // Todas las celdas deben ser Vacia al construir
        for (int i = 0; i < hab3x3.getFilas(); i++) {
            for (int j = 0; j < hab3x3.getColumnas(); j++) {
                Celda celda = hab3x3.getCelda(i, j);
                assertNotNull(celda);
                assertEquals(Celda.TipoCelda.Vacia, celda.getTipo());
            }
        }
    }

    @Test
    public void testConstructorHabitacionGrande() {
        Habitacion grande = new Habitacion(10, 8, 12);
        assertEquals(8, grande.getFilas());
        assertEquals(12, grande.getColumnas());
        assertNotNull(grande.getCelda(7, 11)); // Última celda
    }

    @Test
    public void testConstructorHabitacionMinima() {
        Habitacion minima = new Habitacion(99, 1, 1);
        assertEquals(1, minima.getFilas());
        assertEquals(1, minima.getColumnas());
        assertNotNull(minima.getCelda(0, 0));
    }

    // ============================================================
    // TESTS DE GETTERS
    // ============================================================

    @Test
    public void testGetId() {
        assertEquals(1, hab3x3.getId());
    }

    @Test
    public void testGetFilas() {
        assertEquals(3, hab3x3.getFilas());
        assertEquals(4, hab4x5.getFilas());
    }

    @Test
    public void testGetColumnas() {
        assertEquals(3, hab3x3.getColumnas());
        assertEquals(5, hab4x5.getColumnas());
    }

    // ============================================================
    // TESTS DE GETCELDA
    // ============================================================

    @Test
    public void testGetCeldaPorCoordenadas() {
        Celda celda = hab3x3.getCelda(1, 2);
        assertNotNull(celda);
        assertEquals(Celda.TipoCelda.Vacia, celda.getTipo());
    }

    @Test
    public void testGetCeldaPorPosicion() {
        Posicion pos = new Posicion(2, 1);
        Celda celda = hab3x3.getCelda(pos);
        assertNotNull(celda);
        assertEquals(Celda.TipoCelda.Vacia, celda.getTipo());
    }

    @Test
    public void testGetCeldaBordes() {
        // Esquinas
        assertNotNull(hab4x5.getCelda(0, 0));
        assertNotNull(hab4x5.getCelda(0, 4));
        assertNotNull(hab4x5.getCelda(3, 0));
        assertNotNull(hab4x5.getCelda(3, 4));
    }

    @Test
    public void testGetCeldaFueraDeRango() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            hab3x3.getCelda(-1, 0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            hab3x3.getCelda(0, -1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            hab3x3.getCelda(3, 0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            hab3x3.getCelda(0, 3);
        });
    }

    // ============================================================
    // TESTS DE SETCELDA
    // ============================================================

    @Test
    public void testSetCeldaPorCoordenadas() {
        Celda pared = new Celda(Celda.TipoCelda.Pared);
        hab3x3.setCelda(1, 1, pared);

        Celda recuperada = hab3x3.getCelda(1, 1);
        assertEquals(Celda.TipoCelda.Pared, recuperada.getTipo());
        assertFalse(recuperada.traversable());
    }

    @Test
    public void testSetCeldaPorPosicion() {
        Celda trampa = new Celda(Celda.TipoCelda.Trampa);
        Posicion pos = new Posicion(2, 2);
        hab3x3.setCelda(pos, trampa);

        Celda recuperada = hab3x3.getCelda(pos);
        assertEquals(Celda.TipoCelda.Trampa, recuperada.getTipo());
        assertTrue(recuperada.esTrampa());
    }

    @Test
    public void testSetCeldaConEnemigo() {
        Celda celdaEnemigo = new Celda();
        Enemigo goblin = new Enemigo("Goblin", 30, 30, 8, 2, 2, new Posicion(0, 0));
        celdaEnemigo.setEnemigo(goblin);

        hab3x3.setCelda(0, 1, celdaEnemigo);

        Celda recuperada = hab3x3.getCelda(0, 1);
        assertTrue(recuperada.tieneEnemigo());
        assertEquals("Goblin", recuperada.getEnemigo().getNombre());
    }

    @Test
    public void testSetCeldaConObjeto() {
        Celda celdaObjeto = new Celda();
        Objeto pocion = new Pocion("Poción", "Cura 20", 20, false, true);
        celdaObjeto.setObjeto(pocion);

        hab3x3.setCelda(2, 0, celdaObjeto);

        Celda recuperada = hab3x3.getCelda(2, 0);
        assertTrue(recuperada.tieneObjeto());
        assertEquals("Poción", recuperada.getObjeto().getNombre());
    }

    @Test
    public void testSetCeldaConPuerta() {
        Celda puerta = new Celda(Celda.TipoCelda.Puerta);
        puerta.setIdHabitacionDestino(5);

        hab3x3.setCelda(2, 2, puerta);

        Celda recuperada = hab3x3.getCelda(2, 2);
        assertTrue(recuperada.esPuerta());
        assertEquals(5, recuperada.getIdHabitacionDestino());
    }

    @Test
    public void testSetCeldaSobrescribir() {
        Celda pared = new Celda(Celda.TipoCelda.Pared);
        hab3x3.setCelda(1, 1, pared);
        assertEquals(Celda.TipoCelda.Pared, hab3x3.getCelda(1, 1).getTipo());

        Celda vacia = new Celda();
        hab3x3.setCelda(1, 1, vacia);
        assertEquals(Celda.TipoCelda.Vacia, hab3x3.getCelda(1, 1).getTipo());
    }

    // ============================================================
    // TESTS DE ESPOSICIONVALIDA
    // ============================================================

    @Test
    public void testEsPosicionValidaPorCoordenadas() {
        assertTrue(hab3x3.esPosicionValida(0, 0));
        assertTrue(hab3x3.esPosicionValida(2, 2));
        assertTrue(hab3x3.esPosicionValida(1, 0));

        assertFalse(hab3x3.esPosicionValida(-1, 0));
        assertFalse(hab3x3.esPosicionValida(0, -1));
        assertFalse(hab3x3.esPosicionValida(3, 0));
        assertFalse(hab3x3.esPosicionValida(0, 3));
        assertFalse(hab3x3.esPosicionValida(5, 5));
    }

    @Test
    public void testEsPosicionValidaPorPosicion() {
        assertTrue(hab4x5.esPosicionValida(new Posicion(0, 0)));
        assertTrue(hab4x5.esPosicionValida(new Posicion(3, 4)));
        assertTrue(hab4x5.esPosicionValida(new Posicion(1, 2)));

        assertFalse(hab4x5.esPosicionValida(new Posicion(-1, 0)));
        assertFalse(hab4x5.esPosicionValida(new Posicion(4, 0)));
        assertFalse(hab4x5.esPosicionValida(new Posicion(0, 5)));
    }

    // ============================================================
    // TESTS DE EQUALS
    // ============================================================

    @Test
    public void testEqualsMismoId() {
        Habitacion h1 = new Habitacion(1, 3, 3);
        Habitacion h2 = new Habitacion(1, 5, 5); // Mismo id, distintas dimensiones
        assertEquals(h1, h2);
    }

    @Test
    public void testEqualsDistintoId() {
        Habitacion h1 = new Habitacion(1, 3, 3);
        Habitacion h2 = new Habitacion(2, 3, 3);
        assertNotEquals(h1, h2);
    }

    @Test
    public void testEqualsMismaReferencia() {
        assertEquals(hab3x3, hab3x3);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(hab3x3, null);
    }

    @Test
    public void testEqualsOtroTipo() {
        assertNotEquals("Habitacion 1", hab3x3);
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToString() {
        String str = hab3x3.toString();
        assertTrue(str.contains("Habitacion 1"));
        assertTrue(str.contains("3x3"));
    }

    @Test
    public void testToStringHab4x5() {
        String str = hab4x5.toString();
        assertTrue(str.contains("Habitacion 2"));
        assertTrue(str.contains("4x5"));
    }

    // ============================================================
    // TESTS DE INTEGRACIÓN
    // ============================================================

    @Test
    public void testConfigurarHabitacionCompleta() {
        // Configurar una habitación como el inicio de una partida
        hab3x3.setCelda(0, 0, new Celda()); // Jugador
        hab3x3.setCelda(0, 1, crearCeldaConObjeto("Poción", 20));
        hab3x3.setCelda(1, 1, new Celda(Celda.TipoCelda.Pared));
        hab3x3.setCelda(1, 2, crearCeldaConEnemigo("Goblin", 30, 8, 2, 2));
        hab3x3.setCelda(2, 1, crearCeldaPuerta(3));

        // Verificar la configuración
        assertTrue(hab3x3.getCelda(0, 0).traversable());
        assertTrue(hab3x3.getCelda(0, 1).tieneObjeto());
        assertFalse(hab3x3.getCelda(1, 1).traversable());
        assertTrue(hab3x3.getCelda(1, 2).tieneEnemigo());
        assertTrue(hab3x3.getCelda(2, 1).esPuerta());
        assertEquals(3, hab3x3.getCelda(2, 1).getIdHabitacionDestino());
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private Celda crearCeldaConObjeto(String nombre, int valor) {
        Celda celda = new Celda();
        celda.setObjeto(new Pocion(nombre, "Cura " + valor, valor, false, true));
        return celda;
    }

    private Celda crearCeldaConEnemigo(String nombre, int vida, int atq, int def, int vel) {
        Celda celda = new Celda();
        celda.setEnemigo(new Enemigo(nombre, vida, vida, atq, def, vel, new Posicion(0, 0)));
        return celda;
    }

    private Celda crearCeldaPuerta(int idDestino) {
        Celda celda = new Celda(Celda.TipoCelda.Puerta);
        celda.setIdHabitacionDestino(idDestino);
        return celda;
    }
}