package Juego.Persistencia;

import Structures.Lista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase ConfigPartida y sus clases internas.
 */
public class ConfigPartidaTest {

    private ConfigPartida config;
    private ConfigPartida.HabitacionConfig habConfig;
    private ConfigPartida.CeldaConfig celdaConfig;
    private ConfigPartida.EnemigoConfig enemigoConfig;
    private ConfigPartida.ObjetoConfig objetoConfig;
    private ConfigPartida.PuertaConfig puertaConfig;
    private ConfigPartida.JugadorConfig jugadorConfig;

    @BeforeEach
    public void setUp() {
        config = new ConfigPartida();
        habConfig = new ConfigPartida.HabitacionConfig();
        celdaConfig = new ConfigPartida.CeldaConfig();
        enemigoConfig = new ConfigPartida.EnemigoConfig();
        objetoConfig = new ConfigPartida.ObjetoConfig();
        puertaConfig = new ConfigPartida.PuertaConfig();
        jugadorConfig = new ConfigPartida.JugadorConfig();
    }

    // ============================================================
    // TESTS DE CONFIGPARTIDA (CLASE PRINCIPAL)
    // ============================================================

    @Test
    public void testConfigPartidaValoresPorDefecto() {
        assertNull(config.habitaciones);
        assertNull(config.jugador);
        assertEquals(0, config.turnosTotales);
    }

    @Test
    public void testConfigPartidaTurnosTotales() {
        config.turnosTotales = 35;
        assertEquals(35, config.turnosTotales);
    }

    @Test
    public void testConfigPartidaJugador() {
        config.jugador = jugadorConfig;
        assertNotNull(config.jugador);
        assertEquals(jugadorConfig, config.jugador);
    }

    @Test
    public void testConfigPartidaHabitaciones() {
        Lista<ConfigPartida.HabitacionConfig> lista = new Lista<>();
        lista.add(habConfig);
        config.habitaciones = lista;

        assertNotNull(config.habitaciones);
        assertEquals(1, config.habitaciones.size());
    }

    // ============================================================
    // TESTS DE HABITACIONCONFIG
    // ============================================================

    @Test
    public void testHabitacionConfigValoresPorDefecto() {
        assertEquals(0, habConfig.id);
        assertEquals(0, habConfig.filas);
        assertEquals(0, habConfig.columnas);
        assertNull(habConfig.celdas);
        assertNull(habConfig.puertas);
        assertNull(habConfig.padreId);
    }

    @Test
    public void testHabitacionConfigConDatos() {
        habConfig.id = 1;
        habConfig.filas = 4;
        habConfig.columnas = 5;

        assertEquals(1, habConfig.id);
        assertEquals(4, habConfig.filas);
        assertEquals(5, habConfig.columnas);
    }

    @Test
    public void testHabitacionConfigPadreIdRaiz() {
        assertNull(habConfig.padreId); // Raíz
    }

    @Test
    public void testHabitacionConfigPadreIdHija() {
        habConfig.padreId = 1;
        assertEquals(1, habConfig.padreId.intValue());
    }

    @Test
    public void testHabitacionConfigConCeldas() {
        Lista<ConfigPartida.CeldaConfig> celdas = new Lista<>();
        celdas.add(celdaConfig);
        celdas.add(new ConfigPartida.CeldaConfig());

        habConfig.celdas = celdas;
        assertEquals(2, habConfig.celdas.size());
    }

    @Test
    public void testHabitacionConfigConPuertas() {
        Lista<ConfigPartida.PuertaConfig> puertas = new Lista<>();
        puertas.add(puertaConfig);

        habConfig.puertas = puertas;
        assertEquals(1, habConfig.puertas.size());
    }

    @Test
    public void testHabitacionConfigSinPuertas() {
        habConfig.puertas = new Lista<>();
        assertEquals(0, habConfig.puertas.size());
    }

    // ============================================================
    // TESTS DE CELDACONFIG
    // ============================================================

    @Test
    public void testCeldaConfigValoresPorDefecto() {
        assertEquals(0, celdaConfig.x);
        assertEquals(0, celdaConfig.y);
        assertNull(celdaConfig.tipo);
        assertNull(celdaConfig.enemigo);
        assertNull(celdaConfig.objeto);
    }

    @Test
    public void testCeldaConfigConCoordenadas() {
        celdaConfig.x = 2;
        celdaConfig.y = 3;

        assertEquals(2, celdaConfig.x);
        assertEquals(3, celdaConfig.y);
    }

