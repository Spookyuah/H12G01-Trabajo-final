import Juego.Modelo.*;
import Juego.Modelo.Objetos.Arma;
import Juego.Modelo.Objetos.Escudo;
import Juego.Modelo.Objetos.Objeto;
import Juego.Modelo.Objetos.Pocion;
import Juego.Motor.AccionJugador;
import Juego.Motor.MotorJuego;
import Juego.Persistencia.GestorJSON;
import Structures.Lista;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Pantalla principal del juego.
 *
 * Layout:
 *   ┌─────────────────── HEADER ──────────────────────┐
 *   │ Título        Habitación    Turnos  [Guardar]   │
 *   ├──────────────────┬──────────────────────────────┤
 *   │                  │  ◈ JUGADOR (stats + barra)   │
 *   │  GRID de la      │  ◈ INVENTARIO               │
 *   │  habitación      │  ◈ ACCIONES                  │
 *   │  (GridPane)      │     Movimiento seleccionado  │
 *   │                  │     Tipo de acción           │
 *   │                  │     Dirección (flechas)      │
 *   │                  │     [▶ EJECUTAR TURNO]       │
 *   ├──────────────────┴──────────────────────────────┤
 *   │  ◈ LOG DE EVENTOS (TextArea últimas entradas)   │
 *   └─────────────────────────────────────────────────┘
 *
 * Flujo de interacción:
 *   1. Celdas verdes = alcanzables → hacer clic para seleccionar movimiento.
 *   2. Elegir tipo de acción (NADA / ATACAR / USAR / RECOGER / ABRIR).
 *   3. Para ATACAR/RECOGER/ABRIR elegir dirección con las flechas.
 *      Para USAR hacer clic en el objeto del inventario.
 *   4. Pulsar "▶ EJECUTAR TURNO".
 */
public class PantallaJuego {

    private static final double ESCALA_TEXTO = 1.3;

    // ─── Modelo ──────────────────────────────────────────────────
    private final MotorJuego motor;
    private final String rutaConfig;      // Para "cargar partida" desde menú

    // ─── Estado de la acción pendiente ───────────────────────────
    private Posicion movimientoSeleccionado   = null;
    private AccionJugador.TipoAccion tipoAccion = AccionJugador.TipoAccion.NADA;
    private String   direccionSeleccionada    = null;
    private int      inventarioSlot           = -1;

    // ─── Referencias a componentes que se actualizan ─────────────
    private GridPane gridHabitacion;
    private Label    lblVida;
    private ProgressBar barraVida;
    private Label    lblStats;
    private Label    lblPosicion;
    private Label    lblTurnos;
    private Label    lblHabitacion;
    private Label    lblCamino;
    private TextArea areaLog;
    private VBox     inventarioContainer;
    private Label    lblMovSeleccionado;
    private Label    lblAccionPendiente;
    private Label    lblDireccionSel;

    // Botones de tipo de acción (para actualizar su estilo al seleccionar)
    private Button[] btnsAccion;

    // ═════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═════════════════════════════════════════════════════════════

    public PantallaJuego(MotorJuego motor, String rutaConfig) {
        this.motor = motor;
        this.rutaConfig = rutaConfig;
    }

    // ═════════════════════════════════════════════════════════════
    // CREAR ESCENA
    // ═════════════════════════════════════════════════════════════

    public Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0b0e;");

        root.setTop(crearHeader());
        root.setCenter(crearCentro());
        root.setBottom(crearPanelLog());

        actualizarTodo();

        Scene scene = new Scene(root, 1300, 850);

