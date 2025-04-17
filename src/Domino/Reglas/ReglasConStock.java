package Domino.Reglas;

import Domino.Juego.MazoDomino;
import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import java.util.ArrayList;
import java.util.List;

public abstract class ReglasConStock implements ReglasDomino {
    protected MazoDomino mazo;
    protected ArrayList<FichaDomino> stock;

    public ReglasConStock() {
        this.mazo = new MazoDomino();
        this.stock = new ArrayList<>();
    }

    @Override
    public boolean sePuedeJugar(List<Jugador> jugadores) {
        for (Jugador j : jugadores) {
            if (j.getFichas().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean puedeRobarFicha(Jugador jugador) {
        if (stock.isEmpty()) {
            return false;
        }
        FichaDomino ficha = stock.remove(0);
        jugador.agregarFicha(ficha);
        return true;
    }

    public List<FichaDomino> getStock() {
        return stock;
    }
}
