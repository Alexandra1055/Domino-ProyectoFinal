package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasDomino;
import Domino.Reglas.ReglasEspanol;

import java.util.List;
import java.util.Scanner;

public class MainPruebaJuego {
    public static void main(String[] args) {

        Scanner imprimir = new Scanner(System.in);
        Pais pais = Pais.ESPANOL;
        Modalidad modalidad= Modalidad.INDIVIDUAL;
        ReglasDomino reglas = new ReglasEspanol();
        PartidaIndividual partida = new PartidaIndividual(pais,modalidad,reglas);

        Jugador jugador1 = new Jugador("Alexandra");
        Jugador jugador2 = new Jugador("Jaume");
        Jugador jugador3 = new Jugador("Cristian");
        Jugador jugador4 = new Jugador("Sergio");

        partida.agregarJugador(jugador1);
        partida.agregarJugador(jugador2);
        partida.agregarJugador(jugador3);
        partida.agregarJugador(jugador4);

        partida.iniciarPartida();
        partida.mostrarEstado();

        Mesa mesa = new Mesa();
        TurnoJuego turnoJuego = new TurnoJuego(mesa);

        List<Jugador> jugadores = partida.getJugadores();

        int turnoActual = 0;
        boolean partidaTerminada = false;

        while (reglas.sePuedeJugar(jugadores) && !partidaTerminada) {
            Jugador jugadorActual = jugadores.get(turnoActual);
            System.out.println("\n=========================================");
            System.out.println("Estado actual de la mesa:");
            mesa.imprimirMesa();

            if (reglas instanceof ReglasConStock) {
                turnoJuego.turnoJugador(jugadorActual, (ReglasConStock) reglas);
            } else {
                turnoJuego.turnoJugador(jugadorActual, null);
            }
            if (jugadorActual.getFichas().isEmpty()) {
                System.out.println("¡El jugador " + jugadorActual.getNombre() + " ha ganado la partida al quedarse sin fichas!");
                partidaTerminada = true;
                break;
            }


            turnoActual = (turnoActual + 1) % jugadores.size();
        }

    }
}
