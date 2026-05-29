package Juego.Motor;

import Juego.Modelo.Posicion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase AccionJugador.
 */
public class AccionJugadorTest {

    private Posicion destino = new Posicion(2, 3);
    private String direccion = "ARRIBA";
    private int indiceObjeto = 0;

    // ============================================================
    // TESTS DE MÉTODOS FACTORÍA - SIN MOVIMIENTO
    // ============================================================

    @Test
    public void testAtacar() {
        AccionJugador accion = AccionJugador.atacar("ABAJO");

        assertFalse(accion.tieneMovimiento());
        assertNull(accion.getDestino());
        assertTrue(accion.esAtaque());
        assertEquals("ABAJO", accion.getDireccion());
        assertEquals(-1, accion.getIndiceObjeto());
    }

    @Test
    public void testUsar() {
        AccionJugador accion = AccionJugador.usar(2);

        assertFalse(accion.tieneMovimiento());
        assertNull(accion.getDestino());
        assertTrue(accion.esUsar());
        assertEquals(2, accion.getIndiceObjeto());
        assertNull(accion.getDireccion());
    }

    @Test
    public void testRecoger() {
        AccionJugador accion = AccionJugador.recoger("DERECHA");

        assertFalse(accion.tieneMovimiento());
        assertNull(accion.getDestino());
        assertTrue(accion.esRecoger());
        assertEquals("DERECHA", accion.getDireccion());
        assertEquals(-1, accion.getIndiceObjeto());
    }

    @Test
    public void testAbrir() {
        AccionJugador accion = AccionJugador.abrir("IZQUIERDA");

        assertFalse(accion.tieneMovimiento());
        assertNull(accion.getDestino());
        assertTrue(accion.esAbrir());
        assertEquals("IZQUIERDA", accion.getDireccion());
        assertEquals(-1, accion.getIndiceObjeto());
    }

    @Test
    public void testNada() {
        AccionJugador accion = AccionJugador.nada();

        assertFalse(accion.tieneMovimiento());
        assertNull(accion.getDestino());
        assertTrue(accion.esNada());
        assertNull(accion.getDireccion());
        assertEquals(-1, accion.getIndiceObjeto());
    }

    // ============================================================
    // TESTS DE MÉTODOS FACTORÍA - CON MOVIMIENTO
    // ============================================================

    @Test
    public void testMover() {
        AccionJugador accion = AccionJugador.mover(destino);

        assertTrue(accion.tieneMovimiento());
        assertEquals(destino, accion.getDestino());
        assertTrue(accion.esNada()); // Solo mover, sin acción
    }

    @Test
    public void testMoverYAtacar() {
        AccionJugador accion = AccionJugador.moverYAtacar(destino, "ARRIBA");

        assertTrue(accion.tieneMovimiento());
        assertEquals(destino, accion.getDestino());
        assertTrue(accion.esAtaque());
        assertEquals("ARRIBA", accion.getDireccion());
    }

    @Test
    public void testMoverYUsar() {
        AccionJugador accion = AccionJugador.moverYUsar(destino, 1);

        assertTrue(accion.tieneMovimiento());
        assertEquals(destino, accion.getDestino());
        assertTrue(accion.esUsar());
        assertEquals(1, accion.getIndiceObjeto());
    }

    @Test
    public void testMoverYRecoger() {
        AccionJugador accion = AccionJugador.moverYRecoger(destino, "ABAJO");

        assertTrue(accion.tieneMovimiento());
        assertEquals(destino, accion.getDestino());
        assertTrue(accion.esRecoger());
        assertEquals("ABAJO", accion.getDireccion());
    }

    @Test
    public void testMoverYAbrir() {
        AccionJugador accion = AccionJugador.moverYAbrir(destino, "DERECHA");

        assertTrue(accion.tieneMovimiento());
        assertEquals(destino, accion.getDestino());
        assertTrue(accion.esAbrir());
        assertEquals("DERECHA", accion.getDireccion());
    }

    // ============================================================
    // TESTS DE GETTERS
    // ============================================================

    @Test
    public void testGetDestino() {
        AccionJugador accion = AccionJugador.mover(destino);
        assertEquals(destino, accion.getDestino());
    }

    @Test
    public void testGetDestinoNulo() {
        AccionJugador accion = AccionJugador.nada();
        assertNull(accion.getDestino());
    }

