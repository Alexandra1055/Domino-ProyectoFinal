package Domino.GestorJuego;

import Domino.DAO.UsuarioDAO;
import Domino.ENUMS.Pais;
import Domino.IO.Output;
import Domino.Juego.*;
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
        if (partida instanceof PartidaParejas) {
            PartidaParejas pp = (PartidaParejas) partida;
            Equipo equipo1 = new Equipo("Equipo 1");
            Equipo equipo2 = new Equipo("Equipo 2");

            Jugador jUsuario = new Jugador(nombreUsuario);
            Jugador j3 = new Jugador("Jugador 3");
            equipo1.agregarJugador(jUsuario);
            equipo1.agregarJugador(j3);

            Jugador j2 = new Jugador("Jugador 2");
            Jugador j4 = new Jugador("Jugador 4");
            equipo2.agregarJugador(j2);
            equipo2.agregarJugador(j4);

            pp.agregarEquipo(equipo1);
            pp.agregarEquipo(equipo2);

            int maxJug = Math.max(
                    equipo1.getJugadores().size(),
                    equipo2.getJugadores().size()
            );
            for (int idx = 0; idx < maxJug; idx++) {
                if (idx < equipo1.getJugadores().size()) {
                    pp.agregarJugador(equipo1.getJugadores().get(idx));
                }
                if (idx < equipo2.getJugadores().size()) {
                    pp.agregarJugador(equipo2.getJugadores().get(idx));
                }
            }
            // Creamos dos equipos con dos jugadores cada uno
        } else {
            partida.agregarJugador(new Jugador(nombreUsuario));
            for (int i = 2; i <= 4; i++) {
                partida.agregarJugador(new Jugador("Jugador " + i));
            }// Partida individual
        }

    }

    public static void procesarResultadoDePartida(JuegoDomino partida, Pais paisSeleccionado, int mejorPuntuacionActual, Usuario usuario, UsuarioDAO usuarioDAO){
        ReglasDomino reglas = partida.getReglas();
        Mesa mesa = partida.getMesa();

        boolean hayBloqueo = reglas.aplicaBloqueo(mesa, paisSeleccionado);
        Jugador ganador = null;
        int puntosObtenidos;

        if (hayBloqueo){
            ganador = reglas.determinarGanadorBloqueo(partida.getJugadores(), mesa, paisSeleccionado, partida.getTurnoActual());
            puntosObtenidos = reglas.puntosBloqueo();

            if (ganador != null){
                Output.mostrarConSalto("Hubo bloqueo: \n - Ganador " + ganador.getNombre() + " \n - Puntos: " + puntosObtenidos);
            }else {
                Output.mostrarConSalto("Hubo bloqueo, pero ningún jugador recibe puntos");
                return;
            }
        }else {
            for (int i = 0; i < partida.getJugadores().size(); i++) {
                Jugador juegador = partida.getJugadores().get(i);

                if (juegador.getFichas().isEmpty()){
                    ganador = juegador;
                    break;
                }
            }// para cuando la mano esta vacia

            if (ganador == null){
                int sumaMinima= Integer.MAX_VALUE;

                for (int i = 0; i < partida.getJugadores().size(); i++) {
                    Jugador jugador = partida.getJugadores().get(i);
                    int suma = 0;

                    for (int j = 0; j < jugador.getFichas().size(); j++) {
                        suma += jugador.getFichas().get(j).getLado1() + jugador.getFichas().get(j).getLado2();
                    }

                    if (suma < sumaMinima){
                        sumaMinima = suma;
                        ganador = jugador;
                    }
                }
            }// si no hubiera nadie con la mano vacia, se suman los puntos de las fichas que quedan

            puntosObtenidos = reglas.calcularPuntuacion(partida.getJugadores());
            Output.mostrarConSalto("Ha ganado: " + ganador.getNombre() + " con " + puntosObtenidos + " puntos");
        }

        if (ganador.getNombre().equals(usuario.getNombre()) && puntosObtenidos > mejorPuntuacionActual){
            usuario.actualizarPuntuacion(paisSeleccionado, puntosObtenidos);
            usuarioDAO.guardarUsuario(usuario);

            Output.mostrarConSalto("Nuevo récord en " + paisSeleccionado.getTitulo());
        }
    }
}
