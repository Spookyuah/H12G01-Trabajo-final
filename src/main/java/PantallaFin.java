import Juego.Modelo.Jugador;
import Structures.Lista;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Pantalla de fin de partida.
 * Muestra el resultado (victoria / derrota), las estadísticas finales
 * del jugador y el log completo de la partida.
 */
public class PantallaFin {

    private final boolean  victoria;
    private final Jugador  jugador;
    private final Lista<String> log;

    public PantallaFin(boolean victoria, Jugador jugador, Lista<String> log) {
        this.victoria = victoria;
        this.jugador  = jugador;
        this.log      = log;
    }

    public Scene crearEscena() {
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(45, 70, 45, 70));
        root.setStyle("-fx-background-color: #0a0b0e;");

        // ── Resultado ─────────────────────────────────────────────
        String textoResultado = victoria ? "⚔  ¡VICTORIA!  ⚔" : "💀  DERROTA  💀";
        String colorResultado = victoria ? "#d4a017" : "#c0392b";

        Label lblResultado = new Label(textoResultado);
        lblResultado.setStyle(estilo(colorResultado, 40, true));

        Label lblSubtitulo = new Label(
                victoria ? "¡Has escapado del dungeon!" : "Las sombras te han consumido...");
        lblSubtitulo.setStyle(estilo("#6a5040", 15, false));

        // ── Stats del jugador ─────────────────────────────────────
        HBox stats = new HBox(30);
        stats.setAlignment(Pos.CENTER);

        stats.getChildren().addAll(
                crearStat("JUGADOR",  jugador.getNombre(),  "#a09070"),
                crearStat("❤ VIDA",   jugador.getVida() + " / " + jugador.getVidaMax(), "#c07070"),
                crearStat("⚔ ATAQUE", String.valueOf(jugador.getAtq()),  "#c09040"),
                crearStat("🛡 DEFENSA", String.valueOf(jugador.getDef()), "#6090c0")
        );

        // ── Log completo ──────────────────────────────────────────
        Label lblTituloLog = new Label("── REGISTRO COMPLETO DE LA PARTIDA ──");
        lblTituloLog.setStyle(estilo("#503820", 12, false));

        TextArea areaLog = new TextArea();
        areaLog.setEditable(false);
        areaLog.setWrapText(true);
        areaLog.setPrefHeight(260);
        areaLog.setMaxWidth(700);
        areaLog.setStyle(
                "-fx-font-family: 'Courier New'; -fx-font-size: 11px;"
                        + "-fx-text-fill: #88a878; -fx-control-inner-background: #0c0d12;"
                        + "-fx-border-color: #252010; -fx-border-width: 1;");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < log.size(); i++) {
            sb.append("[").append(i + 1).append("] ").append(log.get(i)).append("\n");
        }
        areaLog.setText(sb.toString());

        // ── Botón volver ──────────────────────────────────────────
        Button btnMenu = new Button("[ VOLVER AL MENÚ ]");
        String esBase = estilo("#e8e0d0", 16, false)
                + "-fx-background-color: #12131a;"
                + "-fx-border-color: " + colorResultado + "; -fx-border-width: 1;"
                + "-fx-padding: 10 35; -fx-cursor: hand;";
        String esHov = estilo(colorResultado, 16, true)
                + "-fx-background-color: #12131a;"
                + "-fx-border-color: " + colorResultado + "; -fx-border-width: 1;"
                + "-fx-padding: 10 35; -fx-cursor: hand;";
        btnMenu.setStyle(esBase);
        btnMenu.setOnMouseEntered(e -> btnMenu.setStyle(esHov));
        btnMenu.setOnMouseExited(e -> btnMenu.setStyle(esBase));
        btnMenu.setOnAction(e -> {
            PantallaMenu menu = new PantallaMenu();
            AppMain.primaryStage.setScene(menu.crearEscena());
        });

        root.getChildren().addAll(lblResultado, lblSubtitulo, stats, lblTituloLog, areaLog, btnMenu);

        return new Scene(root, 950, 720);
    }

    // ═════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════

    private VBox crearStat(String etiqueta, String valor, String color) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8, 16, 8, 16));
        box.setStyle("-fx-background-color: #0c0d12; -fx-border-color: #1e2030; -fx-border-width: 1;");

        Label lblEtq = new Label(etiqueta);
        lblEtq.setStyle(estilo("#504838", 10, false));

        Label lblVal = new Label(valor);
        lblVal.setStyle(estilo(color, 15, true));

        box.getChildren().addAll(lblEtq, lblVal);
        return box;
    }

    private String estilo(String color, int size, boolean bold) {
        return "-fx-font-family: 'Courier New'; "
                + "-fx-font-size: " + size + "px; "
                + "-fx-text-fill: " + color + "; "
                + (bold ? "-fx-font-weight: bold; " : "");
    }
}