    @Test
    public void testCeldaConfigTipos() {
        celdaConfig.tipo = "VACIA";
        assertEquals("VACIA", celdaConfig.tipo);

        celdaConfig.tipo = "PARED";
        assertEquals("PARED", celdaConfig.tipo);

        celdaConfig.tipo = "PUERTA";
        assertEquals("PUERTA", celdaConfig.tipo);

        celdaConfig.tipo = "TRAMPA";
        assertEquals("TRAMPA", celdaConfig.tipo);
    }

    @Test
    public void testCeldaConfigConEnemigo() {
        celdaConfig.enemigo = enemigoConfig;
        assertNotNull(celdaConfig.enemigo);
    }

    @Test
    public void testCeldaConfigConObjeto() {
        celdaConfig.objeto = objetoConfig;
        assertNotNull(celdaConfig.objeto);
    }

    @Test
    public void testCeldaConfigConEnemigoYObjeto() {
        celdaConfig.enemigo = enemigoConfig;
        celdaConfig.objeto = objetoConfig;

        assertNotNull(celdaConfig.enemigo);
        assertNotNull(celdaConfig.objeto);
    }

    @Test
    public void testCeldaConfigVacia() {
        celdaConfig.tipo = "VACIA";
        assertNull(celdaConfig.enemigo);
        assertNull(celdaConfig.objeto);
    }

    // ============================================================
    // TESTS DE ENEMIGOCONFIG
    // ============================================================

    @Test
    public void testEnemigoConfigValoresPorDefecto() {
        assertNull(enemigoConfig.nombre);
        assertEquals(0, enemigoConfig.vida);
        assertEquals(0, enemigoConfig.vidaMax);
        assertEquals(0, enemigoConfig.atq);
        assertEquals(0, enemigoConfig.def);
        assertEquals(0, enemigoConfig.vel);
        assertEquals(0, enemigoConfig.xInicial);
        assertEquals(0, enemigoConfig.yInicial);
    }

    @Test
    public void testEnemigoConfigGoblin() {
        enemigoConfig.nombre = "Goblin";
        enemigoConfig.vida = 25;
        enemigoConfig.vidaMax = 25;
        enemigoConfig.atq = 6;
        enemigoConfig.def = 2;
        enemigoConfig.vel = 2;
        enemigoConfig.xInicial = 1;
        enemigoConfig.yInicial = 2;

        assertEquals("Goblin", enemigoConfig.nombre);
        assertEquals(25, enemigoConfig.vida);
        assertEquals(25, enemigoConfig.vidaMax);
        assertEquals(6, enemigoConfig.atq);
        assertEquals(2, enemigoConfig.def);
        assertEquals(2, enemigoConfig.vel);
        assertEquals(1, enemigoConfig.xInicial);
        assertEquals(2, enemigoConfig.yInicial);
    }

    @Test
    public void testEnemigoConfigDragon() {
        enemigoConfig.nombre = "Dragón legendario";
        enemigoConfig.vida = 130;
        enemigoConfig.vidaMax = 130;
        enemigoConfig.atq = 25;
        enemigoConfig.def = 12;
        enemigoConfig.vel = 1;
        enemigoConfig.xInicial = 2;
        enemigoConfig.yInicial = 1;

        assertEquals("Dragón legendario", enemigoConfig.nombre);
        assertEquals(130, enemigoConfig.vida);
        assertEquals(130, enemigoConfig.vidaMax);
        assertEquals(25, enemigoConfig.atq);
        assertEquals(12, enemigoConfig.def);
        assertEquals(1, enemigoConfig.vel);
    }

    @Test
    public void testEnemigoConfigDebil() {
        enemigoConfig.nombre = "Slime";
        enemigoConfig.vida = 5;
        enemigoConfig.vidaMax = 5;
        enemigoConfig.atq = 1;
        enemigoConfig.def = 0;
        enemigoConfig.vel = 1;
        enemigoConfig.xInicial = 0;
        enemigoConfig.yInicial = 0;

        assertEquals(5, enemigoConfig.vida);
        assertEquals(1, enemigoConfig.atq);
        assertEquals(0, enemigoConfig.def);
    }

    // ============================================================
    // TESTS DE OBJETOCONFIG
    // ============================================================

    @Test
    public void testObjetoConfigValoresPorDefecto() {
        assertNull(objetoConfig.nombre);
        assertNull(objetoConfig.desc);
        assertNull(objetoConfig.tipo);
        assertEquals(0, objetoConfig.valor);
        assertFalse(objetoConfig.equipable);
        assertFalse(objetoConfig.consumible);
    }

