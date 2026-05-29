import Juego.Motor.MotorJuego;
import Juego.Persistencia.GestorJSON;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Pantalla de menú principal.
 * Permite iniciar una nueva partida (cargando un JSON de configuración),
 * cargar una partida guardada, o salir del juego.
 */
public class PantallaMenu {

    public Scene crearEscena() {
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(70, 80, 70, 80));
        root.setStyle("-fx-background-color: #0a0b0e;");

        // ── Título ──────────────────────────────────────────────────
        Label titulo = new Label("⚔  DUNGEON  CRAWLER  ⚔");
        titulo.setStyle(estilo("#d4a017", 34, true));

        Label subtitulo = new Label("~ Explora · Combate · Escapa ~");
        subtitulo.setStyle(estilo("#6a5a2a", 14, false));

        Region espacio1 = new Region();
        espacio1.setPrefHeight(20);

        Label sep = new Label("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sep.setStyle(estilo("#2a2010", 14, false));

        Region espacio2 = new Region();
        espacio2.setPrefHeight(10);

        // ── Botones ─────────────────────────────────────────────────
        Button btnNueva  = crearBoton("[ NUEVA PARTIDA ]");
        Button btnCargar = crearBoton("[ CARGAR PARTIDA ]");
        Button btnSalir  = crearBoton("[ SALIR ]");

        // ── Instrucción ─────────────────────────────────────────────
        Label instruccion = new Label("\"Nueva Partida\" requiere un JSON de configuración.\n"
                + "\"Cargar Partida\" requiere el guardado y la configuración original.");
        instruccion.setStyle(estilo("#504838", 11, false));
        instruccion.setWrapText(true);
        instruccion.setMaxWidth(400);
        instruccion.setAlignment(Pos.CENTER);

        // ── Acciones ────────────────────────────────────────────────
        btnNueva.setOnAction(e -> iniciarNuevaPartida());
        btnCargar.setOnAction(e -> cargarPartidaGuardada());
        btnSalir.setOnAction(e -> AppMain.primaryStage.close());

        root.getChildren().addAll(
                titulo, subtitulo, espacio1, sep, espacio2,
                btnNueva, btnCargar, btnSalir,
                new Region(), instruccion
        );

        return new Scene(root, 950, 700);
    }

    // ═══════════════════════════════════════════════════════════════
    // ACCIONES
    // ═══════════════════════════════════════════════════════════════

    private void iniciarNuevaPartida() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo de configuración JSON");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
        File archivo = fc.showOpenDialog(AppMain.primaryStage);
        if (archivo == null) return;

        try {
            GestorJSON gestor = new GestorJSON();
            MotorJuego motor = gestor.cargarConfig(archivo.getAbsolutePath());
            PantallaJuego pantalla = new PantallaJuego(motor, archivo.getAbsolutePath());
            AppMain.primaryStage.setScene(pantalla.crearEscena());
        } catch (IOException ex) {
            mostrarError("Error al cargar la configuración:\n" + ex.getMessage());
        }
    }

    private void cargarPartidaGuardada() {
        // Paso 1: seleccionar archivo de estado guardado
        FileChooser fcEstado = new FileChooser();
        fcEstado.setTitle("Seleccionar partida guardada (estado)");
        fcEstado.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
        File archivoEstado = fcEstado.showOpenDialog(AppMain.primaryStage);
        if (archivoEstado == null) return;

        // Paso 2: seleccionar archivo de configuración original
        FileChooser fcConfig = new FileChooser();
        fcConfig.setTitle("Seleccionar configuración original del juego");
        fcConfig.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
        File archivoConfig = fcConfig.showOpenDialog(AppMain.primaryStage);
        if (archivoConfig == null) return;

        try {
            GestorJSON gestor = new GestorJSON();
            MotorJuego motor = gestor.cargarPartida(
                    archivoEstado.getAbsolutePath(),
                    archivoConfig.getAbsolutePath()
            );
            PantallaJuego pantalla = new PantallaJuego(motor, archivoConfig.getAbsolutePath());
            AppMain.primaryStage.setScene(pantalla.crearEscena());
        } catch (IOException ex) {
            mostrarError("Error al cargar la partida guardada:\n" + ex.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPERS DE ESTILO
    // ═══════════════════════════════════════════════════════════════

    private Button crearBoton(String texto) {
        Button btn = new Button(texto);
        String estiloBase = estilo("#e8e0d0", 18, false)
                + "-fx-background-color: #12131a;"
                + "-fx-border-color: #2e2c1a; -fx-border-width: 1px;"
                + "-fx-padding: 12 40; -fx-cursor: hand;";
        String estiloHover = estilo("#d4a017", 18, false)
                + "-fx-background-color: #12131a;"
                + "-fx-border-color: #d4a017; -fx-border-width: 1px;"
                + "-fx-padding: 12 40; -fx-cursor: hand;";

        btn.setStyle(estiloBase);
        btn.setMinWidth(280);
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estiloBase));
        return btn;
    }

    private String estilo(String color, int size, boolean bold) {
        return "-fx-font-family: 'Courier New'; "
                + "-fx-font-size: " + size + "px; "
                + "-fx-text-fill: " + color + "; "
                + (bold ? "-fx-font-weight: bold; " : "");
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
