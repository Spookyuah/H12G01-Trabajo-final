package Juego.Modelo;

import Structures.Lista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Mapa.
 */
public class MapaTest {

    private Mapa mapa;
    private Habitacion hab1;
    private Habitacion hab2;
    private Habitacion hab3;
    private Habitacion hab4;
    private Habitacion hab5;

    @BeforeEach
    public void setUp() {
        mapa = new Mapa();
        hab1 = new Habitacion(1, 3, 3);
        hab2 = new Habitacion(2, 4, 4);
        hab3 = new Habitacion(3, 3, 4);
        hab4 = new Habitacion(4, 3, 3);
        hab5 = new Habitacion(5, 4, 3);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorMapaVacio() {
        assertEquals(0, mapa.getNumHabitaciones());
        assertNull(mapa.getActual());
    }

    @Test
    public void testToStringVacio() {
        String str = mapa.toString();
        assertTrue(str.contains("0"));
        assertTrue(str.contains("ninguna"));
    }

    // ============================================================
    // TESTS DE SETINICIAL
    // ============================================================

    @Test
    public void testSetInicial() {
        mapa.setInicial(hab1);

        assertEquals(1, mapa.getNumHabitaciones());
        assertEquals(hab1, mapa.getActual());
        assertTrue(mapa.esInicial(hab1));
    }

    @Test
    public void testSetInicialCambiaActual() {
        mapa.setInicial(hab1);
        assertEquals(hab1, mapa.getActual());
    }

    @Test
    public void testSetInicialDosVeces() {
        mapa.setInicial(hab1);
        assertThrows(IllegalStateException.class, () -> {
            mapa.setInicial(hab2); // Ya tiene raíz
        });
    }

    // ============================================================
    // TESTS DE LINK
    // ============================================================

    @Test
    public void testLinkUnaHabitacion() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);

