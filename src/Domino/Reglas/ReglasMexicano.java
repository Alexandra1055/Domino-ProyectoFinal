package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.JugadorMexicano;
import Domino.Juego.MazoDomino;
import java.util.ArrayList;
import java.util.List;

public class ReglasMexicano extends ReglasConStock {
    private ArrayList<FichaDomino> trenComun;

    public ReglasMexicano(){
        super();
        trenComun = new ArrayList<FichaDomino>();
    }

    @Override
    public void iniciarMano(List<Jugador> jugadores) {
        ArrayList<JugadorMexicano> jugadoresMexicanos = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i++) {
            jugadoresMexicanos.add((JugadorMexicano) jugadores.get(i));
        }
        MazoDomino mazo = new MazoDomino();
        mazo.crearFichas(9);
        int fichasPorJugador = 7;
        for (int i = 0; i < jugadores.size(); i++) {
            JugadorMexicano jugador = jugadoresMexicanos.get(i);
            mazo.repartirFichas(jugador, fichasPorJugador);
            jugador.setTrenPersonal(new ArrayList<FichaDomino>());
        }
        stock = mazo.getStock();
        trenComun = new ArrayList<FichaDomino>();
        FichaDomino dobleInicial = null;
        int indiceInicial = -1;
        for (int i = 0; i < jugadores.size(); i++) {
            JugadorMexicano jugador = jugadoresMexicanos.get(i);
            for (int j = 0; j < jugador.getFichas().size(); j++) {
                FichaDomino ficha = jugador.getFichas().get(j);
                if (ficha.getLado1() == ficha.getLado2()){
                    if (dobleInicial == null || ficha.getLado1() > dobleInicial.getLado1()){
                        dobleInicial = ficha;
                        indiceInicial = i;
                    }
                }
            }
        }
        if (dobleInicial != null && indiceInicial != -1){
            JugadorMexicano jugadorInicial = jugadoresMexicanos.get(indiceInicial);
            for (int i = 0; i < jugadorInicial.getFichas().size(); i++) {
                FichaDomino ficha = jugadorInicial.getFichas().get(i);
                if (ficha.equals(dobleInicial)){
                    jugadorInicial.getFichas().remove(i);
                    jugadorInicial.agregarAlTrenPersonal(dobleInicial);
                    break;
                }
            }
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
        FichaDomino dobleInicial = null;
        int indiceInicial = 0;
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            for (int j = 0; j < jugador.getFichas().size(); j++) {
                FichaDomino ficha = jugador.getFichas().get(j);
                if (ficha.getLado1() == ficha.getLado2()){
                    if (dobleInicial == null || ficha.getLado1() > dobleInicial.getLado1()){
                        dobleInicial = ficha;
                        indiceInicial = i;
                    }
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

    public ArrayList<FichaDomino> getTrenComun() {
        return trenComun;
    }
}
