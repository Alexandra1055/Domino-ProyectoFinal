package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasDomino;

public class PartidaIndividual extends JuegoDomino{

    public PartidaIndividual(Pais pais, Modalidad modalidad, ReglasDomino reglas) {
        super(pais, modalidad, reglas);
    }

    @Override
    public void iniciarPartida() {
        System.out.println("Partida Individual de Dominó: " + pais.getTitulo());
        reglas.iniciarMano(jugadores); //iniciamos la mano segun el pais
    }
}
