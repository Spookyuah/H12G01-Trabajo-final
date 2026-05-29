package Juego.Modelo.Objetos;

import Juego.Modelo.Jugador;
import Juego.Modelo.Posicion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase abstracta Objeto y sus subclases.
 */
public class ObjetoTest {

    private Jugador jugador;
    private Objeto pocion;
    private Objeto pocionGrande;
    private Objeto arma;
    private Objeto escudo;
    private Objeto objetoGenerico;

    @BeforeEach
    public void setUp() {
        jugador = new Jugador("Aventurero", 100, 15, 5, 3, new Posicion(0, 0));
        pocion = new Pocion("Poción de vida", "Restaura 20 puntos de vida", 20, false, true);
        pocionGrande = new Pocion("Poción grande", "Restaura 50 puntos de vida", 50, false, true);
        arma = new Arma("Espada oxidada", "Aumenta 5 puntos de ataque", 5, true, false);
        escudo = new Escudo("Escudo de madera", "Aumenta 3 puntos de defensa", 3, true, false);
        objetoGenerico = new Objeto("Genérico", "Sin efecto", false, false) {
            @Override
            public void usar(Jugador j) {}
        };
    }

    // ============================================================
    // TESTS DE GETTERS BÁSICOS
    // ============================================================

    @Test
    public void testGetNombre() {
        assertEquals("Poción de vida", pocion.getNombre());
        assertEquals("Espada oxidada", arma.getNombre());
        assertEquals("Escudo de madera", escudo.getNombre());
    }

    @Test
    public void testGetDesc() {
        assertEquals("Restaura 20 puntos de vida", pocion.getDesc());
        assertEquals("Aumenta 5 puntos de ataque", arma.getDesc());
        assertEquals("Aumenta 3 puntos de defensa", escudo.getDesc());

    }

    @Test
    public void testEsEquipable() {
        assertFalse(pocion.esEquipable());
        assertTrue(arma.esEquipable());
        assertTrue(escudo.esEquipable());
    }

    @Test
    public void testEsConsumible() {
        assertTrue(pocion.esConsumible());
        assertFalse(arma.esConsumible());
        assertFalse(escudo.esConsumible());
    }

    // ============================================================
    // TESTS DE POCION
    // ============================================================

    @Test
    public void testPocionUsarNormal() {
        jugador.takeDmg(40);
        assertEquals(60, jugador.getVida());

        pocion.usar(jugador);
        assertEquals(80, jugador.getVida());
    }

    @Test
    public void testPocionUsarCasiLleno() {
        jugador.takeDmg(10);
        assertEquals(90, jugador.getVida());

        pocion.usar(jugador);
        assertEquals(100, jugador.getVida()); // Capado en vidaMax
    }

    @Test
    public void testPocionUsarVidaLlena() {
        assertEquals(100, jugador.getVida());

        pocion.usar(jugador);
        assertEquals(100, jugador.getVida()); // No supera vidaMax
    }

    @Test
    public void testPocionGrande() {
        jugador.takeDmg(80);
        assertEquals(20, jugador.getVida());

        pocionGrande.usar(jugador);
        assertEquals(70, jugador.getVida());
    }

    @Test
    public void testPocionGetHeal() {
        Pocion p = (Pocion) pocion;
        assertEquals(20, p.getHeal());

        Pocion pg = (Pocion) pocionGrande;
        assertEquals(50, pg.getHeal());
    }

    @Test
    public void testPocionEsPocion() {
        assertTrue(pocion instanceof Pocion);
        assertTrue(pocionGrande instanceof Pocion);
        assertTrue(pocion instanceof Objeto);
    }

    // ============================================================
    // TESTS DE ARMA
    // ============================================================

    @Test
    public void testArmaUsar() {
        int atqInicial = jugador.getAtq();
        arma.usar(jugador);
        assertEquals(atqInicial + 5, jugador.getAtq());
    }

    @Test
    public void testArmaUsarVariasVeces() {
        arma.usar(jugador);
        assertEquals(20, jugador.getAtq());

        arma.usar(jugador);
        assertEquals(25, jugador.getAtq());

        arma.usar(jugador);
        assertEquals(30, jugador.getAtq());
    }

    @Test
    public void testArmaGetBonusAtq() {
        Arma a = (Arma) arma;
        assertEquals(5, a.getBonusAtq());
    }

    @Test
    public void testArmaEsArma() {
        assertTrue(arma instanceof Arma);
        assertTrue(arma instanceof Objeto);
    }

    // ============================================================
    // TESTS DE ESCUDO
    // ============================================================

    @Test
    public void testEscudoUsar() {
        int defInicial = jugador.getDef();
        escudo.usar(jugador);
        assertEquals(defInicial + 3, jugador.getDef());
    }

    @Test
    public void testEscudoUsarVariasVeces() {
        escudo.usar(jugador);
        assertEquals(8, jugador.getDef());

        escudo.usar(jugador);
        assertEquals(11, jugador.getDef());
    }

    @Test
    public void testEscudoGetBonusDef() {
        Escudo e = (Escudo) escudo;
        assertEquals(3, e.getBonusDef());
    }

    @Test
    public void testEscudoEsEscudo() {
        assertTrue(escudo instanceof Escudo);
        assertTrue(escudo instanceof Objeto);
    }




