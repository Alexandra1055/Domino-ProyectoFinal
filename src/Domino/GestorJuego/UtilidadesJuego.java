package Domino.GestorJuego;

import Domino.Juego.FichaDomino;
import Domino.Juego.JuegoDomino;
import Domino.Juego.Jugador;
import Domino.Juego.Mesa;
import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasDomino;


public class UtilidadesJuego {

    public static void jugarTurnoAutomatico(JuegoDomino partida) {
        Jugador turno = partida.getJugadorActual();
        Mesa mesa = partida.getMesa();
        ReglasDomino reglas = partida.getReglas();

        if (!mesa.esJugadaValida(turno)) {
            if (reglas instanceof ReglasConStock) {
                ((ReglasConStock) reglas).puedeRobarFicha(turno);
            }
            return;
        }
        for (int i = 0; i < turno.getFichas().size(); i++) {
            FichaDomino ficha = turno.getFichas().get(i);
            if (mesa.puedeColocarseIzquierda(ficha)) {
                turno.eliminarFicha(ficha);
                mesa.agregarFichaIzquierda(ficha);
                return;
            }
            if (mesa.puedeColocarseDerecha(ficha)) {
                turno.eliminarFicha(ficha);
                mesa.agregarFichaDerecha(ficha);
                return;
            }
        }
    }

    public static void registrarJugadores(JuegoDomino partida, String nombreUsuario) {
        partida.agregarJugador(new Jugador(nombreUsuario));

        for (int i = 2; i <= 4; i++) {
            partida.agregarJugador(new Jugador("Jugador " + i));
        }
    }
}
