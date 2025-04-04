package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;

import java.util.ArrayList;
import java.util.List;

public class MainPruebas {
    public static void main(String[] args) {
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador("Ale"));
        jugadores.add(new Jugador("Jaume"));
        jugadores.add(new Jugador("Cristian"));
        jugadores.add(new Jugador("Sergio"));

        ReglasLatino reglas = new ReglasLatino();
        reglas.iniciarMano(jugadores);

        System.out.println("Manos");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            System.out.println("Jugador " + jugador.getNombre() + ":");
            jugador.imprimirFichas();
        }
        System.out.println("Stock");
        ArrayList<FichaDomino> stock = reglas.getStock();
        for (int i = 0; i < stock.size(); i++) {
            System.out.print(stock.get(i).toString() + " ");
        }
        System.out.println("\nTotal stock: " + stock.size() + " fichas");

        int puntuacion = reglas.calcularPuntuacion(jugadores);
        System.out.println("Puntuación total de la ronda: " + puntuacion);

        Jugador iniciador = reglas.determinarJugadorInicial(jugadores);
        System.out.println("El jugador que inicia la partida es: " + iniciador.getNombre());
        boolean puedeJugar = reglas.sePuedeJugar(jugadores);
        System.out.println("¿Se puede seguir jugando? " + (puedeJugar ? "Sí" : "No"));
    }
}
