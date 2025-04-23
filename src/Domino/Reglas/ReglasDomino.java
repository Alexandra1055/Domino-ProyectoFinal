package Domino.Reglas;

import Domino.ENUMS.Pais;
import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.Mesa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface ReglasDomino extends Serializable {

    void iniciarMano(List<Jugador> jugadores, Mesa mesa);

    int calcularPuntuacion(List<Jugador> jugadores);

    Jugador determinarJugadorInicial(List<Jugador> jugadores);

    boolean sePuedeJugar(List<Jugador> jugadores); //asi puedo hacer la logica de que ha terminado o no el juego

    default boolean aplicaBloqueo(Mesa mesa, Pais pais) {
        return mesa.estaBloqueado(pais);
    }

    default Jugador determinarGanadorBloqueo(List<Jugador> jugadores, Mesa mesa, Pais pais, int turnoActual){
        return null;
    }

    default int puntosBloqueo() {
        return 0;
    }
}
