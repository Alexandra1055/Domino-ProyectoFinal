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
           JuegoDomino partida = ModalidadFactory.crearPartida(paisSeleccionado,modalidadSeleccionada);
           UtilidadesJuego.registrarJugadores(partida, usuario.getNombre());

           ReglasDomino reglas = partida.getReglas();
           reglas.iniciarMano(partida.getJugadores(), partida.getMesa());

           Jugador primerJugador = reglas.determinarJugadorInicial(partida.getJugadores());
           partida.setTurnoActual(partida.getJugadores().indexOf(primerJugador));
           Output.mostrarConSalto("Empieza el jugador: " + primerJugador.getNombre());

           Mesa mesa = partida.getMesa();
           while (reglas.sePuedeJugar(partida.getJugadores()) && !mesa.estaBloqueado(paisSeleccionado)) {
               if (partida.getJugadorActual().getNombre().equals(usuario.getNombre())) {
                   partida.jugarTurno();
               } else {
                   UtilidadesJuego.jugarTurnoAutomatico(partida);
               }
               if (reglas.sePuedeJugar(partida.getJugadores())) {
                   partida.proximoTurno();
               }
           }

           procesarResultadoDePartida(partida, paisSeleccionado, mejorPuntuacion);

           String respuesta;
           if (tieneObjetivo) {
               respuesta = Input.leerLinea("Objetivo: " + objetivoPuntos + ". ¿Otra partida para superarlo? (S/N): ");
           } else {
               respuesta = Input.leerLinea("Tu récord: " + mejorPuntuacion + ". ¿Otra partida para superarlo? (S/N): ");
           }
           continuarJuego = respuesta.equalsIgnoreCase("S");
       }
       Output.mostrarConSalto("Fin de la partida en " + paisSeleccionado.getTitulo() + ". Puntuacion maxima: " + usuario.getPuntuacionMaxima(paisSeleccionado));

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

    private void procesarResultadoDePartida(JuegoDomino partida, Pais paisSeleccionado, int mejorPuntuacionActual){
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

