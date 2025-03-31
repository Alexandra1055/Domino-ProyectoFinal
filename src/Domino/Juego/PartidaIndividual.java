package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;

public class PartidaIndividual extends JuegoDomino{

    public PartidaIndividual(Pais pais, Modalidad modalidad) {
        super(pais, modalidad);
    }

    @Override
    public void iniciarPartida() {
        System.out.println("Partida Individual de Dominó: " + pais.getTitulo());
    }
}
