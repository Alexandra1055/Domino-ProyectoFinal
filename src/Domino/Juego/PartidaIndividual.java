package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.IO.Output;
import Domino.Reglas.ReglasDomino;

import java.io.Serializable;

public class PartidaIndividual extends JuegoDomino implements Serializable {

    public PartidaIndividual(Pais pais, Modalidad modalidad, ReglasDomino reglas, Mesa mesa) {
        super(pais, modalidad, reglas, mesa);
    }

    @Override
    public void iniciarPartida() {
        Output.mostrarConSalto("Partida Individual Domino: " + pais.getTitulo());

        reglas.iniciarMano(jugadores);

        Jugador primero = reglas.determinarJugadorInicial(jugadores);

        turnoActual = jugadores.indexOf(primero);
        Output.mostrarConSalto("Empieza el jugador: " + primero.getNombre());

        while (reglas.sePuedeJugar(jugadores)){
            mesa.imprimirMesa();
            mostrarEstado();
            jugarTurno();
            if (reglas.sePuedeJugar(jugadores)){
                proximoTurno();
            }
        }

        int puntuacion = reglas.calcularPuntuacion(jugadores);

        Output.mostrarConSalto("¡Partida Finalizada! Tu puntuación es: " + puntuacion);
    }
}
