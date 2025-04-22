package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface ReglasDomino extends Serializable {

    void iniciarMano(List<Jugador> jugadores);

    int calcularPuntuacion(List<Jugador> jugadores);

    Jugador determinarJugadorInicial(List<Jugador> jugadores);

    boolean sePuedeJugar(List<Jugador> jugadores); //asi puedo hacer la logica de que ha terminado o no el juego

}