    @Test
    public void testObjetoConfigPocion() {
        objetoConfig.nombre = "Poción de vida";
        objetoConfig.desc = "Restaura 20 puntos de vida";
        objetoConfig.tipo = "POCION_VIDA";
        objetoConfig.valor = 20;
        objetoConfig.equipable = false;
        objetoConfig.consumible = true;

        assertEquals("Poción de vida", objetoConfig.nombre);
        assertEquals("Restaura 20 puntos de vida", objetoConfig.desc);
        assertEquals("POCION_VIDA", objetoConfig.tipo);
        assertEquals(20, objetoConfig.valor);
        assertFalse(objetoConfig.equipable);
        assertTrue(objetoConfig.consumible);
    }

    @Test
    public void testObjetoConfigArma() {
        objetoConfig.nombre = "Espada oxidada";
        objetoConfig.desc = "Aumenta 5 puntos de ataque";
        objetoConfig.tipo = "ARMA";
        objetoConfig.valor = 5;
        objetoConfig.equipable = true;
        objetoConfig.consumible = false;

        assertEquals("ARMA", objetoConfig.tipo);
        assertEquals(5, objetoConfig.valor);
        assertTrue(objetoConfig.equipable);
        assertFalse(objetoConfig.consumible);
    }

    @Test
    public void testObjetoConfigEscudo() {
        objetoConfig.nombre = "Escudo de madera";
        objetoConfig.desc = "Aumenta 3 puntos de defensa";
        objetoConfig.tipo = "ESCUDO";
        objetoConfig.valor = 3;
        objetoConfig.equipable = true;
        objetoConfig.consumible = false;

        assertEquals("ESCUDO", objetoConfig.tipo);
        assertEquals(3, objetoConfig.valor);
    }

    @Test
    public void testObjetoConfigLlave() {
        objetoConfig.nombre = "Llave oxidada";
        objetoConfig.desc = "Llave para abrir puertas";
        objetoConfig.tipo = "LLAVE";
        objetoConfig.valor = 0;
        objetoConfig.equipable = true;
        objetoConfig.consumible = false;

        assertEquals("LLAVE", objetoConfig.tipo);
        assertEquals(0, objetoConfig.valor);
    }

    @Test
    public void testObjetoConfigTiposValidos() {
        String[] tipos = {"POCION_VIDA", "ARMA", "ESCUDO", "LLAVE"};
        for (String tipo : tipos) {
            objetoConfig.tipo = tipo;
            assertEquals(tipo, objetoConfig.tipo);
        }
    }

    // ============================================================
    // TESTS DE PUERTACONFIG
    // ============================================================

    @Test
    public void testPuertaConfigValoresPorDefecto() {
        assertEquals(0, puertaConfig.x);
        assertEquals(0, puertaConfig.y);
        assertEquals(0, puertaConfig.idHabitacionDestino);
    }

    @Test
    public void testPuertaConfigConDatos() {
        puertaConfig.x = 3;
        puertaConfig.y = 2;
        puertaConfig.idHabitacionDestino = 5;

        assertEquals(3, puertaConfig.x);
        assertEquals(2, puertaConfig.y);
        assertEquals(5, puertaConfig.idHabitacionDestino);
    }

    @Test
    public void testPuertaConfigDestinoRaiz() {
        puertaConfig.idHabitacionDestino = 1;
        assertEquals(1, puertaConfig.idHabitacionDestino);
    }

    @Test
    public void testPuertaConfigDestinoLejano() {
        puertaConfig.idHabitacionDestino = 999;
        assertEquals(999, puertaConfig.idHabitacionDestino);
    }

    // ============================================================
    // TESTS DE JUGADORCONFIG
    // ============================================================

    @Test
    public void testJugadorConfigValoresPorDefecto() {
        assertNull(jugadorConfig.nombre);
        assertEquals(0, jugadorConfig.vida);
        assertEquals(0, jugadorConfig.vidaMax);
        assertEquals(0, jugadorConfig.atq);
        assertEquals(0, jugadorConfig.def);
        assertEquals(0, jugadorConfig.vel);
        assertEquals(0, jugadorConfig.habitacion);
        assertEquals(0, jugadorConfig.xInicial);
        assertEquals(0, jugadorConfig.yInicial);
    }

    @Test
    public void testJugadorConfigAventurero() {
        jugadorConfig.nombre = "Aventurero";
        jugadorConfig.vida = 100;
        jugadorConfig.vidaMax = 100;
        jugadorConfig.atq = 15;
        jugadorConfig.def = 5;
        jugadorConfig.vel = 3;
        jugadorConfig.habitacion = 1;
        jugadorConfig.xInicial = 0;
        jugadorConfig.yInicial = 0;

        assertEquals("Aventurero", jugadorConfig.nombre);
        assertEquals(100, jugadorConfig.vida);
        assertEquals(100, jugadorConfig.vidaMax);
        assertEquals(15, jugadorConfig.atq);
        assertEquals(5, jugadorConfig.def);
        assertEquals(3, jugadorConfig.vel);
        assertEquals(1, jugadorConfig.habitacion);
        assertEquals(0, jugadorConfig.xInicial);
        assertEquals(0, jugadorConfig.yInicial);
    }

