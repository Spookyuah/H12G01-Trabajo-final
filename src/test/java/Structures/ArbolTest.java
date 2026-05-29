package Structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Arbol (estructura de datos propia).
 */
public class ArbolTest {

    private Arbol<String> arbol;

    @BeforeEach
    public void setUp() {
        arbol = new Arbol<>();
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorArbolVacio() {
        assertNull(arbol.getRaiz());
        assertEquals(0, arbol.getNumNodos());
    }

    // ============================================================
    // TESTS DE SETRAIZ
    // ============================================================

    @Test
    public void testSetRaiz() {
        arbol.setRaiz("A");

        assertEquals("A", arbol.getRaiz());
        assertEquals(1, arbol.getNumNodos());
        assertTrue(arbol.esRaiz("A"));
    }

    @Test
    public void testSetRaizDosVeces() {
        arbol.setRaiz("A");
        assertThrows(IllegalStateException.class, () -> {
            arbol.setRaiz("B");
        });
    }

    @Test
    public void testSetRaizEntero() {
        Arbol<Integer> arbolInt = new Arbol<>();
        arbolInt.setRaiz(1);

        assertEquals(1, arbolInt.getRaiz());
        assertEquals(1, arbolInt.getNumNodos());
    }

    // ============================================================
    // TESTS DE ADDHIJO
    // ============================================================

    @Test
    public void testAddHijoSimple() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");

        assertEquals(2, arbol.getNumNodos());
        assertFalse(arbol.esHoja("A"));
        assertTrue(arbol.esHoja("B"));
    }

    @Test
    public void testAddVariosHijos() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("A", "D");

