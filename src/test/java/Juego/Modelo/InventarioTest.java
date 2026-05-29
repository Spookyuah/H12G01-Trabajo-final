package Juego.Modelo;

import Juego.Modelo.Objetos.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Inventario.
 */
public class InventarioTest {

    private Inventario inventario;
    private Objeto pocion;
    private Objeto arma;
    private Objeto escudo;
    private Objeto llave;
    private Objeto pocionGrande;

    @BeforeEach
    public void setUp() {
        inventario = new Inventario();
        pocion = new Pocion("Poción de vida", "Cura 20", 20, false, true);
        arma = new Arma("Espada oxidada", "Ataque +5", 5, true, false);
        escudo = new Escudo("Escudo de madera", "Defensa +3", 3, true, false);
        pocionGrande = new Pocion("Poción grande", "Cura 50", 50, false, true);
    }

    // ============================================================
    // TESTS DEL CONSTRUCTOR
    // ============================================================

    @Test
    public void testConstructorInventarioVacio() {
        assertTrue(inventario.estaVacio());
        assertFalse(inventario.estaLleno());
        assertEquals(0, inventario.getCantidad());
    }

    @Test
    public void testConstructorCapacidadPorDefecto() {
        // Capacidad por defecto es 10
        for (int i = 0; i < 10; i++) {
            assertTrue(inventario.cogerObjeto(new Pocion("P" + i, "Cura", 10, false, true)));
        }
        assertTrue(inventario.estaLleno());
        assertEquals(10, inventario.getCantidad());
    }

    // ============================================================
    // TESTS DE COGER OBJETO
    // ============================================================

    @Test
    public void testCogerObjeto() {
        assertTrue(inventario.cogerObjeto(pocion));
        assertEquals(1, inventario.getCantidad());
        assertFalse(inventario.estaVacio());
    }

    @Test
    public void testCogerVariosObjetos() {
        assertTrue(inventario.cogerObjeto(pocion));
        assertTrue(inventario.cogerObjeto(arma));
        assertTrue(inventario.cogerObjeto(escudo));

        assertEquals(3, inventario.getCantidad());
    }

    @Test
    public void testCogerObjetoInventarioLleno() {
        // Llenar inventario
        for (int i = 0; i < 10; i++) {
            inventario.cogerObjeto(new Pocion("P" + i, "Cura", 10, false, true));
        }
        assertTrue(inventario.estaLleno());

        // Intentar añadir uno más
        assertFalse(inventario.cogerObjeto(pocion));
        assertEquals(10, inventario.getCantidad());
    }

    @Test
    public void testCogerMismoTipoDeObjeto() {
        // Puede haber dos objetos iguales (mismo nombre, tipo)
        assertTrue(inventario.cogerObjeto(pocion));
        assertTrue(inventario.cogerObjeto(pocion)); // Misma poción
        assertEquals(2, inventario.getCantidad());
    }

    // ============================================================
    // TESTS DE ELIMINAR OBJETO
    // ============================================================

