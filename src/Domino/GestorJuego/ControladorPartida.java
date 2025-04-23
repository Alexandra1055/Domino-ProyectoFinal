package Domino.GestorJuego;

import Domino.DAO.PartidaDAO;
import Domino.DAO.UsuarioDAO;
import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Factory.ModalidadFactory;
import Domino.IO.Input;
import Domino.IO.Output;
import Domino.Juego.JuegoDomino;
import Domino.Juego.Jugador;
import Domino.Juego.Mesa;
import Domino.Juego.Usuario;
import Domino.Reglas.ReglasDomino;

public class ControladorPartida {
    private Usuario usuario;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PartidaDAO partidaDAO = new PartidaDAO();

    public ControladorPartida(Usuario usuario, UsuarioDAO usuarioDAO, PartidaDAO partidaDAO){
        this.usuario = usuario;
        this.usuarioDAO = usuarioDAO;
        this.partidaDAO = partidaDAO;
    }

    public void iniciarPartidaNueva(){
        Pais paisSeleccionado = seleccionarPais();
        Modalidad modalidadSeleccionada = seleccionarModalidad();
        int objetivoPuntos = paisSeleccionado.getPuntuacionGanadora();
        boolean tieneObjetivo = paisSeleccionado.tieneObjetivo();
        int mejorPuntuacion = usuario.getPuntuacionMaxima(paisSeleccionado);

        boolean continuarJuego = true;
        while (continuarJuego){
            JuegoDomino partida = ModalidadFactory.crearPartida(paisSeleccionado, modalidadSeleccionada);
            UtilidadesJuego.registrarJugadores(partida, usuario.getNombre());
            Mesa mesa = partida.getMesa();
            ReglasDomino reglas = partida.getReglas();

            reglas.iniciarMano(partida.getJugadores(), mesa);

            Jugador primero = reglas.determinarJugadorInicial(partida.getJugadores());
            partida.setTurnoActual(partida.getJugadores().indexOf(primero));
            Output.mostrarConSalto("Empieza el jugador: " + primero.getNombre());
            mesa.imprimirMesa();
            partida.proximoTurno();

            while (reglas.sePuedeJugar(partida.getJugadores()) && !mesa.estaBloqueado(paisSeleccionado)) {
                Jugador turno = partida.getJugadorActual();

                if (turno.getNombre().equals(usuario.getNombre())) {
                    partida.jugarTurno();
                } else {
                    UtilidadesJuego.jugarTurnoAutomatico(partida);
                    mesa.imprimirMesa();
                }
                if (reglas.sePuedeJugar(partida.getJugadores())) {
                    partida.proximoTurno();
                }
            }

            UtilidadesJuego.procesarResultadoDePartida(partida,paisSeleccionado,mejorPuntuacion,usuario,usuarioDAO);

            String pregunta;
            if (tieneObjetivo) {
                pregunta = "Objetivo: " + objetivoPuntos + ". ¿Otra partida para superarlo? (S/N): ";
            } else {
                pregunta = "Tu récord: " + mejorPuntuacion + ". ¿Otra partida para superarlo? (S/N): ";
            }
            String respuesta = Input.leerLinea(pregunta);
            continuarJuego = respuesta.equalsIgnoreCase("S");
        }
        Output.mostrarConSalto("Fin de la partida en " + paisSeleccionado.getTitulo() + ". Puntuación máxima: " + usuario.getPuntuacionMaxima(paisSeleccionado));
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

