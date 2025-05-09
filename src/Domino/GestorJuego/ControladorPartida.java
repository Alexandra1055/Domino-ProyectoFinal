package Domino.GestorJuego;

import Domino.DAO.PartidaDAO;
import Domino.DAO.UsuarioDAO;
import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Factory.ModalidadFactory;
import Domino.IO.Input;
import Domino.IO.Output;
import Domino.Juego.*;
import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasDomino;

import java.util.List;

public class ControladorPartida {
    private Usuario usuario;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PartidaDAO partidaDAO = new PartidaDAO();
    private boolean retornoPorGuardado = false;

    public ControladorPartida(Usuario usuario, UsuarioDAO usuarioDAO, PartidaDAO partidaDAO){
        this.usuario = usuario;
        this.usuarioDAO = usuarioDAO;
        this.partidaDAO = partidaDAO;
    }

    public void iniciarPartidaNueva(){
        Pais pais = seleccionarPais();
        Modalidad modalidad = seleccionarModalidad();

        Output.mostrarConSalto("Has seleccionado el país " + pais.getTitulo() + " con modalidad " + modalidad.getTitulo());
        Output.mostrarConSalto("Reglas: " + pais.getReglaEspecial());
        Output.mostrarConSalto("Este Dominó consta de " + pais.getTotalFichas() + " fichas");
        if (pais.tieneObjetivo()) {
            Output.mostrarConSalto("Puntuación Ganadora: " + pais.getPuntuacionGanadora() + " puntos");
        } else {
            Output.mostrarConSalto("Puntuación Ganadora: Sin límite");
        }

        int mejorPuntuacion = usuario.getPuntuacionMaxima(pais);

        int ipais = pais.ordinal() + 1;
        int imod = modalidad.ordinal() + 1;
        JuegoDomino partidaGuardada = partidaDAO.cargarPartida(usuario.getNombre(), ipais, imod);

        if (partidaGuardada != null) {
            String respuesta = Input.leerLinea("Hay una partida guardada de " + pais.getTitulo()  + " / " + modalidad.getTitulo() + ". ¿Continuar? (S/N): ");
            if (respuesta.equalsIgnoreCase("S")) {
                reanudarPartidaExistente(partidaGuardada, pais, modalidad);
                return;
            }
        }

        nuevaPartida(pais, modalidad, mejorPuntuacion);
    }

    public void reanudarPartidaExistente(JuegoDomino partida, Pais pais, Modalidad modalidad) {
        Mesa mesa = partida.getMesa();
        if (partida.getJugadores().isEmpty() && partida instanceof PartidaParejas) {
            UtilidadesJuego.registrarJugadores(partida, usuario.getNombre());
        }

        Output.mostrarConSalto("Estado actual de la mesa:");
        mesa.imprimirMesa();

        Output.mostrarConSalto("Fichas en tu mano:");
        List<Jugador> jugadores = partida.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            if (j.getNombre().equals(usuario.getNombre())) {
                Output.mostrarConSalto(j.getFichas().toString());
                break;
            }
        }