        assertEquals(4, arbol.getNumNodos());
        assertEquals(3, arbol.getHijos("A").size());
    }

    @Test
    public void testAddHijoProfundo() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("B", "C");
        arbol.addHijo("C", "D");

        assertEquals(4, arbol.getNumNodos());
        assertTrue(arbol.esHoja("D"));
    }

    @Test
    public void testAddHijoSinRaiz() {
        assertThrows(IllegalArgumentException.class, () -> {
            arbol.addHijo("A", "B");
        });
    }

    @Test
    public void testAddHijoPadreNoExiste() {
        arbol.setRaiz("A");
        assertThrows(IllegalArgumentException.class, () -> {
            arbol.addHijo("X", "B");
        });
    }

    @Test
    public void testAddHijoDuplicado() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        assertThrows(IllegalArgumentException.class, () -> {
            arbol.addHijo("A", "B");
        });
    }

    @Test
    public void testAddHijoDuplicadoEnOtroPadre() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        assertThrows(IllegalArgumentException.class, () -> {
            arbol.addHijo("C", "B"); // B ya existe
        });
    }

    // ============================================================
    // TESTS DE GETTERS DE RELACIONES
    // ============================================================

    @Test
    public void testGetHijos() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");

        Lista<String> hijos = arbol.getHijos("A");
        assertEquals(2, hijos.size());
        assertTrue(contiene(hijos, "B"));
        assertTrue(contiene(hijos, "C"));
    }

    @Test
    public void testGetHijosHoja() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");

        Lista<String> hijos = arbol.getHijos("B");
        assertEquals(0, hijos.size());
    }

    @Test
    public void testGetHijosNoExiste() {
        arbol.setRaiz("A");
        Lista<String> hijos = arbol.getHijos("X");
        assertEquals(0, hijos.size());
    }

    @Test
    public void testGetPadre() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");

        assertEquals("A", arbol.getPadre("B"));
        assertNull(arbol.getPadre("A")); // La raíz no tiene padre
    }

    @Test
    public void testGetPadreNoExiste() {
        arbol.setRaiz("A");
        assertNull(arbol.getPadre("X"));
    }

    // ============================================================
    // TESTS DE ESRAIZ Y ESHOJA
    // ============================================================

    @Test
    public void testEsRaiz() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");

        assertTrue(arbol.esRaiz("A"));
        assertFalse(arbol.esRaiz("B"));
        assertFalse(arbol.esRaiz("X"));
    }

    @Test
    public void testEsHoja() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("B", "D");

        assertFalse(arbol.esHoja("A"));
        assertFalse(arbol.esHoja("B"));
        assertTrue(arbol.esHoja("C"));
        assertTrue(arbol.esHoja("D"));
    }

    @Test
    public void testRaizEsHoja() {
        arbol.setRaiz("A");
        assertTrue(arbol.esHoja("A")); // Sin hijos, es hoja
    }

    // ============================================================
    // TESTS DE GETRAIZ Y GETNUMNODOS
    // ============================================================

    @Test
    public void testGetRaiz() {
        assertNull(arbol.getRaiz());
        arbol.setRaiz("A");
        assertEquals("A", arbol.getRaiz());
    }

    @Test
    public void testGetNumNodos() {
        assertEquals(0, arbol.getNumNodos());
        arbol.setRaiz("A");
        assertEquals(1, arbol.getNumNodos());
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        assertEquals(3, arbol.getNumNodos());
    }

    // ============================================================
    // TESTS DE RECORRIDOS
    // ============================================================

    @Test
    public void testRecorridoPreorden() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("B", "D");
        arbol.addHijo("B", "E");
        arbol.addHijo("C", "F");

        Lista<String> preorden = arbol.recorridoPreorden();
        // A, B, D, E, C, F
        assertEquals(6, preorden.size());
        assertEquals("A", preorden.get(0));
        assertEquals("B", preorden.get(1));
        assertEquals("D", preorden.get(2));
        assertEquals("E", preorden.get(3));
        assertEquals("C", preorden.get(4));
        assertEquals("F", preorden.get(5));
    }

    @Test
    public void testRecorridoPreordenSoloRaiz() {
        arbol.setRaiz("A");
        Lista<String> preorden = arbol.recorridoPreorden();
        assertEquals(1, preorden.size());
        assertEquals("A", preorden.get(0));
    }

    @Test
    public void testRecorridoPostorden() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("B", "D");
        arbol.addHijo("B", "E");
        arbol.addHijo("C", "F");

        Lista<String> postorden = arbol.recorridoPostorden();
        // D, E, B, F, C, A
        assertEquals(6, postorden.size());
        assertEquals("D", postorden.get(0));
        assertEquals("E", postorden.get(1));
        assertEquals("B", postorden.get(2));
        assertEquals("F", postorden.get(3));
        assertEquals("C", postorden.get(4));
        assertEquals("A", postorden.get(5));
    }

    @Test
    public void testRecorridoPorNiveles() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("B", "D");
        arbol.addHijo("B", "E");
        arbol.addHijo("C", "F");

        Lista<String> niveles = arbol.recorridoPorNiveles();
        // A, B, C, D, E, F
        assertEquals(6, niveles.size());
        assertEquals("A", niveles.get(0));
        assertEquals("B", niveles.get(1));
        assertEquals("C", niveles.get(2));
        assertEquals("D", niveles.get(3));
        assertEquals("E", niveles.get(4));
        assertEquals("F", niveles.get(5));
    }

    // ============================================================
    // TESTS DE TRAYECTO (CAMINO DESDE RAÍZ)
    // ============================================================

    @Test
    public void testTrayecto() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("B", "C");
        arbol.addHijo("B", "D");

        Lista<String> trayecto = arbol.trayecto("C");
        assertEquals(3, trayecto.size());
        assertEquals("A", trayecto.get(0));
        assertEquals("B", trayecto.get(1));
        assertEquals("C", trayecto.get(2));
    }

    @Test
    public void testTrayectoRaiz() {
        arbol.setRaiz("A");
        Lista<String> trayecto = arbol.trayecto("A");
        assertEquals(1, trayecto.size());
        assertEquals("A", trayecto.get(0));
    }

    @Test
    public void testTrayectoNoExiste() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        Lista<String> trayecto = arbol.trayecto("X");
        assertEquals(0, trayecto.size());
    }

    // ============================================================
    // TESTS DE PROFUNDIDAD
    // ============================================================

    @Test
    public void testProfundidadRaiz() {
        arbol.setRaiz("A");
        assertEquals(0, arbol.profundidad("A"));
    }

    @Test
    public void testProfundidadHijo() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        assertEquals(1, arbol.profundidad("B"));
    }

    @Test
    public void testProfundidadNieto() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("B", "C");
        assertEquals(2, arbol.profundidad("C"));
    }

    @Test
    public void testProfundidadNoExiste() {
        arbol.setRaiz("A");
        assertEquals(-1, arbol.profundidad("X"));
    }

    // ============================================================
    // TESTS DE ALTURA
    // ============================================================

    @Test
    public void testAlturaArbolVacio() {
        assertEquals(-1, arbol.altura());
    }

    @Test
    public void testAlturaSoloRaiz() {
        arbol.setRaiz("A");
        assertEquals(0, arbol.altura());
    }

    @Test
    public void testAlturaConHijos() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        assertEquals(1, arbol.altura());
    }

    @Test
    public void testAlturaConNietos() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("B", "C");
        arbol.addHijo("C", "D");
        assertEquals(3, arbol.altura());
    }

    @Test
    public void testAlturaArbolDesequilibrado() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("B", "D");
        arbol.addHijo("B", "E");
        arbol.addHijo("D", "F");
        // Rama más larga: A-B-D-F = altura 3
        assertEquals(3, arbol.altura());
    }

    // ============================================================
    // TESTS DE GETHOJAS
    // ============================================================

    @Test
    public void testGetHojasRaizSola() {
        arbol.setRaiz("A");
        Lista<String> hojas = arbol.getHojas();
        assertEquals(1, hojas.size());
        assertEquals("A", hojas.get(0));
    }

    @Test
    public void testGetHojasArbolComplejo() {
        arbol.setRaiz("A");
        arbol.addHijo("A", "B");
        arbol.addHijo("A", "C");
        arbol.addHijo("A", "D");
        arbol.addHijo("B", "E");
        arbol.addHijo("B", "F");
        arbol.addHijo("D", "G");

        Lista<String> hojas = arbol.getHojas();
        // Hojas: E, F, C, G
        assertEquals(4, hojas.size());
        assertTrue(contiene(hojas, "E"));
        assertTrue(contiene(hojas, "F"));
        assertTrue(contiene(hojas, "C"));
        assertTrue(contiene(hojas, "G"));
    }

    // ============================================================
    // TESTS CON TIPOS DIFERENTES
    // ============================================================


    @Test
    public void testArbolObjetos() {
        Arbol<String> arbolStr = new Arbol<>();
        arbolStr.setRaiz("Habitacion 1");
        arbolStr.addHijo("Habitacion 1", "Habitacion 2");
        arbolStr.addHijo("Habitacion 1", "Habitacion 3");
        arbolStr.addHijo("Habitacion 2", "Habitacion 4");

        assertEquals(4, arbolStr.getNumNodos());
        assertEquals("Habitacion 1", arbolStr.getRaiz());
        assertTrue(arbolStr.esHoja("Habitacion 3"));
        assertTrue(arbolStr.esHoja("Habitacion 4"));
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testArbolVacioRecorridos() {
        assertEquals(0, arbol.recorridoPreorden().size());
        assertEquals(0, arbol.recorridoPostorden().size());
        assertEquals(0, arbol.recorridoPorNiveles().size());
        assertEquals(0, arbol.getHojas().size());
        assertEquals(0, arbol.trayecto("X").size());
    }

    @Test
    public void testArbolVacioAltura() {
        assertEquals(-1, arbol.altura());
    }

    @Test
    public void testArbolVacioProfundidad() {
        assertEquals(-1, arbol.profundidad("X"));
    }

    @Test
    public void testArbolMuchosHijos() {
        arbol.setRaiz("A");
        for (int i = 0; i < 10; i++) {
            arbol.addHijo("A", "H" + i);
        }
        assertEquals(11, arbol.getNumNodos());
        assertEquals(10, arbol.getHijos("A").size());
        assertEquals(1, arbol.altura());
        assertEquals(10, arbol.getHojas().size());
    }

    @Test
    public void testArbolCadenaLarga() {
        arbol.setRaiz("0");
        for (int i = 1; i <= 10; i++) {
            arbol.addHijo(String.valueOf(i - 1), String.valueOf(i));
        }
        assertEquals(11, arbol.getNumNodos());
        assertEquals(10, arbol.altura());
        assertEquals(10, arbol.profundidad("10"));
        assertEquals(1, arbol.getHojas().size());
    }

    // ============================================================
    // MÉTODO AUXILIAR
    // ============================================================

    private boolean contiene(Lista<String> lista, String buscado) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equals(buscado)) return true;
        }
        return false;
    }
}