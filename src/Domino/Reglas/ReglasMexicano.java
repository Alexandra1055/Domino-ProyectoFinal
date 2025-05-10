package Domino.Reglas;

import Domino.ENUMS.Pais;
import Domino.Juego.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReglasMexicano extends ReglasConStock implements Serializable {

    @Override
    public void iniciarMano(List<Jugador> jugadores, Mesa mesa) {
        mazo.crearFichas(9);
        int fichasPorJugador = 7;
        for (int i = 0; i < jugadores.size(); i++) {
            mazo.repartirFichas(jugadores.get(i), fichasPorJugador);
        }
        stock.clear();
        stock.addAll(mazo.getFichasRestantes());

        Jugador turno = determinarJugadorInicial(jugadores);
        FichaDomino dobleInicial = null;
            for (int i = 0; i < turno.getFichas().size(); i++) {
            FichaDomino f = turno.getFichas().get(i);
            if (f.getLado1() == f.getLado2()
                && (dobleInicial == null || f.getLado1() > dobleInicial.getLado1())) {
                dobleInicial = f;
            }
        }
        if (dobleInicial != null) {
        turno.eliminarFicha(dobleInicial);
        mesa.agregarFichaDerecha(dobleInicial);
        }
    }


    @Override
    public int calcularPuntuacion(List<Jugador> jugadores) {
        int puntos = 0;
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
                    puntos += f.getLado1() + f.getLado2();
                }
            }
        }
        return puntos;
    }

    @Override
    public Jugador determinarJugadorInicial(List<Jugador> jugadores) {
        FichaDomino mejor = null;
        Jugador elegido = jugadores.get(0);
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            for (int k = 0; k < j.getFichas().size(); k++) {
                FichaDomino f = j.getFichas().get(k);
                if (f.getLado1() == f.getLado2()) {
                    if (mejor == null || f.getLado1() > mejor.getLado1()) {
                        mejor = f;
                        elegido = j;
                    }
                }
            }
        }
        return elegido;
    }
    @Override
    public Jugador determinarGanadorBloqueo(List<Jugador> jugadores, Mesa mesa, Pais pais, int turnoActual){
        int total = jugadores.size();
        int indiceUltimo = (turnoActual - 1 + total) % total;
        return jugadores.get(indiceUltimo);
    }

}
