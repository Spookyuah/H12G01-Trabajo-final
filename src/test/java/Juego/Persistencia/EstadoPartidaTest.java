package Juego.Persistencia;

import Structures.Lista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase EstadoPartida y sus clases internas.
 */
public class EstadoPartidaTest {

    private EstadoPartida estado;
    private EstadoPartida.JugadorEstado jugadorEstado;
    private EstadoPartida.ObjetoEstado objetoEstado;
    private EstadoPartida.EnemigoEstado enemigoEstado;
    private EstadoPartida.CeldaEstado celdaEstado;

    @BeforeEach
    public void setUp() {
        estado = new EstadoPartida();
        jugadorEstado = new EstadoPartida.JugadorEstado();
        objetoEstado = new EstadoPartida.ObjetoEstado();
        enemigoEstado = new EstadoPartida.EnemigoEstado();
        celdaEstado = new EstadoPartida.CeldaEstado();
    }

    // ============================================================
    // TESTS DE ESTADOPARTIDA (CLASE PRINCIPAL)
    // ============================================================

    @Test
    public void testEstadoPartidaValoresPorDefecto() {
        assertEquals(0, estado.turnosRestantes);
        assertNull(estado.jugador);
        assertNull(estado.enemigos);
        assertNull(estado.celdasModificadas);
        assertFalse(estado.partidaTerminada);
        assertFalse(estado.victoria);
    }

    @Test
    public void testEstadoPartidaTurnosRestantes() {
        estado.turnosRestantes = 20;
        assertEquals(20, estado.turnosRestantes);
    }

    @Test
    public void testEstadoPartidaTerminada() {
        estado.partidaTerminada = true;
        assertTrue(estado.partidaTerminada);
    }

    @Test
    public void testEstadoPartidaVictoria() {
        estado.victoria = true;
        assertTrue(estado.victoria);
    }

    @Test
    public void testEstadoPartidaConJugador() {
        estado.jugador = jugadorEstado;
        assertNotNull(estado.jugador);
    }

    @Test
    public void testEstadoPartidaConEnemigos() {
        Lista<EstadoPartida.EnemigoEstado> enemigos = new Lista<>();
        enemigos.add(enemigoEstado);
        estado.enemigos = enemigos;

        assertNotNull(estado.enemigos);
        assertEquals(1, estado.enemigos.size());
    }

    @Test
    public void testEstadoPartidaConCeldasModificadas() {
        Lista<EstadoPartida.CeldaEstado> celdas = new Lista<>();
        celdas.add(celdaEstado);
        estado.celdasModificadas = celdas;

        assertNotNull(estado.celdasModificadas);
        assertEquals(1, estado.celdasModificadas.size());
    }

    // ============================================================
    // TESTS DE JUGADORESTADO
    // ============================================================

    @Test
    public void testJugadorEstadoValoresPorDefecto() {
        assertNull(jugadorEstado.nombre);
        assertEquals(0, jugadorEstado.vida);
        assertEquals(0, jugadorEstado.vidaMax);
        assertEquals(0, jugadorEstado.atq);
        assertEquals(0, jugadorEstado.def);
        assertEquals(0, jugadorEstado.vel);
        assertEquals(0, jugadorEstado.habitacionActualId);
        assertEquals(0, jugadorEstado.x);
        assertEquals(0, jugadorEstado.y);
        assertNull(jugadorEstado.inventario);
    }

    @Test
    public void testJugadorEstadoConDatos() {
        jugadorEstado.nombre = "Aventurero";
        jugadorEstado.vida = 87;
        jugadorEstado.vidaMax = 100;
        jugadorEstado.atq = 15;
        jugadorEstado.def = 5;
        jugadorEstado.vel = 3;
        jugadorEstado.habitacionActualId = 2;
        jugadorEstado.x = 3;
        jugadorEstado.y = 1;

        assertEquals("Aventurero", jugadorEstado.nombre);
        assertEquals(87, jugadorEstado.vida);
        assertEquals(100, jugadorEstado.vidaMax);
        assertEquals(15, jugadorEstado.atq);
        assertEquals(5, jugadorEstado.def);
        assertEquals(3, jugadorEstado.vel);
        assertEquals(2, jugadorEstado.habitacionActualId);
        assertEquals(3, jugadorEstado.x);
        assertEquals(1, jugadorEstado.y);
    }

    @Test
    public void testJugadorEstadoConVidaBaja() {
        jugadorEstado.vida = 10;
        jugadorEstado.vidaMax = 100;

        assertEquals(10, jugadorEstado.vida);
        assertTrue(jugadorEstado.vida < jugadorEstado.vidaMax);
    }

    @Test
    public void testJugadorEstadoMuerto() {
        jugadorEstado.vida = 0;
        jugadorEstado.vidaMax = 100;

        assertEquals(0, jugadorEstado.vida);
    }

