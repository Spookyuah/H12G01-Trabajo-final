package Juego.Motor;

import Juego.Modelo.Habitacion;
import Structures.ColaPila.*;
import Structures.Lista;
import Juego.Modelo.Posicion;

public class Movimiento {   //La clase movimiento no va a crear ninguna instancia, todos sus metodos seran statics
    private static class PosCost{   //Clase privada para el algoritmo de busqueda, permite crear una cola con dos tipos de datos diferentes
        Posicion pos;
        int cost;
        PosCost(Posicion pos, int cost){
            this.pos = pos;
            this.cost = cost;
        }
    }
    public static Lista<Posicion> rangoMovimiento(Habitacion h, Posicion origen, int vel) {
        if (!h.esPosicionValida(origen)) throw new IllegalArgumentException("La posicion " + origen + " no existe en la habitacion" + h.getId());
        if (!h.getCelda(origen).traversable()) throw new IllegalArgumentException("La celda " + origen + " no es traversable");

        Lista<Posicion> alcanzables = new Lista<>();
        int filas = h.getFilas();
        int columnas = h.getColumnas();
        boolean[][] visitado = new boolean[filas][columnas]; //True si la casilla ya se ha visitado

        Cola<PosCost> cola = new Cola<>(); //Inicializar cola, añadir origen como coste 0
        cola.add(new PosCost(origen, 0));
        visitado[origen.getX()][origen.getY()] = true;
        alcanzables.add(origen);

        int[] dx = {-1, 1, 0, 0}; //Cada indice del int representa una direccion, i=0 es [-1,0], abajo; i=1 es [1,0], arriba...
        int[] dy = {0, 0, -1, 1};

        while (!cola.isEmpty()) { //Algoritmo BFS
            PosCost actual = cola.remove();
            if (actual.cost >= vel) continue; //Finaliza si las casillas van mas alla del movimiento disponible

            for (int i = 0; i < 4; i++) {
                int newX = actual.pos.getX() + dx[i];
                int newY = actual.pos.getY() + dy[i];

                if (!h.esPosicionValida(newX, newY)) continue;
                if (visitado[newX][newY]) continue;
                if (!h.getCelda(newX, newY).traversable()) continue;
                if (h.getCelda(newX, newY).tieneEnemigo()) continue; //Solo procede si la celda que esta comprobando es valida, traversable, y no ha sido visitada ya

                visitado[newX][newY] = true;
                Posicion newPos= new Posicion(newX, newY);
                int newCost = actual.cost +1;
                cola.add(new PosCost(newPos, newCost)); //Añade la nueva posicion, y aumenta en 1 el coste de llegar a dicha celda
                alcanzables.add(newPos);
            }
        }
        return alcanzables;
    }
    public static Posicion buscarCercano(Lista<Posicion> alcanzables, Posicion objetivo) {  //Funcion para la IA enemiga
        if (alcanzables.isEmpty()) return null;

        Posicion mejor= alcanzables.get(0);
        int mejorDistancia= mejor.distanciaDe(objetivo);

        for (int i = 1; i < alcanzables.size(); i++) {
            Posicion actual= alcanzables.get(i);
            if (actual.equals(objetivo)) continue;

            int distancia = actual.distanciaDe(objetivo);
            if (distancia<mejorDistancia) {
                mejorDistancia= distancia;
                mejor= actual;
            }
        }
        return mejor;
    }
}
