package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasDomino;

import java.util.ArrayList;

public abstract class JuegoDomino {
    protected Pais pais;
    protected Modalidad modalidad;
    protected int puntuacionGanadora;
    protected ArrayList<Jugador> jugadores;
    protected int turnoActual;
    protected ReglasDomino reglas;

    public JuegoDomino(Pais pais, Modalidad modalidad, ReglasDomino reglas){
        this.pais = pais;
        this.modalidad = modalidad;
        this.puntuacionGanadora = pais.getPuntuacionGanadora();
        this.jugadores = new ArrayList<Jugador>();
        this.turnoActual = 0;
        this.reglas = reglas;
    }

    public void agregarJugador(Jugador jugador){
        jugadores.add(jugador);
    }

    public abstract void iniciarPartida();

    public void mostrarEstado(){

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);

            System.out.println("Jugador " + (i + 1) + " : " + jugador.getNombre()); //El +1 es solo para que empiece en 1 y no en 0
            jugador.imprimirFichas();
        }
    }

    public void proximoTurno(){

        turnoActual = (turnoActual + 1) % jugadores.size(); //va cambiando de jugadores en un bucle

        System.out.println("Turno de : " + jugadores.get(turnoActual).getNombre());
    }

    public ReglasDomino getReglas() {
        return reglas;
    }

    public void setReglas(ReglasDomino reglas) {
        this.reglas = reglas;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
}