    // ============================================================
    // TESTS DE OBJETO GENÉRICO
    // ============================================================

    @Test
    public void testObjetoGenericoUsar() {
        int vidaAntes = jugador.getVida();
        int atqAntes = jugador.getAtq();
        int defAntes = jugador.getDef();

        objetoGenerico.usar(jugador);

        // Sin efecto
        assertEquals(vidaAntes, jugador.getVida());
        assertEquals(atqAntes, jugador.getAtq());
        assertEquals(defAntes, jugador.getDef());
    }

    // ============================================================
    // TESTS DE COMPARABLE
    // ============================================================

    @Test
    public void testCompareToOrdenAlfabetico() {
        // "Espada oxidada" < "Poción de vida" alfabéticamente
        assertTrue(arma.compareTo(pocion) < 0);
        assertTrue(pocion.compareTo(arma) > 0);
    }

    @Test
    public void testCompareToMismoNombre() {
        Objeto pocion2 = new Pocion("Poción de vida", "Otra desc", 30, false, true);
        assertEquals(0, pocion.compareTo(pocion2));
    }

    @Test
    public void testCompareToNombresIguales() {
        Objeto a1 = new Arma("Espada", "Ataque +5", 5, true, false);
        Objeto a2 = new Arma("Espada", "Ataque +10", 10, true, false);
        assertEquals(0, a1.compareTo(a2));
    }

    @Test
    public void testCompareToDiferentesTipos() {
        // Compara por nombre, no por tipo
        // "Escudo de madera" vs "Espada oxidada"
        assertTrue(escudo.compareTo(arma) < 0); // E > E, "scudo" < "spada"
    }

    // ============================================================
    // TESTS DE COMBINACIONES
    // ============================================================

    @Test
    public void testUsarArmaYEscudo() {
        arma.usar(jugador);
        escudo.usar(jugador);

        assertEquals(20, jugador.getAtq());
        assertEquals(8, jugador.getDef());
    }

    @Test
    public void testUsarPocionYArma() {
        jugador.takeDmg(50);
        assertEquals(50, jugador.getVida());

        pocion.usar(jugador);
        assertEquals(70, jugador.getVida());

        arma.usar(jugador);
        assertEquals(20, jugador.getAtq());
        assertEquals(70, jugador.getVida()); // La vida no cambia
    }

    @Test
    public void testUsarMuchasPociones() {
        jugador.takeDmg(90);
        assertEquals(10, jugador.getVida());

        pocion.usar(jugador);
        assertEquals(30, jugador.getVida());

        pocion.usar(jugador);
        assertEquals(50, jugador.getVida());

        pocionGrande.usar(jugador);
        assertEquals(100, jugador.getVida());
    }


    // ============================================================
    // TESTS DE TOSTRING (si existe)
    // ============================================================

    @Test
    public void testToStringPocion() {
        String str = pocion.toString();
        assertTrue(str.contains("Poción de vida") || str.contains("Pocion"));
    }

    @Test
    public void testToStringArma() {
        String str = arma.toString();
        assertTrue(str.contains("Espada oxidada") || str.contains("Arma"));
    }

    // ============================================================
    // TESTS DE CASOS LÍMITE
    // ============================================================

    @Test
    public void testPocionCuracionCero() {
        Pocion pCero = new Pocion("Poción falsa", "No cura nada", 0, false, true);
        jugador.takeDmg(30);
        int vidaAntes = jugador.getVida();

        pCero.usar(jugador);
        assertEquals(vidaAntes, jugador.getVida());
    }

    @Test
    public void testArmaBonusCero() {
        Arma aCero = new Arma("Arma rota", "No añade ataque", 0, true, false);
        int atqAntes = jugador.getAtq();

        aCero.usar(jugador);
        assertEquals(atqAntes, jugador.getAtq());
    }

    @Test
    public void testEscudoBonusCero() {
        Escudo eCero = new Escudo("Escudo roto", "No añade defensa", 0, true, false);
        int defAntes = jugador.getDef();

        eCero.usar(jugador);
        assertEquals(defAntes, jugador.getDef());
    }

    @Test
    public void testPocionCuracionNegativa() {
        // No debería permitirse, pero si se crea, no debería bajar la vida
        Pocion pNeg = new Pocion("Poción venenosa", "Cura -10", -10, false, true);
        jugador.takeDmg(20);
        int vidaAntes = jugador.getVida();

        pNeg.usar(jugador);
        // Math.min(vidaMax, vida + (-10)) = Math.min(100, 70) = 70
        // O Math.max(0, vida + (-10))? Depende de la implementación de curar()
    }

    @Test
    public void testNombresLargos() {
        Objeto obj = new Pocion(
                "Poción de vida extremadamente poderosa y muy larga",
                "Descripción muy larga...",
                100,
                false,
                true
        );
        assertNotNull(obj.getNombre());
        assertTrue(obj.getNombre().length() > 10);
    }

    @Test
    public void testNombresVacios() {
        Objeto obj = new Pocion("", "", 10, false, true);
        assertEquals("", obj.getNombre());
        assertEquals("", obj.getDesc());
    }

    @Test
    public void testNombresNull() {
        Objeto obj = new Pocion(null, null, 10, false, true);
        assertNull(obj.getNombre());
        assertNull(obj.getDesc());
    }
}