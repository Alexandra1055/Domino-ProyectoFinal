package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.MazoDomino;

import java.util.ArrayList;
import java.util.List;
//como el Mexicano tiene algo llamado trenes personales o comunes, como esto es una peculiaridad del domino mexicano que se debe mantener
public class ReglasMexicano implements ReglasDomino{
    private ArrayList<FichaDomino> trenComun; //tren personal lo defini en jugador

    public ReglasMexicano(){
        trenComun = new ArrayList<FichaDomino>();
    }

    @Override
    public void iniciarMano(List<Jugador> jugadores) {
        MazoDomino mazo = new MazoDomino();
        mazo.crearFichas(9);
        int fichasPorJugador = 7;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            mazo.repartirFichas(jugador,fichasPorJugador);
            jugador.setTrenPersonal(new ArrayList<FichaDomino>());
        }
        trenComun = new ArrayList<FichaDomino>();

        FichaDomino dobleInicial = null;
        int indiceInicial = -1;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);

            for (int j = 0; j < jugador.getFichas().size(); j++) {
                FichaDomino ficha = jugador.getFichas().get(j);

                if (ficha.getLado1() == ficha.getLado2()){
                    if (dobleInicial == null || ficha.getLado1() > dobleInicial.getLado1()){
                        dobleInicial = ficha;
                        indiceInicial = i;
                    }//aqui miro quien tiene el doble mayor entre todos los jugadores
                }
            }
        }
        if (dobleInicial != null && indiceInicial != -1){
            Jugador jugadorInicial = jugadores.get(indiceInicial);

            for (int i = 0; i < jugadorInicial.getFichas().size(); i++) {
                FichaDomino ficha = jugadorInicial.getFichas().get(i);

                if (ficha.equals(dobleInicial)){
                    jugadorInicial.getFichas().remove(i);
                    jugadorInicial.agregarAlTrenPersonal(dobleInicial);
                    break; //asi cuando encontremos un doble pasa de la mano dle jugador a su tren personal
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
                if (ficha.getLado1() == ficha.getLado2()) {
                    if (dobleInicial == null || ficha.getLado1() > dobleInicial.getLado1()) {
                        dobleInicial = ficha;
                        indiceInicial = i;
                    }
                }
            }
        }
        return jugadores.get(indiceInicial); //mismo metodo que arriba
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