        // ✅ Añadir listener para cuando se redimensiona la ventana
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            actualizarGrid();
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            actualizarGrid();
        });

        return scene;
    }

    // ═════════════════════════════════════════════════════════════
    // HEADER
    // ═════════════════════════════════════════════════════════════

    private HBox crearHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 20, 12, 20));
        header.setStyle(
                "-fx-background-color: #0c0d12;"
                        + "-fx-border-color: #252010; -fx-border-width: 0 0 1 0;");

        Label titulo = new Label("⚔  DUNGEON CRAWLER");
        titulo.setStyle(estilo("#d4a017", 18, true));

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        lblCamino    = new Label();
        lblCamino.setStyle(estilo("#506840", 12, false));

        lblHabitacion = new Label("HAB: ?");
        lblHabitacion.setStyle(estilo("#907840", 13, false));

        lblTurnos = new Label("TURNOS: ?");
        lblTurnos.setStyle(estilo("#c0b080", 13, false));

        Button btnGuardar = crearBotonHeader("💾 Guardar");
        btnGuardar.setOnAction(e -> guardarPartida());

        Button btnMenu = crearBotonHeader("🏠 Menú");
        btnMenu.setOnAction(e -> {
            PantallaMenu menu = new PantallaMenu();
            AppMain.primaryStage.setScene(menu.crearEscena());
        });

        header.getChildren().addAll(titulo, espaciador, lblCamino, lblHabitacion, lblTurnos, btnGuardar, btnMenu);
        return header;
    }

    // ═════════════════════════════════════════════════════════════
    // CENTRO (GRID + PANEL DERECHO)
    // ═════════════════════════════════════════════════════════════

    private HBox crearCentro() {
        HBox centro = new HBox(12);
        centro.setPadding(new Insets(15));
        centro.setStyle("-fx-background-color: #0a0b0e;");
        centro.setAlignment(Pos.CENTER); // ✅ Centrar contenido

        // Grid en un StackPane para centrarlo
        StackPane contenedorGrid = new StackPane();
        contenedorGrid.setStyle("-fx-background-color: #0c0d12;");
        contenedorGrid.setPadding(new Insets(12));

        // ✅ Hacer que el contenedor crezca con la ventana
        contenedorGrid.setMinSize(400, 400);
        contenedorGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        gridHabitacion = new GridPane();
        gridHabitacion.setHgap(2);
        gridHabitacion.setVgap(2);
        gridHabitacion.setStyle("-fx-background-color: #050608;");
        gridHabitacion.setPadding(new Insets(4));
        gridHabitacion.setAlignment(Pos.CENTER); // ✅ Centrar la cuadrícula

        // Leyenda debajo del grid
        VBox wrapGrid = new VBox(8);
        wrapGrid.setAlignment(Pos.TOP_CENTER);
        wrapGrid.getChildren().addAll(gridHabitacion, crearLeyenda());
        contenedorGrid.getChildren().add(wrapGrid);

        ScrollPane scroll = new ScrollPane(contenedorGrid);
        scroll.setStyle("-fx-background: #0c0d12; -fx-background-color: #0c0d12;");
        scroll.setFitToHeight(true);  // ✅ Ajustar al alto disponible
        scroll.setFitToWidth(true);   // ✅ Ajustar al ancho disponible
        HBox.setHgrow(scroll, Priority.ALWAYS);

        // Panel derecho
        VBox panelDerecho = crearPanelDerecho();
        panelDerecho.setMinWidth(380);
        panelDerecho.setMaxWidth(380);

        centro.getChildren().addAll(scroll, panelDerecho);
        return centro;
    }

    private HBox crearLeyenda() {
        HBox leyenda = new HBox(12);
        leyenda.setAlignment(Pos.CENTER);
        leyenda.setPadding(new Insets(4, 0, 0, 0));

        String[][] items = {
                {"#4a9adf", "J Jugador"},
                {"#f04040", "E Enemigo"},
                {"#40c040", "O Objeto"},
                {"#d4a017", "🚪 Puerta"},
                {"#c040c0", "⚡ Trampa"},
                {"#3a3a3a", "▪ Pared"},
                {"#1a5a1a", "· Alcanzable"}
        };
        for (String[] it : items) {
            Label lbl = new Label(it[1]);
            lbl.setStyle(estilo(it[0], 10, false));
            leyenda.getChildren().add(lbl);
        }
        return leyenda;
    }

    // ═════════════════════════════════════════════════════════════
    // PANEL DERECHO
    // ═════════════════════════════════════════════════════════════

    private VBox crearPanelDerecho() {
        VBox panel = new VBox(6);
        panel.setPadding(new Insets(0, 0, 0, 0));
        panel.setStyle("-fx-background-color: #0a0b0e;");

        panel.getChildren().addAll(
                crearPanelJugador(),
                crearPanelInventario(),
                crearPanelAcciones()
        );
        VBox.setVgrow(panel.getChildren().get(2), Priority.ALWAYS);
        return panel;
    }

    // ── Panel de stats del jugador ────────────────────────────────

    private VBox crearPanelJugador() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(8));
        box.setStyle(estiloPanel());

        Label titulo = new Label("◈ JUGADOR");
        titulo.setStyle(estilo("#d4a017", 12, true));

        lblVida = new Label("HP: ? / ?");
        lblVida.setStyle(estilo("#d0a0a0", 12, false));

        barraVida = new ProgressBar(1.0);
        barraVida.setMaxWidth(Double.MAX_VALUE);
        barraVida.setPrefHeight(10);
        barraVida.setStyle("-fx-accent: #27ae60; -fx-background-color: #1a0808;");

        lblStats = new Label("⚔ ATQ: ?  🛡 DEF: ?  💨 VEL: ?");
        lblStats.setStyle(estilo("#9a9070", 11, false));

        lblPosicion = new Label("📍 Pos: (?,?)");
        lblPosicion.setStyle(estilo("#607080", 11, false));

        box.getChildren().addAll(titulo, lblVida, barraVida, lblStats, lblPosicion);
        return box;
    }

    // ── Panel de inventario ───────────────────────────────────────

    private VBox crearPanelInventario() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(8));
        box.setStyle(estiloPanel());
        box.setMaxHeight(220);

        Label titulo = new Label("◈ INVENTARIO  (clic para usar)");
        titulo.setStyle(estilo("#d4a017", 12, true));

        inventarioContainer = new VBox(2);
        ScrollPane scroll = new ScrollPane(inventarioContainer);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(170);

        box.getChildren().addAll(titulo, scroll);
        return box;
    }

    // ── Panel de acciones ─────────────────────────────────────────
    //  ↑ CORREGIDO: se eliminó el bucle for-each sobre btnsAccion
    //    (que todavía no estaba inicializado) y se colocó setWrapText
    //    dentro del bucle de creación de botones.

    private VBox crearPanelAcciones() {
        VBox box = new VBox(6);
        box.setPadding(new Insets(8));
        box.setStyle(estiloPanel());

        Label titulo = new Label("◈ ACCIONES DEL TURNO");
        titulo.setStyle(estilo("#d4a017", 12, true));

        // Movimiento seleccionado
        lblMovSeleccionado = new Label("Mover a: (sin mover)");
        lblMovSeleccionado.setStyle(estilo("#607080", 11, false));
        lblMovSeleccionado.setWrapText(true);

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #252010;");

        // Tipo de acción
        Label lblTipo = new Label("Tipo de acción:");
        lblTipo.setStyle(estilo("#807060", 11, false));

        HBox rowAccion = new HBox(4);
        // ❌ ELIMINADO: el bucle for (Button btn : btnsAccion) que causaba NullPointerException
        // Ahora creamos el array y los botones directamente.

        AccionJugador.TipoAccion[] tipos = AccionJugador.TipoAccion.values();
        btnsAccion = new Button[tipos.length];
        for (int i = 0; i < tipos.length; i++) {
            final AccionJugador.TipoAccion tipo = tipos[i];
            Button btn = new Button(etiquetaAccion(tipo));
            btn.setWrapText(true);   // ✅ Ahora cada botón permite texto multilínea
            btn.setStyle(estiloToggleBtn(tipo == AccionJugador.TipoAccion.NADA));
            btn.setOnAction(e -> {
                tipoAccion = tipo;
                direccionSeleccionada = null;
                actualizarEstilosBotonesAccion();
                actualizarResumenAccion();
            });
            btnsAccion[i] = btn;
            rowAccion.getChildren().add(btn);
        }

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: #252010;");

        // Dirección
        Label lblDir = new Label("Dirección (para atacar / recoger / abrir):");
        lblDir.setStyle(estilo("#807060", 11, false));
        lblDir.setWrapText(true);

        GridPane cruceta = new GridPane();
        cruceta.setHgap(6);
        cruceta.setVgap(6);

        Button bArriba    = crearBotonDir("↑ ARRIBA",    "ARRIBA");
        Button bAbajo     = crearBotonDir("↓ ABAJO",     "ABAJO");
        Button bIzquierda = crearBotonDir("← IZQ",       "IZQUIERDA");
        Button bDerecha   = crearBotonDir("→ DER",        "DERECHA");
        cruceta.add(bArriba,    1, 0);
        cruceta.add(bIzquierda, 0, 1);
        cruceta.add(bDerecha,   2, 1);
        cruceta.add(bAbajo,     1, 2);

        lblDireccionSel = new Label("Sin dirección seleccionada");
        lblDireccionSel.setStyle(estilo("#506870", 10, false));

        Separator sep3 = new Separator();
        sep3.setStyle("-fx-background-color: #252010;");

        // Resumen de la acción
        lblAccionPendiente = new Label("Acción: NADA");
        lblAccionPendiente.setStyle(estilo("#80a870", 11, false));
        lblAccionPendiente.setWrapText(true);

        // Botón ejecutar
        Button btnEjecutar = new Button("▶   EJECUTAR TURNO");
        btnEjecutar.setMaxWidth(Double.MAX_VALUE);
        String estiloEjec = estilo("#0a0a0a", 14, true)
                + "-fx-background-color: #c9a017; -fx-padding: 9 15; -fx-cursor: hand;";
        String estiloEjecHover = estilo("#0a0a0a", 14, true)
                + "-fx-background-color: #f0c830; -fx-padding: 9 15; -fx-cursor: hand;";
        btnEjecutar.setStyle(estiloEjec);
        btnEjecutar.setOnMouseEntered(e -> btnEjecutar.setStyle(estiloEjecHover));
        btnEjecutar.setOnMouseExited(e -> btnEjecutar.setStyle(estiloEjec));
        btnEjecutar.setOnAction(e -> ejecutarTurno());

        box.getChildren().addAll(
                titulo,
                lblMovSeleccionado,
                sep1,
                lblTipo, rowAccion,
                sep2,
                lblDir, cruceta, lblDireccionSel,
                sep3,
                lblAccionPendiente,
                btnEjecutar
        );
        return box;
    }

    // ═════════════════════════════════════════════════════════════
    // PANEL LOG
    // ═════════════════════════════════════════════════════════════

    private VBox crearPanelLog() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(8, 15, 8, 15));
        box.setMaxHeight(150);
        box.setStyle(
                "-fx-background-color: #0c0d12;"
                        + "-fx-border-color: #252010; -fx-border-width: 1 0 0 0;");

        Label titulo = new Label("◈ LOG DE EVENTOS");
        titulo.setStyle(estilo("#d4a017", 11, true));

        areaLog = new TextArea();
        areaLog.setEditable(false);
        areaLog.setWrapText(true);
        areaLog.setPrefHeight(100);
        areaLog.setStyle(
                "-fx-font-family: 'Courier New'; -fx-font-size: 13px;"
                        + "-fx-text-fill: #90b080; -fx-control-inner-background: #0a0b0e;"
                        + "-fx-border-color: #1c1e28; -fx-border-width: 1;");

        box.getChildren().addAll(titulo, areaLog);
        return box;
    }

    // ═════════════════════════════════════════════════════════════
    // ACTUALIZAR GRID
    // ═════════════════════════════════════════════════════════════

    private void actualizarGrid() {
        gridHabitacion.getChildren().clear();

        Habitacion hab    = motor.getMapa().getActual();
        Jugador    jugador = motor.getJugador();
        Lista<Posicion> alcanzables = motor.getMovimientosPosibles();

        // ✅ Calcular tamaño dinámico basado en el espacio disponible
        double anchoDisponible = gridHabitacion.getScene() != null ?
                gridHabitacion.getScene().getWidth() * 0.6 : 800;
        double altoDisponible = gridHabitacion.getScene() != null ?
                gridHabitacion.getScene().getHeight() * 0.7 : 600;

        int columnas = hab.getColumnas();
        int filas = hab.getFilas();

        // ✅ Calcular el tamaño máximo que puede tener cada celda
        int tamPorAncho = (int)((anchoDisponible - (columnas + 1) * 2) / columnas);
        int tamPorAlto = (int)((altoDisponible - (filas + 1) * 2) / filas);
        final int TAM = Math.min(tamPorAncho, tamPorAlto);

        // Limitar tamaño mínimo y máximo
        final int TAM_FINAL = Math.max(40, Math.min(TAM, 100));

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                Celda celda = hab.getCelda(fila, col);
                Posicion pos = new Posicion(fila, col);

                boolean esJugador   = jugador.getPosicion().equals(pos);
                boolean esAlcanzable = estaEnLista(alcanzables, pos);
                boolean esDestino   = (movimientoSeleccionado != null) && movimientoSeleccionado.equals(pos);

                StackPane cellaPane = new StackPane();
                cellaPane.setMinSize(TAM_FINAL, TAM_FINAL);
                cellaPane.setMaxSize(TAM_FINAL, TAM_FINAL);
                cellaPane.setStyle(estiloCell(celda, esJugador, esAlcanzable, esDestino));

                // Contenido visual con tamaño de fuente proporcional
                String simbolo    = simboloCell(celda, esJugador);
                String colorTexto = colorTextoCell(celda, esJugador);
                if (!simbolo.isEmpty()) {
                    Label lbl = new Label(simbolo);
                    int fontSize;

                    if (esJugador) {
                        fontSize = (int)(TAM_FINAL * 0.5); // Tamaño del jugador
                    } else if (celda.tieneEnemigo()) {
                        Enemigo e = celda.getEnemigo();
                        String nombreEnemigo = e.getNombre().toLowerCase();

                        if (nombreEnemigo.contains("dragon") || nombreEnemigo.contains("dragón")) {
                            fontSize = (int)(TAM_FINAL * 0.7); // 🐉 GRANDE
                        } else if (nombreEnemigo.contains("orco") || nombreEnemigo.contains("orc")) {
                            fontSize = (int)(TAM_FINAL * 0.55); // 👹 MEDIANO
                        } else if (nombreEnemigo.contains("goblin")) {
                            fontSize = (int)(TAM_FINAL * 0.4); // 👺 PEQUEÑO
                        } else {
                            fontSize = (int)(TAM_FINAL * 0.45); // Otros enemigos
                        }
                    } else {
                        fontSize = (int)(TAM_FINAL * 0.3); // Objetos y símbolos
                    }

                    fontSize = Math.max(10, Math.min(fontSize, 32));
                    lbl.setStyle(estilo(colorTexto, fontSize, esJugador));
                    cellaPane.getChildren().add(lbl);
                }

                // Tooltip informativo
                Tooltip.install(cellaPane, new Tooltip(tooltipCell(celda, esJugador, pos)));

                // Interacción: click para seleccionar destino de movimiento
                if (!esJugador && esAlcanzable && celda.traversable()) {
                    final int f = fila, c = col;
                    cellaPane.setStyle(cellaPane.getStyle() + " -fx-cursor: hand;");
                    cellaPane.setOnMouseClicked(ev -> seleccionarMovimiento(new Posicion(f, c)));
                } else if (esJugador) {
                    cellaPane.setStyle(cellaPane.getStyle() + " -fx-cursor: hand;");
                    cellaPane.setOnMouseClicked(ev -> {
                        movimientoSeleccionado = null;
                        actualizarResumenAccion();
                        actualizarGrid();
                    });
                }

                gridHabitacion.add(cellaPane, col, fila);
            }
        }
    }

    // ── Helpers de estilo de celda ──────────────────────────────

    private String estiloCell(Celda c, boolean esJugador, boolean esAlc, boolean esDest) {
        String bg, border;
        int bw = 1;
        if (esJugador) {
            bg = "#132840"; border = "#4a9adf"; bw = 2;
        } else if (esDest) {
            bg = "#0f2f0f"; border = "#30c030"; bw = 2;
        } else if (c.tieneEnemigo()) {
            bg = "#300808"; border = "#701010";
        } else if (c.tieneObjeto()) {
            bg = "#082008"; border = "#185818";
        } else {
            switch (c.getTipo()) {
                case Pared:  bg = "#141414"; border = "#242424"; break;
                case Puerta: bg = "#201800"; border = "#c09010"; break;
                case Trampa: bg = "#1a0820"; border = "#601060"; break;
                default:
                    bg = esAlc ? "#081808" : "#10111a";
                    border = esAlc ? "#1a4a1a" : "#18192a";
            }
        }
        return "-fx-background-color: " + bg + ";"
                + "-fx-border-color: " + border + ";"
                + "-fx-border-width: " + bw + "px;";
    }

    private String simboloCell(Celda c, boolean esJugador) {
        if (esJugador) {
            Jugador j = motor.getJugador();
            return emojiJugador();
        }
        if (c.tieneEnemigo()) {
            Enemigo e = c.getEnemigo();
            return emojiEnemigo(e.getNombre()); // ✅ Emoji según tipo de enemigo
        }
        if (c.tieneObjeto())    return "O";
        switch (c.getTipo()) {
            case Pared:   return "▪";
            case Puerta:  return "🚪";
            case Trampa:  return "⚡";
            default:      return "";
        }
    }

    private String colorTextoCell(Celda c, boolean esJugador) {
        if (esJugador)          return "#5ab0f0";
        if (c.tieneEnemigo())   return "#f04040";
        if (c.tieneObjeto())    return "#40c040";
        switch (c.getTipo()) {
            case Pared:  return "#383838";
            case Puerta: return "#d4a017";
            case Trampa: return "#c040c0";
            default:     return "#383848";
        }
    }

    private String tooltipCell(Celda c, boolean esJugador, Posicion pos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Celda (").append(pos.getX()).append(",").append(pos.getY()).append(")\n");
        if (esJugador) {
            Jugador j = motor.getJugador();
            sb.append("JUGADOR: ").append(j.getNombre()).append("\n");
            sb.append("HP: ").append(j.getVida()).append("/").append(j.getVidaMax()).append("\n");
            sb.append("ATQ: ").append(j.getAtq()).append("  DEF: ").append(j.getDef());
        } else if (c.tieneEnemigo()) {
            Enemigo en = c.getEnemigo();
            sb.append("ENEMIGO: ").append(en.getNombre()).append("\n");
            sb.append("HP: ").append(en.getVida()).append("/").append(en.getVidaMax()).append("\n");
            sb.append("ATQ: ").append(en.getAtq()).append("  DEF: ").append(en.getDef());
        } else if (c.tieneObjeto()) {
            Objeto obj = c.getObjeto();
            sb.append("OBJETO: ").append(obj.getNombre()).append("\n");
            // ✅ Verificar descripción
            String descripcion = obj.getDesc();
            if (descripcion != null && !descripcion.isEmpty()) {
                sb.append(descripcion);
            }
            // ✅ Mostrar estadísticas si es arma, escudo o Pocion
            if (obj instanceof Arma) {
                sb.append("\n⚔ Ataque: +").append(((Arma) obj).getBonusAtq());
            } else if (obj instanceof Escudo) {
                sb.append("\n🛡 Defensa: +").append(((Escudo) obj).getBonusDef());
            } else if (obj instanceof Pocion) {
                sb.append("\n💚 Curación: +").append(((Pocion) obj).getHeal());
            }
        } else {
            sb.append("Tipo: ").append(c.getTipo().name());
            if (c.esPuerta() && c.getIdHabitacionDestino() >= 0)
                sb.append(" → Habitación ").append(c.getIdHabitacionDestino());
        }
        return sb.toString();
    }
    // ═════════════════════════════════════════════════════════════
    // ACTUALIZAR INVENTARIO
    // ═════════════════════════════════════════════════════════════

    private void actualizarInventario() {
        inventarioContainer.getChildren().clear();
        Inventario inv = motor.getJugador().getInventario();

        if (inv.estaVacio()) {
            Label lbl = new Label("(inventario vacío)");
            lbl.setStyle(estilo("#484038", 11, false));
            inventarioContainer.getChildren().add(lbl);
            return;
        }

        for (int i = 0; i < inv.getCantidad(); i++) {
            Objeto obj  = inv.getObjeto(i);
            final int idx = i;
            boolean sel = (inventarioSlot == i);

            HBox fila = new HBox(6);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setPadding(new Insets(3, 6, 3, 6));
            fila.setStyle(
                    "-fx-background-color: " + (sel ? "#0f2010" : "#0c0d14") + ";"
                            + "-fx-border-color: " + (sel ? "#30803a" : "#1a1c2a") + ";"
                            + "-fx-border-width: 1; -fx-cursor: hand;");

            String icono = (obj instanceof Pocion) ? "🧪" :
                    (obj instanceof Arma)   ? "⚔"  :
                            (obj instanceof Escudo) ? "🛡"  : "◆";

            Label lblIdx    = new Label("[" + i + "]");
            lblIdx.setStyle(estilo("#444040", 10, false));

            Label lblNombre = new Label(icono + " " + obj.getNombre());
            lblNombre.setStyle(estilo(sel ? "#60d060" : "#a8a080", 11, false));

            fila.getChildren().addAll(lblIdx, lblNombre);
            fila.setOnMouseClicked(e -> {
                inventarioSlot = (inventarioSlot == idx) ? -1 : idx;
                // Si hay un objeto seleccionado, cambiar tipo acción a USAR
                if (inventarioSlot >= 0) {
                    tipoAccion = AccionJugador.TipoAccion.USAR;
                    actualizarEstilosBotonesAccion();
                }
                actualizarInventario();
                actualizarResumenAccion();
            });

            inventarioContainer.getChildren().add(fila);
        }

        Label cap = new Label("  " + inv.getCantidad() + " / 10 objetos");
        cap.setStyle(estilo("#484038", 10, false));
        inventarioContainer.getChildren().add(cap);
    }

    // ═════════════════════════════════════════════════════════════
    // ACTUALIZAR INFO DEL JUGADOR
    // ═════════════════════════════════════════════════════════════

    private void actualizarInfoJugador() {
        Jugador j = motor.getJugador();
        lblVida.setText("❤ " + j.getNombre() + "   HP: " + j.getVida() + " / " + j.getVidaMax());

        double pct = (double) j.getVida() / Math.max(1, j.getVidaMax());
        barraVida.setProgress(pct);
        String colorBarra = pct > 0.6 ? "#27ae60" : pct > 0.3 ? "#e67e22" : "#c0392b";
        barraVida.setStyle("-fx-accent: " + colorBarra + "; -fx-background-color: #1a0808;");

        lblStats.setText("⚔ ATQ: " + j.getAtq()
                + "   🛡 DEF: " + j.getDef()
                + "   💨 VEL: " + j.getVel());
        lblPosicion.setText("📍 Posición: " + j.getPosicion());
    }

    // ═════════════════════════════════════════════════════════════
    // ACTUALIZAR HEADER
    // ═════════════════════════════════════════════════════════════

    private void actualizarHeader() {
        Habitacion hab = motor.getMapa().getActual();
        lblHabitacion.setText("HAB: " + hab.getId()
                + " (" + hab.getFilas() + "×" + hab.getColumnas() + ")");
        lblTurnos.setText("TURNOS: " + motor.getTurnosRestantes());

        // Distancia mínima hasta la salida (RF#3 del enunciado)
        try {
            Lista<Habitacion> salidas = motor.getMapa().getSalidas();
            if (salidas != null && salidas.size() > 0) {
                Lista<Habitacion> camino = motor.getMapa().caminoA(salidas.get(0));
                int habs = (camino != null) ? Math.max(0, camino.size() - 1) : 0;
                lblCamino.setText("🗺 Habitaciones hasta salida: " + habs);
            }
        } catch (Exception ignored) {
            lblCamino.setText("");
        }
    }

    // ═════════════════════════════════════════════════════════════
    // ACTUALIZAR LOG
    // ═════════════════════════════════════════════════════════════

    private void actualizarLog() {
        Lista<String> log = motor.getLog();
        StringBuilder sb  = new StringBuilder();
        int inicio = Math.max(0, log.size() - 10);
        for (int i = inicio; i < log.size(); i++) {
            sb.append("[").append(i + 1).append("] ").append(log.get(i)).append("\n");
        }
        areaLog.setText(sb.toString());
        areaLog.setScrollTop(Double.MAX_VALUE);
    }

    // ═════════════════════════════════════════════════════════════
    // ACCIONES DE JUEGO
    // ═════════════════════════════════════════════════════════════

    private void seleccionarMovimiento(Posicion pos) {
        movimientoSeleccionado = pos;
        actualizarResumenAccion();
        actualizarGrid();
    }

    private void actualizarResumenAccion() {
        if (lblMovSeleccionado != null) {
            lblMovSeleccionado.setText(movimientoSeleccionado != null
                    ? "Mover a: " + movimientoSeleccionado
                    : "Mover a: (sin mover, quedar en " + motor.getJugador().getPosicion() + ")");
        }
        if (lblAccionPendiente != null) {
            lblAccionPendiente.setText("Acción: " + describirAccionPendiente());
        }
    }

    private String describirAccionPendiente() {
        String mov = (movimientoSeleccionado != null) ? "Mover→" + movimientoSeleccionado + " + " : "";
        switch (tipoAccion) {
            case ATACAR:  return mov + "ATACAR " + (direccionSeleccionada != null ? direccionSeleccionada : "?");
            case USAR:    return mov + "USAR obj[" + (inventarioSlot >= 0 ? inventarioSlot : "?") + "]";
            case RECOGER: return mov + "RECOGER " + (direccionSeleccionada != null ? direccionSeleccionada : "?");
            case ABRIR:   return mov + "ABRIR " + (direccionSeleccionada != null ? direccionSeleccionada : "?");
            default:      return mov.isEmpty() ? "NADA (pasar turno)" : mov + "NADA";
        }
    }

    private void ejecutarTurno() {
        if (motor.esPartidaTerminada()) {
            mostrarPantallaFin();
            return;
        }

        AccionJugador accion = construirAccion();
        if (accion == null) return;   // Validación fallida, ya se mostró alerta

        motor.ejecutarTurnoJugador(accion);

        // Resetear estado de la UI para el siguiente turno
        movimientoSeleccionado = null;
        tipoAccion             = AccionJugador.TipoAccion.NADA;
        direccionSeleccionada  = null;
        inventarioSlot         = -1;

        actualizarTodo();

        if (motor.esPartidaTerminada()) {
            mostrarPantallaFin();
        }
    }

    private AccionJugador construirAccion() {
        switch (tipoAccion) {
            case NADA:
                return (movimientoSeleccionado != null)
                        ? AccionJugador.mover(movimientoSeleccionado)
                        : AccionJugador.nada();

            case ATACAR:
                if (direccionSeleccionada == null) {
                    mostrarAlerta("Selecciona una dirección para atacar (↑↓←→).");
                    return null;
                }
                return (movimientoSeleccionado != null)
                        ? AccionJugador.moverYAtacar(movimientoSeleccionado, direccionSeleccionada)
                        : AccionJugador.atacar(direccionSeleccionada);

            case USAR:
                if (inventarioSlot < 0) {
                    mostrarAlerta("Haz clic en un objeto del inventario para seleccionarlo.");
                    return null;
                }
                return (movimientoSeleccionado != null)
                        ? AccionJugador.moverYUsar(movimientoSeleccionado, inventarioSlot)
                        : AccionJugador.usar(inventarioSlot);

            case RECOGER:
                if (direccionSeleccionada == null) {
                    mostrarAlerta("Selecciona una dirección para recoger el objeto.");
                    return null;
                }
                return (movimientoSeleccionado != null)
                        ? AccionJugador.moverYRecoger(movimientoSeleccionado, direccionSeleccionada)
                        : AccionJugador.recoger(direccionSeleccionada);

            case ABRIR:
                if (direccionSeleccionada == null) {
                    mostrarAlerta("Selecciona la dirección donde está la puerta.");
                    return null;
                }
                return (movimientoSeleccionado != null)
                        ? AccionJugador.moverYAbrir(movimientoSeleccionado, direccionSeleccionada)
                        : AccionJugador.abrir(direccionSeleccionada);

            default:
                return AccionJugador.nada();
        }
    }

    private void actualizarTodo() {
        actualizarGrid();
        actualizarInfoJugador();
        actualizarInventario();
        actualizarResumenAccion();
        actualizarEstilosBotonesAccion();
        actualizarLog();
        actualizarHeader();
    }

    private void guardarPartida() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar partida");
        fc.setInitialFileName("partida_guardada.json");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
        File archivo = fc.showSaveDialog(AppMain.primaryStage);
        if (archivo != null) {
            try {
                new GestorJSON().guardarPartida(motor, archivo.getAbsolutePath());
                mostrarAlerta("✅ Partida guardada en:\n" + archivo.getName());
            } catch (IOException ex) {
                mostrarAlerta("❌ Error al guardar:\n" + ex.getMessage());
            }
        }
    }

    private void mostrarPantallaFin() {
        Lista<String> logFinal = motor.getLog();
        PantallaFin fin = new PantallaFin(motor.esVictoria(), motor.getJugador(), logFinal);
        AppMain.primaryStage.setScene(fin.crearEscena());
    }

    // ═════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════
    /**
     * Devuelve el emoji correspondiente al jugador según su nombre.
     */
    private String emojiJugador() {
        return "ඬ";
    }

    private String emojiEnemigo(String nombre) {
        String nombreLower = nombre.toLowerCase();
        if (nombreLower.contains("orco") || nombreLower.contains("orc")) {
            return "👹";
        } else if (nombreLower.contains("dragon") || nombreLower.contains("Dragon")) {
            return "🐉";
        } else if (nombreLower.contains("goblin") || nombreLower.contains("trasgo")) {
            return "👺";
        }
        return null;
    }
    private boolean estaEnLista(Lista<Posicion> lista, Posicion pos) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equals(pos)) return true;
        }
        return false;
    }

    private String etiquetaAccion(AccionJugador.TipoAccion tipo) {
        switch (tipo) {
            case NADA:    return "NADA";
            case ATACAR:  return "⚔ ATQ";
            case USAR:    return "💊 USO";
            case RECOGER: return "🤲 COG";
            case ABRIR:   return "🚪 ABR";
            default:      return tipo.name();
        }
    }

    private Button crearBotonDir(String texto, String dir) {
        Button btn = new Button(texto);
        String esBase = estilo("#a0a080", 12, false)
                + "-fx-background-color: #141520; -fx-border-color: #252535;"
                + "-fx-border-width: 1; -fx-padding: 6 12; -fx-cursor: hand;";
        String esHov = estilo("#d4a017", 12, false)
                + "-fx-background-color: #141520; -fx-border-color: #d4a017;"
                + "-fx-border-width: 1; -fx-padding: 4 8; -fx-cursor: hand;";
        btn.setMinWidth(62);
        btn.setStyle(esBase);
        btn.setOnMouseEntered(e -> btn.setStyle(esHov));
        btn.setOnMouseExited(e -> btn.setStyle(esBase));
        btn.setOnAction(e -> {
            direccionSeleccionada = dir;
            lblDireccionSel.setText("← " + dir + " seleccionado");
            actualizarResumenAccion();
        });
        return btn;
    }

    private void actualizarEstilosBotonesAccion() {
        if (btnsAccion == null) return;
        AccionJugador.TipoAccion[] tipos = AccionJugador.TipoAccion.values();
        for (int i = 0; i < btnsAccion.length; i++) {
            if (btnsAccion[i] != null)
                btnsAccion[i].setStyle(estiloToggleBtn(tipos[i] == tipoAccion));
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dungeon Crawler");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // ═════════════════════════════════════════════════════════════
    // ESTILOS
    // ═════════════════════════════════════════════════════════════

    private String estilo(String color, int size, boolean bold) {
        int scaledSize = (int)(size * ESCALA_TEXTO); // ✅ Usa la constante
        return "-fx-font-family: 'Courier New'; "
                + "-fx-font-size: " + scaledSize + "px; "
                + "-fx-text-fill: " + color + "; "
                + (bold ? "-fx-font-weight: bold; " : "");
    }

    private String estiloPanel() {
        return "-fx-background-color: #0c0d12; -fx-border-color: #1c1e2c; -fx-border-width: 1;";
    }

    private String estiloToggleBtn(boolean activo) {
        return "-fx-font-family: 'Courier New'; -fx-font-size: 12px;"
                + "-fx-text-fill: " + (activo ? "#d4a017" : "#707060") + ";"
                + "-fx-background-color: " + (activo ? "#201800" : "#101118") + ";"
                + "-fx-border-color: " + (activo ? "#d4a017" : "#252535") + ";"
                + "-fx-border-width: 1; -fx-padding: 4 8; -fx-cursor: hand;";
    }

    private Button crearBotonHeader(String texto) {
        Button btn = new Button(texto);
        String esBase = estilo("#909070", 12, false)
                + "-fx-background-color: #141520; -fx-border-color: #252535;"
                + "-fx-border-width: 1; -fx-padding: 5 10; -fx-cursor: hand;";
        btn.setStyle(esBase);
        btn.setOnMouseEntered(e -> btn.setStyle(estilo("#d4a017", 12, false)
                + "-fx-background-color: #141520; -fx-border-color: #d4a017;"
                + "-fx-border-width: 1; -fx-padding: 5 10; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle(esBase));
        return btn;
    }
}