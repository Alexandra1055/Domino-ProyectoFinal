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
    }
}
