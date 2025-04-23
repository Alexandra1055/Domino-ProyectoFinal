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
        Pais pais = seleccionarPais();
        Modalidad modalidad = seleccionarModalidad();
        int mejorPuntuacion = usuario.getPuntuacionMaxima(pais);

        JuegoDomino partidaGuardada = partidaDAO.cargarPartida(usuario.getNombre(), pais.ordinal() + 1);

        if (partidaGuardada != null) {
            String respuesta = Input.leerLinea("Hay una partida guardada de " + pais.getTitulo() + ". ¿Continuar? (S/N): ");
            if (respuesta.equalsIgnoreCase("S")) {
                reanudarPartida(partidaGuardada, pais, mejorPuntuacion);
                return;
            }
        }

        nuevaPartida(pais, modalidad, mejorPuntuacion);
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

            bucleTurnos(partida, pais);

            partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal()+1, partida);

            UtilidadesJuego.procesarResultadoDePartida(partida, pais, mejorPuntuacion, usuario, usuarioDAO);

            String pregunta;
            if (tieneObjetivo) {
                pregunta = "Objetivo: " + objetivo + ". ¿Otra? (S/N): ";
            } else {
                pregunta = "Récord: " + mejorPuntuacion + ". ¿Otra? (S/N): ";
            }
            continuar = Input.leerLinea(pregunta).equalsIgnoreCase("S");
        }
        Output.mostrarConSalto("Fin en " + pais.getTitulo() + ". Máxima: " + usuario.getPuntuacionMaxima(pais));
    }

    private void reanudarPartida(JuegoDomino partida, Pais pais, int mejorPuntuacion) {
        Output.mostrarConSalto("Reanudando partida de " + pais.getTitulo());

        bucleTurnos(partida, pais);

        partidaDAO.guardarPartida(usuario.getNombre(), pais.ordinal()+1, partida);

        UtilidadesJuego.procesarResultadoDePartida(partida, pais, mejorPuntuacion, usuario, usuarioDAO);

    }

    private void bucleTurnos(JuegoDomino partida, Pais pais) {
        Mesa mesa = partida.getMesa();
        ReglasDomino reglas = partida.getReglas();

        while (reglas.sePuedeJugar(partida.getJugadores()) && !mesa.estaBloqueado(pais)) {
            mesa.imprimirMesa();
            Jugador turno = partida.getJugadorActual();

            if (turno.getNombre().equals(usuario.getNombre())) {
                partida.jugarTurno();
            } else {
                UtilidadesJuego.jugarTurnoAutomatico(partida);
            }

            if (reglas.sePuedeJugar(partida.getJugadores())) {
                partida.proximoTurno();
            }
        }
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

