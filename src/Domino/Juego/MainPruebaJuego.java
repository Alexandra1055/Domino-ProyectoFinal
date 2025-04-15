package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasDomino;
import Domino.Reglas.ReglasEspanol;
import java.util.Scanner;

public class MainPruebaJuego {
    public static void main(String[] args) {

        Scanner imprimir = new Scanner(System.in);
        Pais pais = Pais.ESPANOL;
        Modalidad modalidad = Modalidad.PAREJAS;
        ReglasDomino reglas = new ReglasEspanol();
        Mesa mesa = new Mesa();
        PartidaParejas partida = new PartidaParejas(pais, modalidad, reglas, mesa);

        Jugador jugador1 = new Jugador("Alexandra");
        Jugador jugador2 = new Jugador("Jaume");
        Jugador jugador3 = new Jugador("Cristian");
        Jugador jugador4 = new Jugador("Sergio");

        Equipo equipo1 = new Equipo("Equipo1");
        Equipo equipo2 = new Equipo("Equipo2");
        equipo1.agregarJugador(jugador1);
        equipo1.agregarJugador(jugador2);
        equipo2.agregarJugador(jugador3);
        equipo2.agregarJugador(jugador4);

        partida.agregarEquipo(equipo1);
        partida.agregarEquipo(equipo2);

        partida.iniciarPartida();

        System.out.println("\n=========================================");
        System.out.println("Estado completo de los jugadores:");
        partida.mostrarEstado();

        boolean partidaTerminada = false;
        boolean primeraVez = true;
        while (reglas.sePuedeJugar(partida.getJugadores()) && !partidaTerminada) {
            System.out.println("\n=========================================");
            System.out.println("Estado actual de la mesa:");
            mesa.imprimirMesa();

            if (primeraVez) {
                primeraVez = false;
            } else {
                Jugador jugadorActual = partida.getJugadores().get(partida.turnoActual);
                System.out.println("Turno del jugador " + jugadorActual.getNombre() + ":");
                jugadorActual.imprimirFichas();
            }

            partida.jugarTurno();

            for (int i = 0; i < partida.getJugadores().size(); i++) {
                Jugador jugadorActual = partida.getJugadores().get(i);
                if (jugadorActual.getFichas().isEmpty()) {
                    System.out.println("¡El jugador " + jugadorActual.getNombre() +
                            " ha ganado la partida al quedarse sin fichas!");
                    partidaTerminada = true;
                    break;
                }
            }
            if (!partidaTerminada) {
                partida.proximoTurno();
            }
        }

        System.out.println("\n=========================================");
        System.out.println("Estado final de la mesa:");
        mesa.imprimirMesa();
        partida.mostrarEstado();
        System.out.println("Puntuación final: " + reglas.calcularPuntuacion(partida.getJugadores()));
        System.out.println("El jugador que inició la partida fue: " +
                reglas.determinarJugadorInicial(partida.getJugadores()).getNombre());

        imprimir.close();
    }
}