    @Test
    public void testJugadorConfigDificil() {
        jugadorConfig.nombre = "Aventurero Experto";
        jugadorConfig.vida = 100;
        jugadorConfig.vidaMax = 100;
        jugadorConfig.atq = 10;
        jugadorConfig.def = 4;
        jugadorConfig.vel = 3;
        jugadorConfig.habitacion = 1;
        jugadorConfig.xInicial = 0;
        jugadorConfig.yInicial = 0;

        assertEquals(10, jugadorConfig.atq);
        assertEquals(4, jugadorConfig.def);
    }

    @Test
    public void testJugadorConfigPosicionInicial() {
        jugadorConfig.habitacion = 2;
        jugadorConfig.xInicial = 3;
        jugadorConfig.yInicial = 4;

        assertEquals(2, jugadorConfig.habitacion);
        assertEquals(3, jugadorConfig.xInicial);
        assertEquals(4, jugadorConfig.yInicial);
    }

    // ============================================================
    // TESTS DE INTEGRACIÓN - CONFIGURACIÓN COMPLETA
    // ============================================================

    @Test
    public void testConfiguracionCompleta() {
        // Construir una configuración como la del JSON
        config.turnosTotales = 30;

        // Jugador
        jugadorConfig.nombre = "Aventurero";
        jugadorConfig.vida = 100;
        jugadorConfig.vidaMax = 100;
        jugadorConfig.atq = 12;
        jugadorConfig.def = 5;
        jugadorConfig.vel = 3;
        jugadorConfig.habitacion = 1;
        jugadorConfig.xInicial = 0;
        jugadorConfig.yInicial = 0;
        config.jugador = jugadorConfig;

        // Habitación
        habConfig.id = 1;
        habConfig.filas = 4;
        habConfig.columnas = 4;
        habConfig.padreId = null;

        // Celda con enemigo
        celdaConfig.x = 1;
        celdaConfig.y = 2;
        celdaConfig.tipo = "VACIA";
        enemigoConfig.nombre = "Goblin";
        enemigoConfig.vida = 25;
        enemigoConfig.vidaMax = 25;
        enemigoConfig.atq = 6;
        enemigoConfig.def = 2;
        enemigoConfig.vel = 2;
        enemigoConfig.xInicial = 1;
        enemigoConfig.yInicial = 2;
        celdaConfig.enemigo = enemigoConfig;

        Lista<ConfigPartida.CeldaConfig> celdas = new Lista<>();
        celdas.add(celdaConfig);
        habConfig.celdas = celdas;

        Lista<ConfigPartida.HabitacionConfig> habitaciones = new Lista<>();
        habitaciones.add(habConfig);
        config.habitaciones = habitaciones;

        // Verificar
        assertEquals(30, config.turnosTotales);
        assertEquals("Aventurero", config.jugador.nombre);
        assertEquals(1, config.habitaciones.size());
        assertEquals(1, config.habitaciones.get(0).id);
        assertEquals(1, config.habitaciones.get(0).celdas.size());
        assertEquals("Goblin", config.habitaciones.get(0).celdas.get(0).enemigo.nombre);
    }

    @Test
    public void testConfiguracionConVariasHabitaciones() {
        config.turnosTotales = 35;
        config.jugador = jugadorConfig;

        ConfigPartida.HabitacionConfig h1 = new ConfigPartida.HabitacionConfig();
        h1.id = 1;
        h1.padreId = null;

        ConfigPartida.HabitacionConfig h2 = new ConfigPartida.HabitacionConfig();
        h2.id = 2;
        h2.padreId = 1;

        ConfigPartida.HabitacionConfig h3 = new ConfigPartida.HabitacionConfig();
        h3.id = 3;
        h3.padreId = 1;

        Lista<ConfigPartida.HabitacionConfig> habitaciones = new Lista<>();
        habitaciones.add(h1);
        habitaciones.add(h2);
        habitaciones.add(h3);
        config.habitaciones = habitaciones;

        assertEquals(3, config.habitaciones.size());
        assertNull(config.habitaciones.get(0).padreId);
        assertEquals(1, config.habitaciones.get(1).padreId.intValue());
        assertEquals(1, config.habitaciones.get(2).padreId.intValue());
    }
}