    @Test
    public void testGetTipoAccion() {
        assertEquals(AccionJugador.TipoAccion.ATACAR, AccionJugador.atacar("ARRIBA").getTipoAccion());
        assertEquals(AccionJugador.TipoAccion.USAR, AccionJugador.usar(0).getTipoAccion());
        assertEquals(AccionJugador.TipoAccion.RECOGER, AccionJugador.recoger("ABAJO").getTipoAccion());
        assertEquals(AccionJugador.TipoAccion.ABRIR, AccionJugador.abrir("IZQUIERDA").getTipoAccion());
        assertEquals(AccionJugador.TipoAccion.NADA, AccionJugador.nada().getTipoAccion());
    }

    @Test
    public void testGetDireccion() {
        assertEquals("ARRIBA", AccionJugador.atacar("ARRIBA").getDireccion());
        assertEquals("ABAJO", AccionJugador.recoger("ABAJO").getDireccion());
        assertEquals("IZQUIERDA", AccionJugador.abrir("IZQUIERDA").getDireccion());
        assertEquals("DERECHA", AccionJugador.moverYAtacar(destino, "DERECHA").getDireccion());
    }

    @Test
    public void testGetDireccionNula() {
        assertNull(AccionJugador.usar(0).getDireccion());
        assertNull(AccionJugador.nada().getDireccion());
        assertNull(AccionJugador.mover(destino).getDireccion());
    }

    @Test
    public void testGetIndiceObjeto() {
        assertEquals(0, AccionJugador.usar(0).getIndiceObjeto());
        assertEquals(3, AccionJugador.usar(3).getIndiceObjeto());
        assertEquals(1, AccionJugador.moverYUsar(destino, 1).getIndiceObjeto());
    }

    @Test
    public void testGetIndiceObjetoPorDefecto() {
        assertEquals(-1, AccionJugador.atacar("ARRIBA").getIndiceObjeto());
        assertEquals(-1, AccionJugador.nada().getIndiceObjeto());
        assertEquals(-1, AccionJugador.mover(destino).getIndiceObjeto());
    }

    // ============================================================
    // TESTS DE TIENE MOVIMIENTO
    // ============================================================

    @Test
    public void testTieneMovimientoTrue() {
        assertTrue(AccionJugador.mover(destino).tieneMovimiento());
        assertTrue(AccionJugador.moverYAtacar(destino, "ARRIBA").tieneMovimiento());
        assertTrue(AccionJugador.moverYUsar(destino, 0).tieneMovimiento());
        assertTrue(AccionJugador.moverYRecoger(destino, "ABAJO").tieneMovimiento());
        assertTrue(AccionJugador.moverYAbrir(destino, "DERECHA").tieneMovimiento());
    }

    @Test
    public void testTieneMovimientoFalse() {
        assertFalse(AccionJugador.nada().tieneMovimiento());
        assertFalse(AccionJugador.atacar("ARRIBA").tieneMovimiento());
        assertFalse(AccionJugador.usar(0).tieneMovimiento());
        assertFalse(AccionJugador.recoger("ABAJO").tieneMovimiento());
        assertFalse(AccionJugador.abrir("IZQUIERDA").tieneMovimiento());
    }

    // ============================================================
    // TESTS DE MÉTODOS BOOLEANOS
    // ============================================================

    @Test
    public void testEsAtaque() {
        assertTrue(AccionJugador.atacar("ARRIBA").esAtaque());
        assertTrue(AccionJugador.moverYAtacar(destino, "ARRIBA").esAtaque());
        assertFalse(AccionJugador.usar(0).esAtaque());
        assertFalse(AccionJugador.nada().esAtaque());
    }

    @Test
    public void testEsUsar() {
        assertTrue(AccionJugador.usar(0).esUsar());
        assertTrue(AccionJugador.moverYUsar(destino, 1).esUsar());
        assertFalse(AccionJugador.atacar("ARRIBA").esUsar());
        assertFalse(AccionJugador.nada().esUsar());
    }

    @Test
    public void testEsRecoger() {
        assertTrue(AccionJugador.recoger("ABAJO").esRecoger());
        assertTrue(AccionJugador.moverYRecoger(destino, "ABAJO").esRecoger());
        assertFalse(AccionJugador.usar(0).esRecoger());
        assertFalse(AccionJugador.nada().esRecoger());
    }

