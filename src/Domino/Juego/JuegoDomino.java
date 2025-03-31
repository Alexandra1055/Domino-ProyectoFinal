package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;

import java.util.ArrayList;

public abstract class JuegoDomino {
    protected Pais pais;
    protected Modalidad modalidad;
    protected int puntuacionGanadora;
    protected ArrayList<Jugador> jugadores;
    protected int turnoActual;

    public JuegoDomino(Pais pais, Modalidad modalidad){
        this.pais = pais;
        this.modalidad = modalidad;
        this.puntuacionGanadora = pais.getPuntuacionGanadora();
        this.jugadores = new ArrayList<Jugador>();
        this.turnoActual = 0;
        //implementar las reglas de cada modalidad!!!
    }

    public void agregarJugador(Jugador jugador){
        jugadores.add(jugador);
    }

    public abstract void iniciarPartida(); //!!!!podria crear subclases segun si incia la partida individual o en equipo

    public void mostrarEstado(){

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);

            System.out.println("Jugador " + (i + 1) + " : " + jugador.getNombre()); //El +1 es solo para que empiece en 1 y no en 0
            jugador.imprimirFichas();
        }
    }

    public void proximoTurno(){

        turnoActual = (turnoActual + 1) % jugadores.size(); //va cambiando de jugadores en un bucle (como cuando haciamos el cifrado de cesar)

        System.out.println("Turno de : " + jugadores.get(turnoActual).getNombre());
    }

    //pruebo con una de equipos
    public static void main(String[] args) {
        PartidaParejas partida = new PartidaParejas(Pais.ESPANOL, Modalidad.PAREJAS);

        Equipo equipo1 = new Equipo("Equipo 1");
        Equipo equipo2 = new Equipo("Equipo 2");

        Jugador J1 = new Jugador("Alexandra");
        Jugador J2 = new Jugador("Jaume");
        Jugador J3 = new Jugador("Cristian");
        Jugador J4 = new Jugador("Sergio");

        equipo1.agregarJugador(J1);
        equipo1.agregarJugador(J2);

        equipo2.agregarJugador(J3);
        equipo2.agregarJugador(J4);

        partida.agregarEquipo(equipo1);
        partida.agregarEquipo(equipo2);

        partida.iniciarPartida();
        partida.mostrarEstado();

        System.out.println("Cambiar turno");
        partida.proximoTurno();
        partida.proximoTurno();
        partida.proximoTurno();
        partida.proximoTurno();
    }

}
