package Juego.Motor;

import Juego.Modelo.*;
import Structures.Lista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Movimiento (BFS y búsqueda de cercano).
 */
public class MovimientoTest {

    private Habitacion hab3x3;
    private Habitacion hab4x4;

    @BeforeEach
    public void setUp() {
        hab3x3 = new Habitacion(1, 3, 3);
        hab4x4 = new Habitacion(2, 4, 4);
    }

    // ============================================================
    // TESTS DE RANGO DE MOVIMIENTO - CASOS BÁSICOS
    // ============================================================

    @Test
    public void testRangoMovimientoOrigen() {
        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 3);

        // El origen siempre debe estar incluido
        assertTrue(contienePosicion(alcances, origen));
    }

    @Test
    public void testRangoMovimientoVelocidad0() {
        Posicion origen = new Posicion(1, 1);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 0);

        assertEquals(1, alcances.size());
        assertEquals(origen, alcances.get(0));
    }

    @Test
    public void testRangoMovimientoVelocidad1() {
        Posicion origen = new Posicion(1, 1);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 1);

        // Origen + 4 adyacentes = 5 posiciones
        assertEquals(5, alcances.size());
        assertTrue(contienePosicion(alcances, new Posicion(1, 1))); // Origen
        assertTrue(contienePosicion(alcances, new Posicion(0, 1))); // Arriba
        assertTrue(contienePosicion(alcances, new Posicion(2, 1))); // Abajo
        assertTrue(contienePosicion(alcances, new Posicion(1, 0))); // Izquierda
        assertTrue(contienePosicion(alcances, new Posicion(1, 2))); // Derecha
    }

    @Test
    public void testRangoMovimientoVelocidad2DesdeEsquina() {
        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 2);

        // Debería alcanzar varias posiciones
        assertTrue(alcances.size() > 1);

        // No debería incluir diagonales directas (distancia Manhattan 2 pero sin camino)
        // (2,0), (0,2), (1,1) deberían ser alcanzables
        assertTrue(contienePosicion(alcances, new Posicion(2, 0)));
        assertTrue(contienePosicion(alcances, new Posicion(0, 2)));
        assertTrue(contienePosicion(alcances, new Posicion(1, 1)));
    }

    @Test
    public void testRangoMovimientoVelocidadAlta() {
        Posicion origen = new Posicion(1, 1);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab4x4, origen, 10);

        // Con velocidad 10 en una habitación 4x4 vacía, debería alcanzar todas las celdas
        assertEquals(16, alcances.size());
    }

    @Test
    public void testRangoMovimientoEnHabitacionGrande() {
        Habitacion grande = new Habitacion(3, 5, 5);
        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(grande, origen, 2);

        assertTrue(alcances.size() > 1);
        assertFalse(contienePosicion(alcances, new Posicion(4, 4))); // Demasiado lejos
    }

    // ============================================================
    // TESTS DE RANGO DE MOVIMIENTO - CON OBSTÁCULOS
    // ============================================================

    @Test
    public void testRangoMovimientoConPared() {
        hab3x3.setCelda(1, 1, new Celda(Celda.TipoCelda.Pared));

        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 3);

        // La pared (1,1) no debe ser alcanzable
        assertFalse(contienePosicion(alcances, new Posicion(1, 1)));
    }

    @Test
    public void testRangoMovimientoParedBloquea() {
        // Crear un pasillo estrecho
        hab3x3.setCelda(1, 0, new Celda(Celda.TipoCelda.Pared));
        hab3x3.setCelda(1, 1, new Celda(Celda.TipoCelda.Pared));
        hab3x3.setCelda(1, 2, new Celda(Celda.TipoCelda.Pared));

        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 3);

        // No debería poder cruzar la fila de paredes
        assertFalse(contienePosicion(alcances, new Posicion(2, 0)));
        assertFalse(contienePosicion(alcances, new Posicion(2, 1)));
        assertFalse(contienePosicion(alcances, new Posicion(2, 2)));
    }

    @Test
    public void testRangoMovimientoRodeandoPared() {
        hab4x4.setCelda(1, 1, new Celda(Celda.TipoCelda.Pared));

        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab4x4, origen, 3);

        // No debe incluir la pared
        assertFalse(contienePosicion(alcances, new Posicion(1, 1)));

        // Pero sí debe poder rodearla para llegar a celdas detrás
        // Depende de la velocidad y la disposición
    }

    @Test
    public void testRangoMovimientoConPuerta() {
        Celda puerta = new Celda(Celda.TipoCelda.Puerta);
        puerta.setIdHabitacionDestino(2);
        hab3x3.setCelda(1, 0, puerta);

        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 2);

        // Las puertas son transitables
        assertTrue(contienePosicion(alcances, new Posicion(1, 0)));
    }

    @Test
    public void testRangoMovimientoConTrampa() {
        hab3x3.setCelda(1, 0, new Celda(Celda.TipoCelda.Trampa));

        Posicion origen = new Posicion(0, 0);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 2);

        // Las trampas son transitables
        assertTrue(contienePosicion(alcances, new Posicion(1, 0)));
    }

    // ============================================================
    // TESTS DE RANGO DE MOVIMIENTO - EXCEPCIONES
    // ============================================================

    @Test
    public void testRangoMovimientoOrigenInvalido() {
        Posicion origen = new Posicion(-1, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            Movimiento.rangoMovimiento(hab3x3, origen, 2);
        });
    }

    @Test
    public void testRangoMovimientoOrigenFueraDeRango() {
        Posicion origen = new Posicion(5, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            Movimiento.rangoMovimiento(hab3x3, origen, 2);
        });
    }

    @Test
    public void testRangoMovimientoOrigenNoTraversable() {
        hab3x3.setCelda(0, 0, new Celda(Celda.TipoCelda.Pared));
        Posicion origen = new Posicion(0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            Movimiento.rangoMovimiento(hab3x3, origen, 2);
        });
    }

    // ============================================================
    // TESTS DE BUSCAR CERCANO
    // ============================================================


    @Test
    public void testBuscarCercanoObjetivoLejos() {
        Lista<Posicion> disponibles = new Lista<>();
        disponibles.add(new Posicion(0, 0));
        disponibles.add(new Posicion(1, 0));
        disponibles.add(new Posicion(2, 0));

        Posicion objetivo = new Posicion(5, 5);
        Posicion cercana = Movimiento.buscarCercano(disponibles, objetivo);

        assertEquals(new Posicion(2, 0), cercana); // La más cercana al (5,5)
    }

    @Test
    public void testBuscarCercanoObjetivoIgual() {
        Lista<Posicion> disponibles = new Lista<>();
        disponibles.add(new Posicion(1, 1));
        disponibles.add(new Posicion(2, 2));

        Posicion objetivo = new Posicion(1, 1);
        Posicion cercana = Movimiento.buscarCercano(disponibles, objetivo);

        assertEquals(new Posicion(1, 1), cercana);
    }

    @Test
    public void testBuscarCercanoListaUnElemento() {
        Lista<Posicion> disponibles = new Lista<>();
        disponibles.add(new Posicion(3, 3));

        Posicion objetivo = new Posicion(0, 0);
        Posicion cercana = Movimiento.buscarCercano(disponibles, objetivo);

        assertEquals(new Posicion(3, 3), cercana);
    }

    @Test
    public void testBuscarCercanoListaVacia() {
        Lista<Posicion> disponibles = new Lista<>();
        Posicion objetivo = new Posicion(0, 0);

        Posicion cercana = Movimiento.buscarCercano(disponibles, objetivo);
        assertNull(cercana);
    }

    @Test
    public void testBuscarCercanoEmpate() {
        Lista<Posicion> disponibles = new Lista<>();
        disponibles.add(new Posicion(0, 2)); // Distancia Manhattan: |0-1| + |2-1| = 1+1 = 2
        disponibles.add(new Posicion(2, 0)); // Distancia Manhattan: |2-1| + |0-1| = 1+1 = 2

        Posicion objetivo = new Posicion(1, 1);
        Posicion cercana = Movimiento.buscarCercano(disponibles, objetivo);

        // En caso de empate, devuelve la primera que encuentra
        assertNotNull(cercana);
        assertEquals(2, cercana.distanciaDe(objetivo));
    }

    // ============================================================
    // TESTS DE INTEGRACIÓN
    // ============================================================

    @Test
    public void testRangoYBuscarCercanoJuntos() {
        Posicion origen = new Posicion(0, 0);
        Posicion jugador = new Posicion(2, 2);

        Lista<Posicion> rango = Movimiento.rangoMovimiento(hab4x4, origen, 3);
        Posicion mejor = Movimiento.buscarCercano(rango, jugador);

        assertNotNull(mejor);
        assertTrue(rango.size() > 1);
        // La mejor posición debería estar más cerca del jugador que el origen
        assertTrue(mejor.distanciaDe(jugador) <= origen.distanciaDe(jugador));
    }

    @Test
    public void testRangoConVelocidadEnemigoTipica() {
        // Simular un enemigo con velocidad 2 en una habitación 3x3
        hab3x3.setCelda(1, 1, new Celda(Celda.TipoCelda.Pared));

        Posicion enemigo = new Posicion(0, 0);
        Posicion jugador = new Posicion(2, 2);

        Lista<Posicion> rango = Movimiento.rangoMovimiento(hab3x3, enemigo, 2);
        Posicion mejor = Movimiento.buscarCercano(rango, jugador);

        assertNotNull(mejor);
        assertFalse(contienePosicion(rango, new Posicion(1, 1))); // Pared no alcanzable
    }

    @Test
    public void testTodasLasPosicionesSonUnicas() {
        Posicion origen = new Posicion(1, 1);
        Lista<Posicion> alcances = Movimiento.rangoMovimiento(hab3x3, origen, 2);

        // Verificar que no hay duplicados
        for (int i = 0; i < alcances.size(); i++) {
            for (int j = i + 1; j < alcances.size(); j++) {
                assertNotEquals(alcances.get(i), alcances.get(j));
            }
        }
    }

    // ============================================================
    // MÉTODO AUXILIAR
    // ============================================================

    private boolean contienePosicion(Lista<Posicion> lista, Posicion buscada) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equals(buscada)) return true;
        }
        return false;
    }
}