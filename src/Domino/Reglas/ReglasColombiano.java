package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.MazoDomino;

import java.io.Serializable;
import java.util.List;

public class ReglasColombiano extends ReglasSinStock implements Serializable {
    @Override
    public void iniciarMano(List<Jugador> jugadores) {
        mazo.crearFichas(6);
        int fichasPorJugador = 7;
        for (int i = 0; i < jugadores.size(); i++) {
            mazo.repartirFichas(jugadores.get(i), fichasPorJugador);
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
        int mejor = -1;
        Jugador elegido = jugadores.get(0);
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            for (int k = 0; k < j.getFichas().size(); k++) {
                FichaDomino f = j.getFichas().get(k);
                if (f.getLado1() == f.getLado2() && f.getLado1() > mejor) {
                    mejor = f.getLado1();
                    elegido = j;
                }
            }
        }
        return elegido;
    }
}


