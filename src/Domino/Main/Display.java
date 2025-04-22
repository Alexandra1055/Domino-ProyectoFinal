package Domino.Main;

import Domino.DAO.PartidaDAO;
import Domino.DAO.UsuarioDAO;
import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Factory.ModalidadFactory;
import Domino.IO.Input;
import Domino.IO.Output;
import Domino.Juego.JuegoDomino;
import Domino.Juego.Jugador;
import Domino.Juego.Usuario;

import java.util.List;

public class Display {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PartidaDAO partidaDAO = new PartidaDAO();
    private Usuario usuario;

    public void crearOcargarUsuario(){
        Output.mostrarConSalto("Bienvenido al Juego del Domino por Paises");
        String nombre = Input.leerLinea("Introduce tu nombre de Usuario: ");
        usuario = usuarioDAO.cargarUsuario(nombre);

        if (usuario == null){
            usuario = new Usuario(nombre);
            usuarioDAO.guardarUsuario(usuario);
            Output.mostrarConSalto("Usuario nuevo creado: " + nombre);
        }else {
            Output.mostrarConSalto("Usuario registrado: " + nombre);
        }

        menuPrincipal();
    }

    private void menuPrincipal(){
        int opcion;

        do{
            Output.mostrarConSalto("Menu Principal");
            Output.mostrarConSalto("Elige entre estas opciones: ");
            Output.mostrarConSalto("1- Iniciar nueva partida");
            Output.mostrarConSalto("2- Cargar partida guardada");
            Output.mostrarConSalto("3- Ver tu ranking de puntuaciones");
            Output.mostrarConSalto("4- Salir");
            opcion = Input.leerNumeroEntero("Opcion: ");
            switch ( opcion ){
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
                    break;
            }

        }while (opcion != 4);
    }

    private Pais elegirPais(){
        Output.mostrarConSalto("Selecciona un pais: ");
        Pais[] opciones = Pais.values();

        for (int i = 0; i < opciones.length; i++){
            Output.mostrarConSalto(( i + 1 ) + "- " + opciones[i].getTitulo());
        }

        int index = Input.leerNumeroEntero("Opcion: ") - 1;
        if ( index < 0 || index >= opciones.length ){
            Output.error("Seleccion invalida. Por defecto usaremos Domino Español");
            return Pais.ESPANOL;
        }
        return opciones[index];
    }

    private Modalidad elegirModalidad(){
        Output.mostrarConSalto("Selecciona una modalidad: ");
        Modalidad[] opciones = Modalidad.values();

        for (int i = 0; i < opciones.length; i++) {
            Output.mostrarConSalto(( i + 1 ) + "- " + opciones[i].getTitulo());
        }

        int index = Input.leerNumeroEntero("Opcion: ") - 1;
        if ( index < 0 || index >= opciones.length ){
            Output.error("Seleccion invalida. Por defecto usaremos modo individual");
            return Modalidad.INDIVIDUAL;
        }
        return opciones[index];
    }

    private void nuevaPartida(){
        Pais pais = elegirPais();
        Modalidad modalidad = elegirModalidad();
        JuegoDomino partida = ModalidadFactory.crearPartida(pais, modalidad);

        Jugador jugadorUsuario = new Jugador(usuario.getNombre());
        partida.agregarJugador(jugadorUsuario);

        partida.iniciarPartida();

        int puntuacion = partida.getReglas().calcularPuntuacion(partida.getJugadores());

        usuario.actualizarPuntuacion(pais, puntuacion);
        usuarioDAO.guardarUsuario(usuario);

        Output.mostrarConSalto("¡Partida Finalizada! Tu puntuacion es: " + puntuacion );
    }

    private void cargarPartida(){
        Output.mostrarConSalto("Partidas guardadas: ");
        List<String> ids = partidaDAO.listaPartidas();

        for (int i = 0; i < ids.size(); i++) {
            Output.mostrarConSalto(( i + 1 ) + ": " + ids.get(i));
        }

        String id = Input.leerLinea("ID de partida guardada: ");
        JuegoDomino partida = partidaDAO.cargarPartida(id);

        if ( partida != null ){
            Output.mostrarConSalto("Partida " + id + " cargada");
            partida.iniciarPartida();
        }
    }

    private void verPuntuaciones(){
        usuario.imprimirPuntuaciones();
    }

}
