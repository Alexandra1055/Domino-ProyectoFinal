package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.MazoDomino;

import java.util.ArrayList;
import java.util.List;

public class ReglasLatino extends ReglasConStock{

    public ReglasLatino() {
        super();
    }

    @Override
    public void iniciarMano(List<Jugador> jugadores) {
        MazoDomino mazo = new MazoDomino();
        mazo.crearFichas(6);
        int fichasPorJugador = 5;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            mazo.repartirFichas(jugador,fichasPorJugador);
        }
        stock = mazo.getStock();
    }

    @Override
    public int calcularPuntuacion(List<Jugador> jugadores) {
        int puntuacionTotal = 0;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            for (int j = 0; j < jugador.getFichas().size(); j++) {
                FichaDomino ficha = jugador.getFichas().get(j);
                puntuacionTotal += ficha.getLado1() + ficha.getLado2();
            }
        }

        return puntuacionTotal;
    }

    @Override
    public Jugador determinarJugadorInicial(List<Jugador> jugadores) {

        for (int i = 6; i >=0; i--) {
            for (int j = 0; j < jugadores.size(); j++) {
                Jugador jugador = jugadores.get(j);
                ArrayList<FichaDomino> fichas = jugador.getFichas();
                for (int k = 0; k < fichas.size(); k++) {
                    FichaDomino ficha = fichas.get(k);
                    if (ficha.getLado1() == i && ficha.getLado2() == i) {
                        return jugador;
                    }
                }

            }
        }
        return jugadores.get(0);
    }

    @Override
    public boolean sePuedeJugar(List<Jugador> jugadores) {

        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).tieneFichas()){
                return true;
            }
        }

        return false;
    }

}
