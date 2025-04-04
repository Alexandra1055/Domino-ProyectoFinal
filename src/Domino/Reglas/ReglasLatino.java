package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.MazoDomino;

import java.util.List;

public class ReglasLatino implements ReglasDomino{
    @Override
    public void iniciarMano(List<Jugador> jugadores) {
        MazoDomino mazo = new MazoDomino();
        mazo.crearFichas(6);
        int fichasPorJugador = 7;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            mazo.repartirFichas(jugador,fichasPorJugador);
        }
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

        int dobleMayor = -1;
        int indiceInicial = 0;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            for (int j = 0; j < jugador.getFichas().size(); j++) {
                FichaDomino ficha = jugador.getFichas().get(j);
                if (ficha.getLado1() == ficha.getLado2() && ficha.getLado1() > dobleMayor){
                    dobleMayor = ficha.getLado1();
                    indiceInicial = i;
                }
            }
        }

        return jugadores.get(indiceInicial);
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
