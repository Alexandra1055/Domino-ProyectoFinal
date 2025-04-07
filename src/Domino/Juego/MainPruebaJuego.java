package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasDomino;
import Domino.Reglas.ReglasEspanol;

public class MainPruebaJuego {
    public static void main(String[] args) {
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

        if (!jugador1.getFichas().isEmpty()) {
            FichaDomino fichaAColocar = jugador1.getFichas().get(0);
            System.out.println("El jugador " + jugador1.getNombre() + " intenta colocar la ficha " + fichaAColocar.toString() + " a la derecha.");
            mesa.agregarFichaDerecha(fichaAColocar);
            jugador1.getFichas().remove(0);
        }

        mesa.imprimirMesa();

        int puntuacion = reglas.calcularPuntuacion(partida.getJugadores());
        System.out.println("Puntuación total de la ronda: " + puntuacion);
    }
}
