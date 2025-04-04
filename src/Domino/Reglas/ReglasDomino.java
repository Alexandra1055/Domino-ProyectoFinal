package Domino.Reglas;

import Domino.Juego.Jugador;

import java.util.List;

public interface ReglasDomino {

    void iniciarMano(List<Jugador> jugadores);

    int calcularPuntuacion(List<Jugador> jugadores);

    Jugador determinarJugadorInicial(List<Jugador> jugadores);

    boolean sePuedeJugar(List<Jugador> jugadores);
}
