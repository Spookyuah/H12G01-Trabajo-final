import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación JavaFX.
 * Arranca la pantalla de menú principal.
 */
public class AppMain extends Application {

    /** Referencia global al Stage para cambiar escenas desde cualquier pantalla. */
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("⚔ Dungeon Crawler");
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(700);
        primaryStage.setResizable(true);

        PantallaMenu menu = new PantallaMenu();
        primaryStage.setScene(menu.crearEscena());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
