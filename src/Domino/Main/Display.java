package Domino.Main;

import Domino.DAO.PartidaDAO;
import Domino.DAO.UsuarioDAO;
import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Factory.ModalidadFactory;
import Domino.IO.Input;
import Domino.IO.Output;
import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.JuegoDomino;
import Domino.Juego.Mesa;
import Domino.Juego.Usuario;
import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasDomino;

import java.util.List;

public class Display {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PartidaDAO partidaDAO = new PartidaDAO();
    private Usuario usuario;

    public void crearOcargarUsuario() {
        Output.mostrarConSalto("Bienvenido al Juego del Domino por Paises");
        String nombre = Input.leerLinea("Introduce tu nombre de Usuario: ");
        usuario = usuarioDAO.cargarUsuario(nombre);
        if (usuario == null) {
            usuario = new Usuario(nombre);
            usuarioDAO.guardarUsuario(usuario);
            Output.mostrarConSalto("Usuario nuevo creado: " + nombre);
        } else {
            Output.mostrarConSalto("Usuario registrado: " + nombre);
        }
        menuPrincipal();
    }

    private void menuPrincipal() {
        int opcion;
        do {
            Output.mostrarConSalto("Menu Principal");
            Output.mostrarConSalto("Elige entre estas opciones:");
            Output.mostrarConSalto("1- Iniciar nueva partida");
            Output.mostrarConSalto("2- Cargar partida guardada");
            Output.mostrarConSalto("3- Ver tu ranking de puntuaciones");
            Output.mostrarConSalto("4- Salir");
            opcion = Input.leerNumeroEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    nuevaPartida();
                    break;
                case 2:
                    cargarPartida();
                    break;
                case 3:
                    verPuntuaciones();
                    break;
                case 4:
                    Output.mostrarConSalto("Saliendo del juego");
                    break;
                default:
                    Output.error("Opcion no valida");
            }
        } while (opcion != 4);
    }

    private Pais elegirPais() {
        Output.mostrarConSalto("Selecciona un pais:");
        Pais[] opciones = Pais.values();
        for (int i = 0; i < opciones.length; i++) {
            Output.mostrarConSalto((i + 1) + "- " + opciones[i].getTitulo());
        }
        int index = Input.leerNumeroEntero("Opcion: ") - 1;
        if (index < 0 || index >= opciones.length) {
            Output.error("Seleccion invalida. Por defecto usaremos Domino Español");
            return Pais.ESPANOL;
        }
        return opciones[index];
    }

    private Modalidad elegirModalidad() {
        Output.mostrarConSalto("Selecciona una modalidad:");
        Modalidad[] opciones = Modalidad.values();
        for (int i = 0; i < opciones.length; i++) {
            Output.mostrarConSalto((i + 1) + "- " + opciones[i].getTitulo());
        }
        int index = Input.leerNumeroEntero("Opcion: ") - 1;
        if (index < 0 || index >= opciones.length) {
            Output.error("Seleccion invalida. Por defecto usaremos modo individual");
            return Modalidad.INDIVIDUAL;
        }
        return opciones[index];
    }

    private void nuevaPartida() {
        Pais pais = elegirPais();
        Modalidad modalidad = elegirModalidad();
        int objetivo = pais.getPuntuacionGanadora();
        boolean tieneObjetivo = pais.tieneObjetivo();
        int mejorPuntuacion = usuario.getPuntuacionMaxima(pais);
        boolean continuarPartida = true;
        do {
            JuegoDomino partida = ModalidadFactory.crearPartida(pais, modalidad);
            partida.agregarJugador(new Jugador(usuario.getNombre()));

            for (int i = 2; i <= 4; i++) {
                partida.agregarJugador(new Jugador("Jugador " + i));
            }

            ReglasDomino reglas = partida.getReglas();
            reglas.iniciarMano(partida.getJugadores());
            Jugador primero = reglas.determinarJugadorInicial(partida.getJugadores());
            int indexPrimero = partida.getJugadores().indexOf(primero);
            partida.setTurnoActual(indexPrimero);
            Output.mostrarConSalto("Empieza el jugador: " + primero.getNombre());
            colocarFichaInicial(primero, partida.getMesa());
            partida.proximoTurno();

            Mesa mesa = partida.getMesa();
            while (reglas.sePuedeJugar(partida.getJugadores()) && !partida.getMesa().estaBloqueado(pais)){
                mesa.imprimirMesa();
                Jugador actual = partida.getJugadorActual();
                if (actual.getNombre().equals(usuario.getNombre())) {
                    partida.jugarTurno();
                } else {
                    jugarTurnoJugadorAleatrio(partida);
                }
                if (reglas.sePuedeJugar(partida.getJugadores())) {
                    partida.proximoTurno();
                }
            }

            boolean bloqueo = reglas.aplicaBloqueo(mesa, pais);
            Jugador ganadorFinal;
            int puntosFinales;

            if (bloqueo) {
                ganadorFinal = reglas.determinarGanadorBloqueo(partida.getJugadores(), mesa, pais, partida.getTurnoActual());
                puntosFinales = reglas.puntosBloqueo();
                if (ganadorFinal != null) {
                    Output.mostrarConSalto("Bloqueo: gana " + ganadorFinal.getNombre() + " y obtiene " + puntosFinales + " puntos.");
                } else {
                    Output.mostrarConSalto("Bloqueo: ningún jugador recibe puntos.");
                }
            } else {
                ganadorFinal = null;
                for (int i = 0; i < partida.getJugadores().size(); i++) {
                    Jugador jugador = partida.getJugadores().get(i);
                    if (jugador.getFichas().isEmpty()) {
                        ganadorFinal = jugador;
                        break;
                    }
                }
                if (ganadorFinal == null) {
                    int sumaMinima = Integer.MAX_VALUE;
                    for (int i = 0; i < partida.getJugadores().size(); i++) {
                        Jugador jugador = partida.getJugadores().get(i);
                        int sumaFichas = 0;
                        for (int j = 0; j < jugador.getFichas().size(); j++) {
                            FichaDomino ficha = jugador.getFichas().get(j);
                            sumaFichas += ficha.getLado1() + ficha.getLado2();
                        }
                        if (sumaFichas < sumaMinima) {
                            sumaMinima = sumaFichas;
                            ganadorFinal = jugador;
                        }
                    }
                }
                puntosFinales = reglas.calcularPuntuacion(partida.getJugadores());
                Output.mostrarConSalto("Ha ganado: " + ganadorFinal.getNombre() + " con " + puntosFinales + " puntos");
            }

            if (ganadorFinal.getNombre().equals(usuario.getNombre()) && puntosFinales > mejorPuntuacion){
                mejorPuntuacion = puntosFinales;
                usuario.actualizarPuntuacion(pais,puntosFinales);
                usuarioDAO.guardarUsuario(usuario);
                Output.mostrarConSalto("¡Nuevo record en " + pais.getTitulo() + "!");
            }

            String guardar = Input.leerLinea("¿Quieres guardar esta partida? (S/N): ");
            if (guardar.equalsIgnoreCase("S")) {
                String id = String.valueOf(pais.ordinal() + 1) + String.valueOf(modalidad.ordinal() + 1);
                partidaDAO.guardarPartida(id, partida);
            }

            String mensaje;
            if (tieneObjetivo) {
                mensaje = "Objetivo: " + objetivo + ". ¿Otra partida para superarlo? (S/N): ";
            } else {
                mensaje = "Tu récord: " + mejorPuntuacion + ". ¿Otra partida para mejorarlo? (S/N): ";
            }

            String superarRecord = Input.leerLinea(mensaje);
            continuarPartida = superarRecord.equalsIgnoreCase("S");

        } while (continuarPartida);

        Output.mostrarConSalto("Fin de sesión en " + pais.getTitulo() + ". Mejor puntuación: " + mejorPuntuacion + " puntuacion");
    }

    private void colocarFichaInicial(Jugador primero, Mesa mesa) {
        List<FichaDomino> mano = primero.getFichas();
        FichaDomino primeraFicha = null;
        for (int i = 0; i < mano.size(); i++) {
            FichaDomino ficha = mano.get(i);
            if (ficha.getLado1() == ficha.getLado2()) {
                if (primeraFicha == null || ficha.getLado1() > primeraFicha.getLado1()) {
                    primeraFicha = ficha;
                }
            }
        }
        if (primeraFicha != null) {
            mano.remove(primeraFicha);
            mesa.agregarFichaDerecha(primeraFicha);
            Output.mostrarConSalto("Ficha inicial colocada: " + primeraFicha);
        }
    }

    private void jugarTurnoJugadorAleatrio(JuegoDomino partida) {
        Jugador jugadorAleatorio = partida.getJugadorActual();
        Mesa mesa = partida.getMesa();
        ReglasDomino reglas = partida.getReglas();
        if (!mesa.esJugadaValida(jugadorAleatorio)) {
            if (reglas instanceof ReglasConStock) {
                ((ReglasConStock) reglas).puedeRobarFicha(jugadorAleatorio);
            }
            return;
        }
        List<FichaDomino> mano = jugadorAleatorio.getFichas();
        for (int i = 0; i < mano.size(); i++) {
            FichaDomino ficha = mano.get(i);
            if (mesa.puedeColocarseIzquierda(ficha)) {
                mano.remove(i);
                mesa.agregarFichaIzquierda(ficha);
                return;
            }
            if (mesa.puedeColocarseDerecha(ficha)) {
                mano.remove(i);
                mesa.agregarFichaDerecha(ficha);
                return;
            }
        }
    }

    private void cargarPartida() {
        Output.mostrarConSalto("Partidas guardadas:");
        List<String> ids = partidaDAO.listaPartidas();
        for (int i = 0; i < ids.size(); i++) {
            Output.mostrarConSalto((i + 1) + ": " + ids.get(i));
        }
        String id = Input.leerLinea("ID de partida guardada: ");
        JuegoDomino partida = partidaDAO.cargarPartida(id);
        if (partida != null) {
            Output.mostrarConSalto("Partida " + id + " cargada");
            partida.iniciarPartida();
        }
    }

    private void verPuntuaciones() {
        usuario.imprimirPuntuaciones();
    }
}
