package Structures.Arrays;


class ArrayTest {
    public static void main(String[] args) {
        System.out.println("  TEST DE LISTA ");

        // Test constructor y add()
        Lista<String> lista = new Lista<>();
        System.out.println(">>> Lista recién creada (debe estar vacía):");
        System.out.println("    Tamaño: " + lista.size());
        System.out.println("    ¿Está vacía?: " + lista.isEmpty());
        System.out.println();

        // Test add() y toString()
        lista.add("Habitacion");
        lista.add("Jugador");
        lista.add("Enemigo");
        lista.add("Objeto");
        System.out.println(">>> Después de añadir 4 elementos:");
        System.out.println("    Tamaño: " + lista.size());
        System.out.println("    ¿Está vacía?: " + lista.isEmpty());
        System.out.print("    Contenido: ");
        imprimirLista(lista);
        System.out.println();

        // Test get()
        System.out.println(">>> Test get():");
        System.out.println("    Elemento en índice 0: " + lista.get(0));
        System.out.println("    Elemento en índice 1: " + lista.get(1));
        System.out.println("    Elemento en índice 2: " + lista.get(2));
        System.out.println("    Elemento en índice 3: " + lista.get(3));
        System.out.println();

        // Test set()
        System.out.println(">>> Test set() - Cambiar índice 1 por 'Tesoro':");
        String anterior = lista.set(1, "Tesoro");
        System.out.println("    Valor anterior en índice 1: " + anterior);
        System.out.println("    Nuevo valor en índice 1: " + lista.get(1));
        System.out.print("    Lista completa: ");
        imprimirLista(lista);
        System.out.println();

        // Test remove()
        System.out.println(">>> Test remove() - Eliminar índice 2:");
        String eliminado = lista.remove(2);
        System.out.println("    Elemento eliminado: " + eliminado);
        System.out.println("    Tamaño después de eliminar: " + lista.size());
        System.out.print("    Lista completa: ");
        imprimirLista(lista);
        System.out.println();

        // Test remove() del primer elemento
        System.out.println(">>> Test remove() - Eliminar índice 0 (cabeza):");
        eliminado = lista.remove(0);
        System.out.println("    Elemento eliminado: " + eliminado);
        System.out.println("    Tamaño después de eliminar: " + lista.size());
        System.out.print("    Lista completa: ");
        imprimirLista(lista);
        System.out.println();

        // Test de error: get fuera de rango
        System.out.println(">>> Test get() fuera de rango (debe lanzar excepción):");
        try {
            lista.get(99);
            System.out.println("    ERROR: No lanzó excepción cuando debía");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("    Excepción capturada correctamente: " + e.getMessage());
        }
        System.out.println();

        // Test con números
        System.out.println(">>> Test con Integer:");
        Lista<Integer> listaNumeros = new Lista<>();
        for (int i = 10; i <= 50; i += 10) {
            listaNumeros.add(i);
        }
        System.out.print("    Lista de números: ");
        imprimirListaNumeros(listaNumeros);
        System.out.println("    Suma manual de los 5 elementos:");
        int suma = 0;
        for (int i = 0; i < listaNumeros.size(); i++) {
            suma += listaNumeros.get(i);
        }
        System.out.println("    10 + 20 + 30 + 40 + 50 = " + suma);
        System.out.println();

        System.out.println("=========================================");
        System.out.println("  TEST DE Array");
        System.out.println("=========================================\n");

        // Test constructor
        Array<String> Array = new Array<>(3, 4);
        System.out.println(">>> Array 3x4 creada:");
        System.out.println("    Filas: " + Array.getFilas());
        System.out.println("    Columnas: " + Array.getColumnas());
        System.out.println();

        // Test set() y get()
        System.out.println(">>> Test set() y get():");
        Array.set(0, 0, "Puerta");
        Array.set(0, 1, "Trampa");
        Array.set(1, 2, "Enemigo");
        Array.set(2, 3, "Jugador");
        System.out.println("    Posición (0,0): " + Array.get(0, 0));
        System.out.println("    Posición (0,1): " + Array.get(0, 1));
        System.out.println("    Posición (1,2): " + Array.get(1, 2));
        System.out.println("    Posición (2,3): " + Array.get(2, 3));
        System.out.println("    Posición (1,1) - vacía: " + Array.get(1, 1));
        System.out.println();

        System.out.println(">>> Test de validación de posiciones:");
        System.out.println("    (0,0) válida: " + probarGet(Array, 0, 0));
        System.out.println("    (2,3) válida: " + probarGet(Array, 2, 3));
        System.out.println("    (-1,0) válida: " + probarGet(Array, -1, 0));
        System.out.println("    (3,4) válida: " + probarGet(Array, 3, 4));
        System.out.println("    (3,0) válida: " + probarGet(Array, 3, 0));
        System.out.println("    (0,4) válida: " + probarGet(Array, 0, 4));
        System.out.println();

        // Test de error: get fuera de rango
        System.out.println(">>> Test get() fuera de rango (debe lanzar excepción):");
        try {
            Array.get(5, 5);
            System.out.println("    ERROR: No lanzó excepción cuando debía");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("    Excepción capturada correctamente: " + e.getMessage());
        }
        System.out.println();

        // Test de error: set fuera de rango
        System.out.println(">>> Test set() fuera de rango (debe lanzar excepción):");
        try {
            Array.set(-1, 0, "Error");
            System.out.println("    ERROR: No lanzó excepción cuando debía");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("    Excepción capturada correctamente: " + e.getMessage());
        }
        System.out.println();

        // Test rellenar y recorrer toda la Array
        System.out.println(">>> Rellenar Array 3x4 con números de posición:");
        Array<String> ArrayPos = new Array<>(3, 4);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                ArrayPos.set(i, j, "(" + i + "," + j + ")");
            }
        }
        System.out.println("    Array completa:");
        for (int i = 0; i < ArrayPos.getFilas(); i++) {
            System.out.print("    Fila " + i + ": ");
            for (int j = 0; j < ArrayPos.getColumnas(); j++) {
                System.out.print(ArrayPos.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();

        // Test con una Array tipo tablero de habitación
        System.out.println(">>> Simulación de tablero de habitación (3x3):");
        Array<String> tablero = new Array<>(3, 3);

        // Colocar elementos como en el juego
        tablero.set(0, 0, "[JUGADOR]");
        tablero.set(0, 1, "[OBJETO ]");
        tablero.set(0, 2, "[VACÍO  ]");
        tablero.set(1, 0, "[VACÍO  ]");
        tablero.set(1, 1, "[ENEMIGO]");
        tablero.set(1, 2, "[TRAMPA ]");
        tablero.set(2, 0, "[VACÍO  ]");
        tablero.set(2, 1, "[PUERTA ]");
        tablero.set(2, 2, "[VACÍO  ]");

        System.out.println("    +--------+--------+--------+");
        for (int i = 0; i < 3; i++) {
            System.out.print("    ");
            for (int j = 0; j < 3; j++) {
                System.out.print("|" + tablero.get(i, j));
            }
            System.out.println("|");
            System.out.println("    +--------+--------+--------+");
        }
        System.out.println();

        // Verificar posiciones específicas del tablero
        System.out.println(">>> Verificar posiciones del tablero:");
        System.out.println("    Esquina superior izquierda: " + tablero.get(0, 0));
        System.out.println("    Centro: " + tablero.get(1, 1));
        System.out.println("    Esquina inferior derecha: " + tablero.get(2, 2));
        System.out.println("    Posición de la puerta: " + tablero.get(2, 1));
        System.out.println();

        // Sobrescribir una posición
        System.out.println(">>> Mover jugador de (0,0) a (2,0):");
        tablero.set(0, 0, "[VACÍO  ]");  // Donde estaba el jugador, ahora vacío
        tablero.set(2, 0, "[JUGADOR]");  // Nueva posición del jugador
        System.out.println("    Tablero después del movimiento:");
        System.out.println("    +--------+--------+--------+");
        for (int i = 0; i < 3; i++) {
            System.out.print("    ");
            for (int j = 0; j < 3; j++) {
                System.out.print("|" + tablero.get(i, j));
            }
            System.out.println("|");
            System.out.println("    +--------+--------+--------+");
        }
        System.out.println();

        System.out.println("=========================================");
        System.out.println("  TODOS LOS TESTS COMPLETADOS");
        System.out.println("=========================================");
    }

    // Método auxiliar para imprimir listas de String
    private static void imprimirLista(Lista<String> lista) {
        System.out.print("[");
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            if (i < lista.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    // Método auxiliar para imprimir listas de Integer
    private static void imprimirListaNumeros(Lista<Integer> lista) {
        System.out.print("[");
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            if (i < lista.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    // Metodo auxiliar para utilizar indirectamente el metodo privado esPosicionValida
    private static boolean probarGet(Array<?> Array, int fila, int columna) {
        try {
            Array.get(fila, columna);
            return true;  // No lanzó excepción, posición válida
        } catch (IndexOutOfBoundsException e) {
            return false; // Lanzó excepción, posición inválida
        }
    }
}