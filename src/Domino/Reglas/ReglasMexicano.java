package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.JugadorMexicano;
import Domino.Juego.MazoDomino;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReglasMexicano extends ReglasConStock implements Serializable {
    private ArrayList<FichaDomino> trenComun;

    public ReglasMexicano() {
        super();
        trenComun = new ArrayList<FichaDomino>();
    }

    @Override
    public void iniciarMano(List<Jugador> jugadores) {
        ArrayList<JugadorMexicano> jm = new ArrayList<JugadorMexicano>();
        for (int i = 0; i < jugadores.size(); i++) {
            jm.add((JugadorMexicano) jugadores.get(i));
        }
        mazo.crearFichas(9);
        int fichasPorJugador = 7;
        for (int i = 0; i < jm.size(); i++) {
            JugadorMexicano j = jm.get(i);
            mazo.repartirFichas(j, fichasPorJugador);
            j.setTrenPersonal(new ArrayList<FichaDomino>());
        }
        stock.clear();
        stock.addAll(mazo.getFichasRestantes());
        trenComun.clear();

        FichaDomino dobleInicial = null;
        int indiceInit = -1;
        for (int i = 0; i < jm.size(); i++) {
            JugadorMexicano j = jm.get(i);
            for (int k = 0; k < j.getFichas().size(); k++) {
                FichaDomino f = j.getFichas().get(k);
                if (f.getLado1() == f.getLado2()) {
                    if (dobleInicial == null || f.getLado1() > dobleInicial.getLado1()) {
                        dobleInicial = f;
                        indiceInit = i;
                    }
                }
            }
        }
        if (dobleInicial != null && indiceInit != -1) {
            JugadorMexicano j0 = jm.get(indiceInit);
            for (int k = 0; k < j0.getFichas().size(); k++) {
                FichaDomino f = j0.getFichas().get(k);
                if (f.equals(dobleInicial)) {
                    j0.getFichas().remove(k);
                    j0.agregarAlTrenPersonal(dobleInicial);
                    break;
                }
            }
        }
    }

    @Override
    public int calcularPuntuacion(List<Jugador> jugadores) {
        int puntuacion = 0;
        Jugador ganador = null;
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getFichas().isEmpty()) {
                ganador = jugadores.get(i);
                break;
            }
        }
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            if (j != ganador) {
                for (int k = 0; k < j.getFichas().size(); k++) {
                    FichaDomino f = j.getFichas().get(k);
                    puntuacion += f.getLado1() + f.getLado2();
                }
            }
        }
        return puntuacion;
    }

    @Override
    public Jugador determinarJugadorInicial(List<Jugador> jugadores) {
        FichaDomino mejor = null;
        int idx = 0;
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            for (int k = 0; k < j.getFichas().size(); k++) {
                FichaDomino f = j.getFichas().get(k);
                if (f.getLado1() == f.getLado2()) {
                    if (mejor == null || f.getLado1() > mejor.getLado1()) {
                        mejor = f;
                        idx = i;
                    }
                }
            }
        }
        return jugadores.get(idx);
    }

    public List<FichaDomino> getTrenComun() {
        return trenComun;
    }
}