        assertEquals(2, mapa.getNumHabitaciones());
    }

    @Test
    public void testLinkVariasHabitaciones() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);
        mapa.link(hab2, hab4);

        assertEquals(4, mapa.getNumHabitaciones());
    }

    @Test
    public void testLinkArbolCompleto() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);
        mapa.link(hab2, hab4);
        mapa.link(hab2, hab5);

        assertEquals(5, mapa.getNumHabitaciones());
    }

    @Test
    public void testLinkSinRaiz() {
        assertThrows(IllegalArgumentException.class, () -> {
            mapa.link(hab1, hab2); // No hay raíz
        });
    }

    @Test
    public void testLinkPadreNoExiste() {
        mapa.setInicial(hab1);
        assertThrows(IllegalArgumentException.class, () -> {
            mapa.link(hab3, hab4); // hab3 no está en el árbol
        });
    }

    @Test
    public void testLinkHijoDuplicado() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        assertThrows(IllegalArgumentException.class, () -> {
            mapa.link(hab1, hab2); // hab2 ya existe
        });
    }

    // ============================================================
    // TESTS DE GOTOID
    // ============================================================

    @Test
    public void testGoToIdValido() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);

        assertTrue(mapa.goToId(3));
        assertEquals(hab3, mapa.getActual());
    }

    @Test
    public void testGoToIdInvalido() {
        mapa.setInicial(hab1);
        assertFalse(mapa.goToId(99));
        assertEquals(hab1, mapa.getActual()); // No cambia
    }

    @Test
    public void testGoToIdRaiz() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.goToId(2);
        assertEquals(hab2, mapa.getActual());

        mapa.goToId(1);
        assertEquals(hab1, mapa.getActual()); // Vuelve a la raíz
    }

    @Test
    public void testGoToIdMantieneActualSiFalla() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.goToId(2);
        assertEquals(hab2, mapa.getActual());

        mapa.goToId(999);
        assertEquals(hab2, mapa.getActual()); // No cambió
    }

    // ============================================================
    // TESTS DE ESTRUCTURA DEL ÁRBOL
    // ============================================================

    @Test
    public void testEsInicial() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);

        assertTrue(mapa.esInicial(hab1));
        assertFalse(mapa.esInicial(hab2));
        assertFalse(mapa.esInicial(hab3)); // Ni siquiera está en el árbol
    }

    @Test
    public void testEsSalida() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);
        mapa.link(hab2, hab4); // hab4 es hoja

        assertFalse(mapa.esSalida(hab1)); // Raíz con hijos
        assertFalse(mapa.esSalida(hab2)); // Tiene un hijo
        assertTrue(mapa.esSalida(hab3));  // Sin hijos
        assertTrue(mapa.esSalida(hab4));  // Sin hijos
    }

    @Test
    public void testGetSalidas() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);
        mapa.link(hab2, hab4);
        mapa.link(hab2, hab5);

        Lista<Habitacion> salidas = mapa.getSalidas();
        assertEquals(3, salidas.size()); // hab3, hab4, hab5 son hojas
    }

    @Test
    public void testGetSalidasSoloRaiz() {
        mapa.setInicial(hab1);

        Lista<Habitacion> salidas = mapa.getSalidas();
        assertEquals(1, salidas.size());
        assertEquals(hab1, salidas.get(0));
    }

    @Test
    public void testGetSiguientes() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);

        Lista<Habitacion> siguientes = mapa.getSiguientes(hab1);
        assertEquals(2, siguientes.size());
        assertTrue(contieneHabitacion(siguientes, hab2));
        assertTrue(contieneHabitacion(siguientes, hab3));
    }

    @Test
    public void testGetSiguientesHoja() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);

        Lista<Habitacion> siguientes = mapa.getSiguientes(hab2);
        assertEquals(0, siguientes.size());
    }

    @Test
    public void testGetAllHabitaciones() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);
        mapa.link(hab2, hab4);

        Lista<Habitacion> todas = mapa.getAllHabitaciones();
        assertEquals(4, todas.size());
        assertTrue(contieneHabitacion(todas, hab1));
        assertTrue(contieneHabitacion(todas, hab2));
        assertTrue(contieneHabitacion(todas, hab3));
        assertTrue(contieneHabitacion(todas, hab4));
    }

    @Test
    public void testCaminoA() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab2, hab3);
        mapa.link(hab2, hab4);

        Lista<Habitacion> camino = mapa.caminoA(hab3);
        assertEquals(3, camino.size()); // hab1 -> hab2 -> hab3
        assertEquals(hab1, camino.get(0));
        assertEquals(hab2, camino.get(1));
        assertEquals(hab3, camino.get(2));
    }

    @Test
    public void testCaminoARaiz() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);

        Lista<Habitacion> camino = mapa.caminoA(hab1);
        assertEquals(1, camino.size());
        assertEquals(hab1, camino.get(0));
    }

    @Test
    public void testCaminoANoExiste() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);

        Lista<Habitacion> camino = mapa.caminoA(hab5); // No está en el árbol
        assertEquals(0, camino.size());
    }

    // ============================================================
    // TESTS DE GETNUMHABITACIONES
    // ============================================================

    @Test
    public void testGetNumHabitacionesVacio() {
        assertEquals(0, mapa.getNumHabitaciones());
    }

    @Test
    public void testGetNumHabitacionesDespuesDeSetInicial() {
        mapa.setInicial(hab1);
        assertEquals(1, mapa.getNumHabitaciones());
    }

    @Test
    public void testGetNumHabitacionesDespuesDeLinks() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);
        mapa.link(hab2, hab4);
        mapa.link(hab2, hab5);

        assertEquals(5, mapa.getNumHabitaciones());
    }

    // ============================================================
    // TESTS DE GETACTUAL
    // ============================================================

    @Test
    public void testGetActualInicial() {
        assertNull(mapa.getActual());
    }

    @Test
    public void testGetActualDespuesDeSetInicial() {
        mapa.setInicial(hab1);
        assertEquals(hab1, mapa.getActual());
    }

    @Test
    public void testGetActualDespuesDeGoToId() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.goToId(2);

        assertEquals(hab2, mapa.getActual());
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToStringConHabitaciones() {
        mapa.setInicial(hab1);
        mapa.link(hab1, hab2);
        mapa.link(hab1, hab3);

        String str = mapa.toString();
        assertTrue(str.contains("3"));
        assertTrue(str.contains("Habitacion 1"));
    }

    // ============================================================
    // MÉTODO AUXILIAR
    // ============================================================

    private boolean contieneHabitacion(Lista<Habitacion> lista, Habitacion buscada) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equals(buscada)) {
                return true;
            }
        }
        return false;
    }
}