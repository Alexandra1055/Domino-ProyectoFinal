package Domino.Main;

import Domino.DAO.PartidaDAO;
import Domino.DAO.UsuarioDAO;
import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Factory.ModalidadFactory;
import Domino.GestorJuego.ControladorPartida;
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
    public void iniciarJuego() {
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
                    ControladorPartida controlador = new ControladorPartida(usuario,usuarioDAO,partidaDAO);
                    controlador.iniciarPartidaNueva();
                    break;
                case 2:
                    cargarPartida();
                    break;
                case 3:
                    usuario.imprimirPuntuaciones();
                    break;
                case 4:
                    Output.mostrarConSalto("Saliendo del juego");
                    break;
                default:
                    Output.error("Opcion no valida");
            }
        } while (opcion != 4);
    }

    private void cargarPartida() {
        List<Integer> listaCodigos = partidaDAO.listaPartidasUsuario(usuario.getNombre());
        if (listaCodigos.isEmpty()) {
            Output.mostrarConSalto("No tienes ninguna partida guardada.");
            return;
        }

        Output.mostrarConSalto("Partidas guardadas para " + usuario.getNombre() + ":");

        for (int i = 0; i < listaCodigos.size(); i++) {
            int codigo = listaCodigos.get(i);
            int paisCod = codigo / 10;
            int modCod  = codigo % 10;
            Pais pais = Pais.values()[paisCod - 1];
            Modalidad mod = Modalidad.values()[modCod - 1];
            Output.mostrarConSalto((i + 1) + "- "
                    + pais.getTitulo() + " / " + mod.getTitulo());
        }

        int seleccion = Input.leerNumeroEntero("Selecciona la partida por número (1-" + listaCodigos.size() + "): ") - 1;

        if (seleccion < 0 || seleccion >= listaCodigos.size()) {
            Output.error("Selección inválida.");
            return;
        }

        int indicePais = listaCodigos.get(seleccion);
        int elegido = listaCodigos.get(seleccion);
        int paisCod = elegido / 10;
        int modCod  = elegido % 10;
        Pais pais = Pais.values()[paisCod - 1];
        Modalidad mod = Modalidad.values()[modCod - 1];

        JuegoDomino partida = partidaDAO.cargarPartida(usuario.getNombre(), paisCod, modCod);

        if (partida == null) {
            Output.error("Error al cargar la partida de " + pais.getTitulo() + " / " + mod.getTitulo());
        } else {
            Output.mostrarConSalto("Partida de " + pais.getTitulo() + " / " + mod.getTitulo() + " cargada correctamente");
        }
        ControladorPartida controlador = new ControladorPartida(usuario,usuarioDAO,partidaDAO);
        controlador.reanudarPartidaExistente(partida,pais,mod);
    }

}