    @Test
    public void testJugadorEstadoConInventario() {
        Lista<EstadoPartida.ObjetoEstado> inventario = new Lista<>();
        inventario.add(objetoEstado);
        jugadorEstado.inventario = inventario;

        assertNotNull(jugadorEstado.inventario);
        assertEquals(1, jugadorEstado.inventario.size());
    }

    @Test
    public void testJugadorEstadoInventarioVacio() {
        jugadorEstado.inventario = new Lista<>();
        assertNotNull(jugadorEstado.inventario);
        assertEquals(0, jugadorEstado.inventario.size());
    }

    @Test
    public void testJugadorEstadoConVariosObjetos() {
        Lista<EstadoPartida.ObjetoEstado> inventario = new Lista<>();
        inventario.add(new EstadoPartida.ObjetoEstado());
        inventario.add(new EstadoPartida.ObjetoEstado());
        inventario.add(new EstadoPartida.ObjetoEstado());
        jugadorEstado.inventario = inventario;

        assertEquals(3, jugadorEstado.inventario.size());
    }

    // ============================================================
    // TESTS DE OBJETOESTADO
    // ============================================================

    @Test
    public void testObjetoEstadoValoresPorDefecto() {
        assertNull(objetoEstado.nombre);
        assertNull(objetoEstado.desc);
        assertNull(objetoEstado.tipo);
        assertEquals(0, objetoEstado.valor);
        assertFalse(objetoEstado.equipable);
        assertFalse(objetoEstado.consumible);
    }

    @Test
    public void testObjetoEstadoPocion() {
        objetoEstado.nombre = "Poción de vida";
        objetoEstado.desc = "Restaura 20 puntos de vida";
        objetoEstado.tipo = "POCION_VIDA";
        objetoEstado.valor = 20;
        objetoEstado.equipable = false;
        objetoEstado.consumible = true;

        assertEquals("Poción de vida", objetoEstado.nombre);
        assertEquals("Restaura 20 puntos de vida", objetoEstado.desc);
        assertEquals("POCION_VIDA", objetoEstado.tipo);
        assertEquals(20, objetoEstado.valor);
        assertFalse(objetoEstado.equipable);
        assertTrue(objetoEstado.consumible);
    }

    @Test
    public void testObjetoEstadoArma() {
        objetoEstado.nombre = "Espada de plata";
        objetoEstado.tipo = "ARMA";
        objetoEstado.valor = 8;
        objetoEstado.equipable = true;
        objetoEstado.consumible = false;

        assertEquals("ARMA", objetoEstado.tipo);
        assertEquals(8, objetoEstado.valor);
        assertTrue(objetoEstado.equipable);
        assertFalse(objetoEstado.consumible);
    }

    @Test
    public void testObjetoEstadoEscudo() {
        objetoEstado.nombre = "Escudo de hierro";
        objetoEstado.tipo = "ESCUDO";
        objetoEstado.valor = 6;
        objetoEstado.equipable = true;
        objetoEstado.consumible = false;

        assertEquals("ESCUDO", objetoEstado.tipo);
        assertEquals(6, objetoEstado.valor);
    }

    @Test
    public void testObjetoEstadoLlave() {
        objetoEstado.nombre = "Llave del tesoro";
        objetoEstado.tipo = "LLAVE";
        objetoEstado.valor = 0;
        objetoEstado.equipable = true;
        objetoEstado.consumible = false;

        assertEquals("LLAVE", objetoEstado.tipo);
        assertEquals(0, objetoEstado.valor);
    }

    @Test
    public void testObjetoEstadoTiposValidos() {
        String[] tipos = {"POCION_VIDA", "ARMA", "ESCUDO", "LLAVE"};
        for (String tipo : tipos) {
            objetoEstado.tipo = tipo;
            assertEquals(tipo, objetoEstado.tipo);
        }
    }

    // ============================================================
    // TESTS DE ENEMIGOESTADO
    // ============================================================

    @Test
    public void testEnemigoEstadoValoresPorDefecto() {
        assertNull(enemigoEstado.nombre);
        assertEquals(0, enemigoEstado.vida);
        assertEquals(0, enemigoEstado.vidaMax);
        assertEquals(0, enemigoEstado.atq);
        assertEquals(0, enemigoEstado.def);
        assertEquals(0, enemigoEstado.vel);
        assertEquals(0, enemigoEstado.habitacionId);
        assertEquals(0, enemigoEstado.x);
        assertEquals(0, enemigoEstado.y);
    }

