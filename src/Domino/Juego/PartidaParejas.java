package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasDomino;
import java.util.ArrayList;

public class PartidaParejas extends JuegoDomino {

    private ArrayList<Equipo> equipos;

    public PartidaParejas(Pais pais, Modalidad modalidad, ReglasDomino reglas, Mesa mesa) {
        super(pais, modalidad, reglas, mesa);
        this.equipos = new ArrayList<Equipo>();
    }

    public void agregarEquipo(Equipo equipo) {
        equipos.add(equipo);
    }

    @Override
    public void iniciarPartida() {
        System.out.println("Partida por Parejas de Dominó: " + pais.getTitulo());

        int maxJugadores = 0;
        for (int i = 0; i < equipos.size(); i++) {
            int tam = equipos.get(i).getJugadores().size();
            if (tam > maxJugadores) {
                maxJugadores = tam;
            }
        }

        jugadores.clear();

        for (int i = 0; i < maxJugadores; i++) {
            for (int j = 0; j < equipos.size(); j++) {
                ArrayList<Jugador> jugadoresEquipo = equipos.get(j).getJugadores();
                if (i < jugadoresEquipo.size()) {
                    jugadores.add(jugadoresEquipo.get(i));
                }
            }
        }

        reglas.iniciarMano(jugadores);
    }

    @Override
    public void mostrarEstado() {
        for (int i = 0; i < equipos.size(); i++) {
            Equipo equipo = equipos.get(i);
            System.out.println("Equipo " + (i + 1) + " : " + equipo.getNombre());
            equipo.imprimirFichasEquipo();
        }
    }

    @Override
    public void proximoTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        System.out.println("Turno de : " + jugadores.get(turnoActual).getNombre());
    }
}
