package Domino.Reglas;

import Domino.Juego.Jugador;
import Domino.Juego.MazoDomino;

import java.util.List;

public abstract class ReglasSinStock implements ReglasDomino{
    protected MazoDomino mazo;

    public ReglasSinStock(){
        this.mazo = new MazoDomino();
    }
    @Override
    public boolean sePuedeJugar(List<Jugador> jugadores) {
        for (Jugador jugador : jugadores) {
            if (jugador.getFichas().isEmpty()) {
                return false;
            }
        }
        return true;
    }// El juego continúa hasta que un jugador se quede sin fichas
}
