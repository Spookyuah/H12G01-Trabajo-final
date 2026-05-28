package Structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Array (matriz bidimensional propia).
 */
public class ArrayTest {

    private Array<String> matriz3x4;
    private Array<Integer> matrizInt;

    @BeforeEach
    public void setUp() {
        matriz3x4 = new Array<>(3, 4);
        matrizInt = new Array<>(2, 2);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorDimensiones() {
        assertEquals(3, matriz3x4.getFilas());
        assertEquals(4, matriz3x4.getColumnas());
    }

    @Test
    public void testConstructorDimensionesMinimas() {
        Array<String> minima = new Array<>(1, 1);
        assertEquals(1, minima.getFilas());
        assertEquals(1, minima.getColumnas());
    }

    @Test
    public void testConstructorDimensionesGrandes() {
        Array<String> grande = new Array<>(10, 20);
        assertEquals(10, grande.getFilas());
        assertEquals(20, grande.getColumnas());
    }

    @Test
    public void testConstructorFilasInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Array<>(0, 5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Array<>(-1, 5);
        });
    }

    @Test
    public void testConstructorColumnasInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Array<>(5, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Array<>(5, -1);
        });
    }

    @Test
    public void testConstructorInicializaConNull() {
        for (int i = 0; i < matriz3x4.getFilas(); i++) {
            for (int j = 0; j < matriz3x4.getColumnas(); j++) {
                assertNull(matriz3x4.get(i, j));
            }
        }
    }

    // ============================================================
    // TESTS DE GET Y SET
    // ============================================================

    @Test
    public void testSetYGet() {
        matriz3x4.set(0, 0, "Esquina");
        assertEquals("Esquina", matriz3x4.get(0, 0));
    }

    @Test
    public void testSetYGetVariasPosiciones() {
        matriz3x4.set(0, 0, "A");
        matriz3x4.set(1, 2, "B");
        matriz3x4.set(2, 3, "C");

        assertEquals("A", matriz3x4.get(0, 0));
        assertEquals("B", matriz3x4.get(1, 2));
        assertEquals("C", matriz3x4.get(2, 3));
    }

    @Test
    public void testSetSobrescribir() {
        matriz3x4.set(1, 1, "Original");
        assertEquals("Original", matriz3x4.get(1, 1));

        matriz3x4.set(1, 1, "Modificado");
        assertEquals("Modificado", matriz3x4.get(1, 1));
    }

    @Test
    public void testSetNull() {
        matriz3x4.set(0, 0, "Valor");
        assertEquals("Valor", matriz3x4.get(0, 0));

        matriz3x4.set(0, 0, null);
        assertNull(matriz3x4.get(0, 0));
    }

    @Test
    public void testSetConEnteros() {
        matrizInt.set(0, 0, 42);
        matrizInt.set(0, 1, 99);
        matrizInt.set(1, 0, 7);
        matrizInt.set(1, 1, 13);

        assertEquals(42, matrizInt.get(0, 0));
        assertEquals(99, matrizInt.get(0, 1));
        assertEquals(7, matrizInt.get(1, 0));
        assertEquals(13, matrizInt.get(1, 1));
    }

    // ============================================================
    // TESTS DE GET FUERA DE RANGO
    // ============================================================

    @Test
    public void testGetFilaNegativa() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.get(-1, 0);
        });
    }

    @Test
    public void testGetColumnaNegativa() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.get(0, -1);
        });
    }

    @Test
    public void testGetFilaExcedida() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.get(3, 0);
        });
    }

    @Test
    public void testGetColumnaExcedida() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.get(0, 4);
        });
    }

    @Test
    public void testGetAmbosFueraDeRango() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.get(5, 5);
        });
    }

    // ============================================================
    // TESTS DE SET FUERA DE RANGO
    // ============================================================

    @Test
    public void testSetFilaNegativa() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.set(-1, 0, "Error");
        });
    }

    @Test
    public void testSetColumnaNegativa() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.set(0, -1, "Error");
        });
    }

    @Test
    public void testSetFilaExcedida() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.set(3, 0, "Error");
        });
    }

    @Test
    public void testSetColumnaExcedida() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.set(0, 4, "Error");
        });
    }

    // ============================================================
    // TESTS DE GETFILA
    // ============================================================

    @Test
    public void testGetFila() {
        matriz3x4.set(1, 0, "A");
        matriz3x4.set(1, 1, "B");
        matriz3x4.set(1, 2, "C");
        matriz3x4.set(1, 3, "D");

        Lista<String> fila = matriz3x4.getFila(1);
        assertEquals(4, fila.size());
        assertEquals("A", fila.get(0));
        assertEquals("B", fila.get(1));
        assertEquals("C", fila.get(2));
        assertEquals("D", fila.get(3));
    }

    @Test
    public void testGetFilaPrimera() {
        matriz3x4.set(0, 0, "X");
        Lista<String> fila = matriz3x4.getFila(0);
        assertEquals(4, fila.size());
        assertEquals("X", fila.get(0));
    }

    @Test
    public void testGetFilaUltima() {
        matriz3x4.set(2, 3, "Z");
        Lista<String> fila = matriz3x4.getFila(2);
        assertEquals(4, fila.size());
        assertEquals("Z", fila.get(3));
    }

    @Test
    public void testGetFilaFueraDeRango() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.getFila(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matriz3x4.getFila(3);
        });
    }

    // ============================================================
    // TESTS DE BORDES Y ESQUINAS
    // ============================================================

    @Test
    public void testEsquinaSuperiorIzquierda() {
        matriz3x4.set(0, 0, "ESI");
        assertEquals("ESI", matriz3x4.get(0, 0));
    }

    @Test
    public void testEsquinaSuperiorDerecha() {
        matriz3x4.set(0, 3, "ESD");
        assertEquals("ESD", matriz3x4.get(0, 3));
    }

    @Test
    public void testEsquinaInferiorIzquierda() {
        matriz3x4.set(2, 0, "EII");
        assertEquals("EII", matriz3x4.get(2, 0));
    }

    @Test
    public void testEsquinaInferiorDerecha() {
        matriz3x4.set(2, 3, "EID");
        assertEquals("EID", matriz3x4.get(2, 3));
    }

    // ============================================================
    // TESTS DE LLENADO Y RECORRIDO
    // ============================================================

    @Test
    public void testLlenarMatrizCompleta() {
        Array<Integer> matriz = new Array<>(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matriz.set(i, j, i * 10 + j);
            }
        }

        assertEquals(0, matriz.get(0, 0));
        assertEquals(1, matriz.get(0, 1));
        assertEquals(2, matriz.get(0, 2));
        assertEquals(10, matriz.get(1, 0));
        assertEquals(11, matriz.get(1, 1));
        assertEquals(12, matriz.get(1, 2));
        assertEquals(20, matriz.get(2, 0));
        assertEquals(21, matriz.get(2, 1));
        assertEquals(22, matriz.get(2, 2));
    }

    @Test
    public void testLlenarMatrizPorFilas() {
        Array<Integer> matriz = new Array<>(2, 3);
        int contador = 1;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                matriz.set(i, j, contador++);
            }
        }

        // Verificar fila 0
        Lista<Integer> fila0 = matriz.getFila(0);
        assertEquals(1, fila0.get(0));
        assertEquals(2, fila0.get(1));
        assertEquals(3, fila0.get(2));

        // Verificar fila 1
        Lista<Integer> fila1 = matriz.getFila(1);
        assertEquals(4, fila1.get(0));
        assertEquals(5, fila1.get(1));
        assertEquals(6, fila1.get(2));
    }

    // ============================================================
    // TESTS DE SIMULACIÓN DE HABITACIÓN
    // ============================================================

    @Test
    public void testSimularHabitacion() {
        Array<String> tablero = new Array<>(3, 3);

        // Configurar como habitación
        tablero.set(0, 0, "[JUGADOR]");
        tablero.set(0, 1, "[OBJETO ]");
        tablero.set(0, 2, "[VACÍO  ]");
        tablero.set(1, 0, "[VACÍO  ]");
        tablero.set(1, 1, "[ENEMIGO]");
        tablero.set(1, 2, "[TRAMPA ]");
        tablero.set(2, 0, "[VACÍO  ]");
        tablero.set(2, 1, "[PUERTA ]");
        tablero.set(2, 2, "[VACÍO  ]");

        assertEquals("[JUGADOR]", tablero.get(0, 0));
        assertEquals("[ENEMIGO]", tablero.get(1, 1));
        assertEquals("[PUERTA ]", tablero.get(2, 1));
    }

    @Test
    public void testMoverJugador() {
        Array<String> tablero = new Array<>(3, 3);

        tablero.set(0, 0, "[JUGADOR]");
        tablero.set(2, 0, "[VACÍO  ]");

        // Mover jugador
        tablero.set(0, 0, "[VACÍO  ]");
        tablero.set(2, 0, "[JUGADOR]");

        assertEquals("[VACÍO  ]", tablero.get(0, 0));
        assertEquals("[JUGADOR]", tablero.get(2, 0));
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testMatriz1x1() {
        Array<String> m = new Array<>(1, 1);
        assertEquals(1, m.getFilas());
        assertEquals(1, m.getColumnas());
        assertNull(m.get(0, 0));

        m.set(0, 0, "Único");
        assertEquals("Único", m.get(0, 0));
    }

    @Test
    public void testMatrizAsimetrica() {
        Array<String> m = new Array<>(2, 5);
        assertEquals(2, m.getFilas());
        assertEquals(5, m.getColumnas());

        m.set(0, 4, "Final");
        assertEquals("Final", m.get(0, 4));

        m.set(1, 0, "Inicio");
        assertEquals("Inicio", m.get(1, 0));
    }

    @Test
    public void testMatrizValoresNulos() {
        Array<String> m = new Array<>(2, 2);
        m.set(0, 0, "A");
        m.set(0, 1, null);
        m.set(1, 0, null);
        m.set(1, 1, "D");

        assertEquals("A", m.get(0, 0));
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testMatrizConTiposDiferentes() {
        Array<Object> m = new Array<>(2, 2);
        m.set(0, 0, "String");
        m.set(0, 1, 42);
        m.set(1, 0, true);
        m.set(1, 1, 3.14);

        assertEquals("String", m.get(0, 0));
        assertEquals(42, m.get(0, 1));
        assertEquals(true, m.get(1, 0));
        assertEquals(3.14, m.get(1, 1));
    }
}