    @Test
    public void testEnemigoEstadoGoblin() {
        enemigoEstado.nombre = "Goblin";
        enemigoEstado.vida = 10;
        enemigoEstado.vidaMax = 25;
        enemigoEstado.atq = 6;
        enemigoEstado.def = 2;
        enemigoEstado.vel = 2;
        enemigoEstado.habitacionId = 1;
        enemigoEstado.x = 2;
        enemigoEstado.y = 1;

        assertEquals("Goblin", enemigoEstado.nombre);
        assertEquals(10, enemigoEstado.vida);
        assertEquals(25, enemigoEstado.vidaMax);
        assertEquals(6, enemigoEstado.atq);
        assertEquals(2, enemigoEstado.def);
        assertEquals(2, enemigoEstado.vel);
        assertEquals(1, enemigoEstado.habitacionId);
        assertEquals(2, enemigoEstado.x);
        assertEquals(1, enemigoEstado.y);
    }

    @Test
    public void testEnemigoEstadoHerido() {
        enemigoEstado.nombre = "Orco";
        enemigoEstado.vida = 15;
        enemigoEstado.vidaMax = 50;

        assertEquals(15, enemigoEstado.vida);
        assertTrue(enemigoEstado.vida < enemigoEstado.vidaMax);
    }

    @Test
    public void testEnemigoEstadoMuerto() {
        enemigoEstado.nombre = "Goblin";
        enemigoEstado.vida = 0;
        enemigoEstado.vidaMax = 25;

        assertEquals(0, enemigoEstado.vida);
    }

    @Test
    public void testEnemigoEstadoEnDiferenteHabitacion() {
        enemigoEstado.habitacionId = 3;
        assertEquals(3, enemigoEstado.habitacionId);
    }

    // ============================================================
    // TESTS DE CELDAESTADO
    // ============================================================

    @Test
    public void testCeldaEstadoValoresPorDefecto() {
        assertEquals(0, celdaEstado.habitacionId);
        assertEquals(0, celdaEstado.x);
        assertEquals(0, celdaEstado.y);
        assertNull(celdaEstado.tipo);
        assertFalse(celdaEstado.tieneObjeto);
        assertNull(celdaEstado.objeto);
        assertFalse(celdaEstado.tieneEnemigo);
        assertNull(celdaEstado.enemigo);
    }

    @Test
    public void testCeldaEstadoVacia() {
        celdaEstado.habitacionId = 1;
        celdaEstado.x = 0;
        celdaEstado.y = 0;
        celdaEstado.tipo = "VACIA";
        celdaEstado.tieneObjeto = false;
        celdaEstado.tieneEnemigo = false;

        assertEquals(1, celdaEstado.habitacionId);
        assertEquals(0, celdaEstado.x);
        assertEquals(0, celdaEstado.y);
        assertEquals("VACIA", celdaEstado.tipo);
        assertFalse(celdaEstado.tieneObjeto);
        assertFalse(celdaEstado.tieneEnemigo);
    }

    @Test
    public void testCeldaEstadoPared() {
        celdaEstado.tipo = "PARED";
        assertEquals("PARED", celdaEstado.tipo);
    }

    @Test
    public void testCeldaEstadoPuerta() {
        celdaEstado.tipo = "PUERTA";
        assertEquals("PUERTA", celdaEstado.tipo);
    }

    @Test
    public void testCeldaEstadoTrampaActivada() {
        celdaEstado.habitacionId = 2;
        celdaEstado.x = 1;
        celdaEstado.y = 3;
        celdaEstado.tipo = "VACIA"; // La trampa ya se activó, ahora es VACIA
        celdaEstado.tieneObjeto = false;
        celdaEstado.tieneEnemigo = false;

        assertEquals("VACIA", celdaEstado.tipo);
    }

    @Test
    public void testCeldaEstadoConObjeto() {
        celdaEstado.tieneObjeto = true;
        celdaEstado.objeto = objetoEstado;

        assertTrue(celdaEstado.tieneObjeto);
        assertNotNull(celdaEstado.objeto);
        assertFalse(celdaEstado.tieneEnemigo);
    }

    @Test
    public void testCeldaEstadoConEnemigo() {
        celdaEstado.tieneEnemigo = true;
        celdaEstado.enemigo = enemigoEstado;

        assertTrue(celdaEstado.tieneEnemigo);
        assertNotNull(celdaEstado.enemigo);
        assertFalse(celdaEstado.tieneObjeto);
    }

    @Test
    public void testCeldaEstadoConEnemigoYObjeto() {
        // Normalmente no debería pasar, pero la estructura lo permite
        celdaEstado.tieneEnemigo = true;
        celdaEstado.enemigo = enemigoEstado;
        celdaEstado.tieneObjeto = true;
        celdaEstado.objeto = objetoEstado;

        assertTrue(celdaEstado.tieneEnemigo);
        assertTrue(celdaEstado.tieneObjeto);
    }

    @Test
    public void testCeldaEstadoCoordenadas() {
        celdaEstado.habitacionId = 4;
        celdaEstado.x = 5;
        celdaEstado.y = 6;

        assertEquals(4, celdaEstado.habitacionId);
        assertEquals(5, celdaEstado.x);
        assertEquals(6, celdaEstado.y);
    }