        String opcion = Input.leerLinea("¿Continuar partida o Salir al menú? (C/S): ");
        if (opcion.equalsIgnoreCase("S")) {
            partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal()+1, modalidad.ordinal()+1, partida);
            return;
        }

        ReglasDomino reglas = partida.getReglas();
        if (!reglas.sePuedeJugar(partida.getJugadores()) || mesa.estaBloqueado(pais)) {
            int mejorPuntuacion = usuario.getPuntuacionMaxima(pais);
            nuevaPartida(pais, modalidad, mejorPuntuacion);
            return;
        }

        int mejor = usuario.getPuntuacionMaxima(pais);
        bucleTurnos(partida, pais, modalidad, mejor);

        if (retornoPorGuardado) {
            retornoPorGuardado = false;
            return;
        }

        UtilidadesJuego.procesarResultadoDePartida(partida, pais, usuario.getPuntuacionMaxima(pais), usuario, usuarioDAO);
    }

    private void nuevaPartida(Pais pais, Modalidad modalidad, int mejorPuntuacion) {
        int objetivo = pais.getPuntuacionGanadora();
        boolean tieneObjetivo = pais.tieneObjetivo();
        boolean continuar = true;

        while (continuar) {
            JuegoDomino partida = ModalidadFactory.crearPartida(pais, modalidad);
            Mesa mesa = partida.getMesa();
            ReglasDomino reglas = partida.getReglas();

            UtilidadesJuego.registrarJugadores(partida, usuario.getNombre());
            reglas.iniciarMano(partida.getJugadores(), mesa);

            Jugador primero = reglas.determinarJugadorInicial(partida.getJugadores());
            partida.setTurnoActual(partida.getJugadores().indexOf(primero));
            Output.mostrarConSalto("Empieza: " + primero.getNombre());
            partida.proximoTurno();

            bucleTurnos(partida, pais, modalidad, mejorPuntuacion);

            if (retornoPorGuardado) {
                retornoPorGuardado = false;
                return;
            }

            UtilidadesJuego.procesarResultadoDePartida(partida, pais, mejorPuntuacion, usuario, usuarioDAO);
            mejorPuntuacion = usuario.getPuntuacionMaxima(pais);

            String pregunta;
            if (tieneObjetivo) {
                pregunta = "Objetivo: " + objetivo + ". ¿Otra? (S/N): ";
            } else {
                pregunta = "Récord: " + mejorPuntuacion + ". ¿Otra? (S/N): ";
            }
            continuar = Input.leerLinea(pregunta).equalsIgnoreCase("S");
        }
        Output.mostrarConSalto("Fin en " + pais.getTitulo() + ". Máxima: " + usuario.getPuntuacionMaxima(pais));
        partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal() + 1, modalidad.ordinal() + 1, ModalidadFactory.crearPartida(pais, modalidad));
    }

    private void bucleTurnos(JuegoDomino partida, Pais pais, Modalidad modalidad, int mejorPuntuacion) {
        Mesa mesa = partida.getMesa();
        ReglasDomino reglas = partida.getReglas();

        if (!reglas.sePuedeJugar(partida.getJugadores()) || mesa.estaBloqueado(pais)) {
            String respuesta = Input.leerLinea("La partida está finalizada o bloqueada. ¿Continuar (C) o Salir (S)? ");

            if (respuesta.equalsIgnoreCase("S")) {
                partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal() + 1, modalidad.ordinal() + 1, partida);
                return;
            }
        }

        while (reglas.sePuedeJugar(partida.getJugadores()) && !mesa.estaBloqueado(pais)) {
            mesa.imprimirMesa();

            Jugador turno = partida.getJugadorActual();

            if (turno.getNombre().equals(usuario.getNombre())) {
                String respuesta = Input.leerLinea("¿Quieres guardar la partida antes de tu turno? (S/N): ");
                if (respuesta.equalsIgnoreCase("S")) {
                    partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal() + 1, modalidad.ordinal() + 1, partida);
                    retornoPorGuardado = true;
                    return;
                }

                if (!mesa.esJugadaValida(turno)){
                    if (reglas instanceof ReglasConStock) {
                        ReglasConStock rc = (ReglasConStock) reglas;
                        if (rc.puedeRobarFicha(turno)) {
                            Output.mostrarConSalto("Has robado una ficha. Tu mano ahora es:");
                            turno.imprimirFichas(mesa);
                        } else {
                            Output.mostrarConSalto("No hay fichas en el stock. Pasas turno.");
                            partida.proximoTurno();
                            continue;
                        }
                    } else {
                        Output.mostrarConSalto("No tienes jugadas posibles. Pasas turno.");
                        partida.proximoTurno();
                        continue;
                    }
                }
                turno.imprimirFichas(mesa);
                partida.jugarTurno();
                partida.proximoTurno();

            } else {
                UtilidadesJuego.jugarTurnoAutomatico(partida);
                partida.proximoTurno();
            }
        }
        partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal()+1, modalidad.ordinal()+1, partida);
    }

    private Pais seleccionarPais() {
        Output.mostrarConSalto("Selecciona un país:");
        Pais[] listaPaises = Pais.values();
        for (int i = 0; i < listaPaises.length; i++) {
            Output.mostrarConSalto((i + 1) + "- " + listaPaises[i].getTitulo());
        }
        int opcion = Input.leerNumeroEntero("Opción: ") - 1;
        if (opcion < 0 || opcion >= listaPaises.length) {
            Output.error("Selección inválida. Usando Español por defecto.");
            return Pais.ESPANOL;
        }
        return listaPaises[opcion];
    }

    private Modalidad seleccionarModalidad() {
        Output.mostrarConSalto("Selecciona modalidad:");
        Modalidad[] listaModalidades = Modalidad.values();
        for (int i = 0; i < listaModalidades.length; i++) {
            Output.mostrarConSalto((i + 1) + "- " + listaModalidades[i].getTitulo());
        }
        int opcion = Input.leerNumeroEntero("Opción: ") - 1;
        if (opcion < 0 || opcion >= listaModalidades.length) {
            Output.error("Selección inválida. Usando Individual por defecto");
            return Modalidad.INDIVIDUAL;
        }
        return listaModalidades[opcion];
    }
}