    @Test
    public void testEsAbrir() {
        assertTrue(AccionJugador.abrir("DERECHA").esAbrir());
        assertTrue(AccionJugador.moverYAbrir(destino, "DERECHA").esAbrir());
        assertFalse(AccionJugador.atacar("ARRIBA").esAbrir());
        assertFalse(AccionJugador.nada().esAbrir());
    }

    @Test
    public void testEsNada() {
        assertTrue(AccionJugador.nada().esNada());
        assertTrue(AccionJugador.mover(destino).esNada());
        assertFalse(AccionJugador.atacar("ARRIBA").esNada());
        assertFalse(AccionJugador.usar(0).esNada());
    }

    // ============================================================
    // TESTS DE TOSTRING
    // ============================================================

    @Test
    public void testToStringMover() {
        String str = AccionJugador.mover(new Posicion(2, 3)).toString();
        assertEquals("Mover a (2,3)", str);
    }

    @Test
    public void testToStringAtacar() {
        String str = AccionJugador.atacar("ARRIBA").toString();
        assertTrue(str.contains("No mover"));
        assertTrue(str.contains("Atacar ARRIBA"));
    }

    @Test
    public void testToStringUsar() {
        String str = AccionJugador.usar(2).toString();
        assertTrue(str.contains("No mover"));
        assertTrue(str.contains("Usar objeto[2]"));
    }

    @Test
    public void testToStringRecoger() {
        String str = AccionJugador.recoger("ABAJO").toString();
        assertTrue(str.contains("No mover"));
        assertTrue(str.contains("Recoger ABAJO"));
    }

    @Test
    public void testToStringAbrir() {
        String str = AccionJugador.abrir("DERECHA").toString();
        assertTrue(str.contains("No mover"));
        assertTrue(str.contains("Abrir puerta DERECHA"));
    }

    @Test
    public void testToStringNada() {
        assertEquals("No mover", AccionJugador.nada().toString());
    }

    @Test
    public void testToStringMoverYAtacar() {
        String str = AccionJugador.moverYAtacar(new Posicion(1, 1), "IZQUIERDA").toString();
        assertTrue(str.contains("Mover a (1,1)"));
        assertTrue(str.contains("Atacar IZQUIERDA"));
    }

    @Test
    public void testToStringMoverYUsar() {
        String str = AccionJugador.moverYUsar(new Posicion(0, 2), 3).toString();
        assertTrue(str.contains("Mover a (0,2)"));
        assertTrue(str.contains("Usar objeto[3]"));
    }

    @Test
    public void testToStringMoverYRecoger() {
        String str = AccionJugador.moverYRecoger(new Posicion(3, 0), "ARRIBA").toString();
        assertTrue(str.contains("Mover a (3,0)"));
        assertTrue(str.contains("Recoger ARRIBA"));
    }

    @Test
    public void testToStringMoverYAbrir() {
        String str = AccionJugador.moverYAbrir(new Posicion(4, 4), "ABAJO").toString();
        assertTrue(str.contains("Mover a (4,4)"));
        assertTrue(str.contains("Abrir puerta ABAJO"));
    }

    // ============================================================
    // TESTS DE COMBINACIONES
    // ============================================================

    @Test
    public void testTodasLasDirecciones() {
        String[] direcciones = {"ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"};
        for (String dir : direcciones) {
            assertEquals(dir, AccionJugador.atacar(dir).getDireccion());
            assertEquals(dir, AccionJugador.recoger(dir).getDireccion());
            assertEquals(dir, AccionJugador.abrir(dir).getDireccion());
        }
    }

    @Test
    public void testDiferentesDestinos() {
        Posicion p1 = new Posicion(0, 0);
        Posicion p2 = new Posicion(5, 10);
        Posicion p3 = new Posicion(3, 7);

        assertEquals(p1, AccionJugador.mover(p1).getDestino());
        assertEquals(p2, AccionJugador.mover(p2).getDestino());
        assertEquals(p3, AccionJugador.mover(p3).getDestino());
    }

    @Test
    public void testDiferentesIndicesObjeto() {
        assertEquals(0, AccionJugador.usar(0).getIndiceObjeto());
        assertEquals(5, AccionJugador.usar(5).getIndiceObjeto());
        assertEquals(9, AccionJugador.usar(9).getIndiceObjeto());
    }
}