    // ============================================================
    // TESTS DE INTEGRACIÓN - ESTADO COMPLETO
    // ============================================================

    @Test
    public void testEstadoPartidaCompleto() {
        // Construir un estado de partida guardada
        estado.turnosRestantes = 20;
        estado.partidaTerminada = false;
        estado.victoria = false;

        // Jugador
        jugadorEstado.nombre = "Aventurero";
        jugadorEstado.vida = 87;
        jugadorEstado.vidaMax = 100;
        jugadorEstado.atq = 15;
        jugadorEstado.def = 5;
        jugadorEstado.vel = 3;
        jugadorEstado.habitacionActualId = 2;
        jugadorEstado.x = 3;
        jugadorEstado.y = 1;

        // Inventario del jugador
        EstadoPartida.ObjetoEstado obj1 = new EstadoPartida.ObjetoEstado();
        obj1.nombre = "Poción de vida";
        obj1.tipo = "POCION_VIDA";
        obj1.valor = 20;
        obj1.consumible = true;

        Lista<EstadoPartida.ObjetoEstado> inventario = new Lista<>();
        inventario.add(obj1);
        jugadorEstado.inventario = inventario;
        estado.jugador = jugadorEstado;

        // Enemigos
        enemigoEstado.nombre = "Goblin";
        enemigoEstado.vida = 10;
        enemigoEstado.vidaMax = 25;
        enemigoEstado.atq = 6;
        enemigoEstado.def = 2;
        enemigoEstado.vel = 2;
        enemigoEstado.habitacionId = 2;
        enemigoEstado.x = 1;
        enemigoEstado.y = 0;

        Lista<EstadoPartida.EnemigoEstado> enemigos = new Lista<>();
        enemigos.add(enemigoEstado);
        estado.enemigos = enemigos;

        // Celdas modificadas
        celdaEstado.habitacionId = 1;
        celdaEstado.x = 2;
        celdaEstado.y = 1;
        celdaEstado.tipo = "VACIA";
        celdaEstado.tieneObjeto = false;
        celdaEstado.tieneEnemigo = false;

        Lista<EstadoPartida.CeldaEstado> celdas = new Lista<>();
        celdas.add(celdaEstado);
        estado.celdasModificadas = celdas;

        // Verificar
        assertEquals(20, estado.turnosRestantes);
        assertFalse(estado.partidaTerminada);
        assertEquals("Aventurero", estado.jugador.nombre);
        assertEquals(87, estado.jugador.vida);
        assertEquals(1, estado.jugador.inventario.size());
        assertEquals("Poción de vida", estado.jugador.inventario.get(0).nombre);
        assertEquals(1, estado.enemigos.size());
        assertEquals("Goblin", estado.enemigos.get(0).nombre);
        assertEquals(10, estado.enemigos.get(0).vida);
        assertEquals(1, estado.celdasModificadas.size());
        assertEquals("VACIA", estado.celdasModificadas.get(0).tipo);
    }

    @Test
    public void testEstadoPartidaDerrota() {
        estado.turnosRestantes = 0;
        estado.partidaTerminada = true;
        estado.victoria = false;

        jugadorEstado.nombre = "Aventurero";
        jugadorEstado.vida = 0;
        jugadorEstado.vidaMax = 100;
        estado.jugador = jugadorEstado;

        assertTrue(estado.partidaTerminada);
        assertFalse(estado.victoria);
        assertEquals(0, estado.jugador.vida);
    }

    @Test
    public void testEstadoPartidaConMuchosEnemigos() {
        Lista<EstadoPartida.EnemigoEstado> enemigos = new Lista<>();
        for (int i = 0; i < 5; i++) {
            EstadoPartida.EnemigoEstado e = new EstadoPartida.EnemigoEstado();
            e.nombre = "Enemigo " + i;
            e.vida = 30;
            e.vidaMax = 30;
            enemigos.add(e);
        }
        estado.enemigos = enemigos;

        assertEquals(5, estado.enemigos.size());
        assertEquals("Enemigo 0", estado.enemigos.get(0).nombre);
        assertEquals("Enemigo 4", estado.enemigos.get(4).nombre);
    }

    @Test
    public void testEstadoPartidaConMuchasCeldasModificadas() {
        Lista<EstadoPartida.CeldaEstado> celdas = new Lista<>();
        for (int i = 0; i < 10; i++) {
            EstadoPartida.CeldaEstado c = new EstadoPartida.CeldaEstado();
            c.habitacionId = 1;
            c.x = i / 3;
            c.y = i % 3;
            c.tipo = "VACIA";
            celdas.add(c);
        }
        estado.celdasModificadas = celdas;

        assertEquals(10, estado.celdasModificadas.size());
    }
}