    @Test
    public void testEliminarObjeto() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);
        inventario.cogerObjeto(escudo);

        Objeto eliminado = inventario.eliminarObjeto(1);

        assertEquals(arma, eliminado);
        assertEquals(2, inventario.getCantidad());
    }

    @Test
    public void testEliminarPrimerObjeto() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);

        Objeto eliminado = inventario.eliminarObjeto(0);

        assertEquals(pocion, eliminado);
        assertEquals(1, inventario.getCantidad());
        assertEquals(arma, inventario.getObjeto(0));
    }

    @Test
    public void testEliminarUltimoObjeto() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);
        inventario.cogerObjeto(escudo);

        Objeto eliminado = inventario.eliminarObjeto(2);

        assertEquals(escudo, eliminado);
        assertEquals(2, inventario.getCantidad());
    }

    @Test
    public void testEliminarObjetoFueraDeRango() {
        inventario.cogerObjeto(pocion);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            inventario.eliminarObjeto(5);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inventario.eliminarObjeto(-1);
        });
    }

    @Test
    public void testEliminarDeInventarioVacio() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inventario.eliminarObjeto(0);
        });
    }

    // ============================================================
    // TESTS DE GET OBJETO
    // ============================================================

    @Test
    public void testGetObjeto() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);

        assertEquals(pocion, inventario.getObjeto(0));
        assertEquals(arma, inventario.getObjeto(1));
    }

    @Test
    public void testGetObjetoSinEliminar() {
        inventario.cogerObjeto(pocion);

        Objeto recuperado = inventario.getObjeto(0);
        assertNotNull(recuperado);
        assertEquals(1, inventario.getCantidad()); // No se elimina
    }

    @Test
    public void testGetObjetoFueraDeRango() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inventario.getObjeto(0);
        });
    }

    // ============================================================
    // TESTS DE ESTADO DEL INVENTARIO
    // ============================================================

    @Test
    public void testEstaVacioInicial() {
        assertTrue(inventario.estaVacio());
    }

    @Test
    public void testEstaVacioDespuesDeCoger() {
        inventario.cogerObjeto(pocion);
        assertFalse(inventario.estaVacio());
    }

    @Test
    public void testEstaVacioDespuesDeEliminarTodo() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);
        inventario.eliminarObjeto(1);
        inventario.eliminarObjeto(0);

        assertTrue(inventario.estaVacio());
        assertEquals(0, inventario.getCantidad());
    }

    @Test
    public void testEstaLlenoInicial() {
        assertFalse(inventario.estaLleno());
    }

    @Test
    public void testEstaLlenoCon10Objetos() {
        for (int i = 0; i < 10; i++) {
            inventario.cogerObjeto(new Pocion("P" + i, "Cura", 10, false, true));
        }
        assertTrue(inventario.estaLleno());
    }

    @Test
    public void testEstaLlenoCon9Objetos() {
        for (int i = 0; i < 9; i++) {
            inventario.cogerObjeto(new Pocion("P" + i, "Cura", 10, false, true));
        }
        assertFalse(inventario.estaLleno());
    }

    @Test
    public void testGetCantidad() {
        assertEquals(0, inventario.getCantidad());

        inventario.cogerObjeto(pocion);
        assertEquals(1, inventario.getCantidad());

        inventario.cogerObjeto(arma);
        assertEquals(2, inventario.getCantidad());

        inventario.eliminarObjeto(0);
        assertEquals(1, inventario.getCantidad());
    }

    // ============================================================
    // TESTS DE VACIAR
    // ============================================================

    @Test
    public void testVaciar() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);
        inventario.cogerObjeto(escudo);

        inventario.vaciar();

        assertTrue(inventario.estaVacio());
        assertEquals(0, inventario.getCantidad());
    }

    @Test
    public void testVaciarYaVacio() {
        inventario.vaciar();
        assertTrue(inventario.estaVacio());
        assertEquals(0, inventario.getCantidad());
    }

    @Test
    public void testVaciarYCogerDespues() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);
        inventario.vaciar();

        assertTrue(inventario.cogerObjeto(llave));
        assertEquals(1, inventario.getCantidad());
        assertEquals(llave, inventario.getObjeto(0));
    }

    // ============================================================
    // TESTS DE GET TODOS
    // ============================================================

    @Test
    public void testGetTodosVacio() {
        assertNotNull(inventario.getTodos());
        assertTrue(inventario.getTodos().isEmpty());
    }

    @Test
    public void testGetTodosConObjetos() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);

        assertEquals(2, inventario.getTodos().getSize());
        assertEquals(pocion, inventario.getTodos().get(0));
        assertEquals(arma, inventario.getTodos().get(1));
    }

    @Test
    public void testGetTodosNoModificaInventario() {
        inventario.cogerObjeto(pocion);
        inventario.getTodos();

        assertEquals(1, inventario.getCantidad());
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToStringVacio() {
        assertEquals("Inventario vacío", inventario.toString());
    }

    @Test
    public void testToStringConObjetos() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);

        String str = inventario.toString();
        assertTrue(str.contains("Inventario (2/10)"));
        assertTrue(str.contains("Poción de vida"));
        assertTrue(str.contains("Espada oxidada"));
        assertTrue(str.contains("0."));
        assertTrue(str.contains("1."));
    }

    // ============================================================
    // TESTS DE TIPOS DE OBJETOS
    // ============================================================

    @Test
    public void testCogerDiferentesTipos() {
        assertTrue(inventario.cogerObjeto(pocion));
        assertTrue(inventario.cogerObjeto(arma));
        assertTrue(inventario.cogerObjeto(escudo));
        assertTrue(inventario.cogerObjeto(llave));
        assertTrue(inventario.cogerObjeto(pocionGrande));

        assertEquals(5, inventario.getCantidad());
        assertTrue(inventario.getObjeto(0) instanceof Pocion);
        assertTrue(inventario.getObjeto(1) instanceof Arma);
        assertTrue(inventario.getObjeto(2) instanceof Escudo);
        assertTrue(inventario.getObjeto(4) instanceof Pocion);
    }

    @Test
    public void testEliminarYVerificarTipo() {
        inventario.cogerObjeto(pocion);
        inventario.cogerObjeto(arma);
        inventario.cogerObjeto(escudo);

        inventario.eliminarObjeto(1); // Eliminar arma

        assertTrue(inventario.getObjeto(0) instanceof Pocion);
        assertTrue(inventario.getObjeto(1) instanceof Escudo);
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testCogerDespuesDeEliminarDeLleno() {
        // Llenar
        for (int i = 0; i < 10; i++) {
            inventario.cogerObjeto(new Pocion("P" + i, "Cura", 10, false, true));
        }
        assertTrue(inventario.estaLleno());

        // Eliminar uno
        inventario.eliminarObjeto(0);
        assertFalse(inventario.estaLleno());

        // Coger otro
        assertTrue(inventario.cogerObjeto(pocionGrande));
        assertEquals(10, inventario.getCantidad());
        assertTrue(inventario.estaLleno());
    }
}