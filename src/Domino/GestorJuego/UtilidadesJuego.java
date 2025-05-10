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

            int maxJug = Math.max(equipo1.getJugadores().size(), equipo2.getJugadores().size());
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

    public static void procesarResultadoDePartida(JuegoDomino partida, Pais paisSeleccionado, int mejorPuntuacionActual, Usuario usuario, UsuarioDAO usuarioDAO) {
        ReglasDomino reglas = partida.getReglas();
        Mesa mesa = partida.getMesa();

        boolean hayBloqueo = reglas.aplicaBloqueo(mesa, paisSeleccionado);
        String mensajeResultado = "";
        int puntuacionTotal = 0;

        if (partida instanceof PartidaParejas) {
            PartidaParejas partidaParejas = (PartidaParejas) partida;
            Equipo equipoGanador = null;
            Equipo equipoPerdedor = null;

            if (hayBloqueo) {
                Jugador ganadorBloqueo = reglas.determinarGanadorBloqueo(partida.getJugadores(), mesa, paisSeleccionado, partida.getTurnoActual());
                for (int i = 0; i < partidaParejas.getEquipos().size(); i++) {
                    Equipo equipo = partidaParejas.getEquipos().get(i);
                    for (int j = 0; j < equipo.getJugadores().size(); j++) {
                        if (equipo.getJugadores().get(j).equals(ganadorBloqueo)) {
                            equipoGanador = equipo;
                        }
                    }
                }
                puntuacionTotal = reglas.puntosBloqueo();
            } else {
                Jugador ganadorMano = null;
                for (int i = 0; i < partida.getJugadores().size(); i++) {
                    Jugador jugadorActual = partida.getJugadores().get(i);
                    if (jugadorActual.getFichas().isEmpty()) {
                        ganadorMano = jugadorActual;
                        break;
                    }
                }

                for (int i = 0; i < partidaParejas.getEquipos().size(); i++) {
                    Equipo equipo = partidaParejas.getEquipos().get(i);
                    if (equipo.getJugadores().contains(ganadorMano)) {
                        equipoGanador = equipo;
                    } else {
                        equipoPerdedor = equipo;
                    }
                }

                for (int i = 0; i < equipoPerdedor.getJugadores().size(); i++) {
                    Jugador j = equipoPerdedor.getJugadores().get(i);
                    for (int k = 0; k < j.getFichas().size(); k++) {
                        FichaDomino f = j.getFichas().get(k);
                        puntuacionTotal += f.getLado1() + f.getLado2();
                    }
                }

                if (reglas instanceof ReglasConStock) {
                    ReglasConStock reglasStock = (ReglasConStock) reglas;
                    for (int i = 0; i < reglasStock.getStock().size(); i++) {
                        FichaDomino ficha = reglasStock.getStock().get(i);
                        puntuacionTotal += ficha.getLado1() + ficha.getLado2();
                    }
                }
            }
            int puntosPorJugador = puntuacionTotal / 2;
            String nombres = equipoGanador.getJugadores().get(0).getNombre()
                    + " y " + equipoGanador.getJugadores().get(1).getNombre();
            mensajeResultado = "Ha ganado " + equipoGanador.getNombre()
                    + ", formado por " + nombres
                    + " con " + puntuacionTotal + " puntos (cada uno obtiene " + puntosPorJugador + " puntos)";

        } else {
            Jugador ganador = null;
            if (hayBloqueo) {
                ganador = reglas.determinarGanadorBloqueo(partida.getJugadores(), mesa, paisSeleccionado, partida.getTurnoActual());
                puntuacionTotal = reglas.puntosBloqueo();
            } else {
                for (int i = 0; i < partida.getJugadores().size(); i++) {
                    Jugador j = partida.getJugadores().get(i);
                    if (j.getFichas().isEmpty()) {
                        ganador = j;
                        break;
                    }
                }
                puntuacionTotal = reglas.calcularPuntuacion(partida.getJugadores());
            }
            mensajeResultado = "Ha ganado: " + ganador.getNombre() + " con " + puntuacionTotal + " puntos";
            if (ganador.getNombre().equals(usuario.getNombre()) && puntuacionTotal > mejorPuntuacionActual) {
                usuario.actualizarPuntuacion(paisSeleccionado, puntuacionTotal);
                usuarioDAO.guardarUsuario(usuario);
                Output.mostrarConSalto("Nuevo récord en " + paisSeleccionado.getTitulo());
            }
        }
        Output.mostrarConSalto(mensajeResultado);
    }
}
