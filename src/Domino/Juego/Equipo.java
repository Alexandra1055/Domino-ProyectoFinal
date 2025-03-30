package Domino.Juego;

import java.util.ArrayList;

public class Equipo {
    private String nombre; //equipo 1, equipo 2, por ejemplo, asi tengo donde meter los jugadores que van juntos
    private ArrayList<Jugador> jugadores;

    public Equipo(String nombre){
        this.nombre = nombre;
        this.jugadores = new ArrayList<Jugador>();
    }

    public boolean agregarJugador(Jugador jugador){
        if (jugadores.size() < 2){
            jugadores.add(jugador);
            return true;
        }else {
            System.out.println("Los equipos no pueden estar formados por mas de 2 jugadores");
            return false;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void imprimirFichasEquipo(){
        System.out.println("El equipo '" + nombre + "' ,tiene las fichas: ");

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            jugador.imprimirFichas(); //uso el metodo de jugador para los equipos
        }
    }

    public int contarFichasEquipo(){
        int total = 0;

        for (int i = 0; i < jugadores.size(); i++) {
            total += jugadores.get(i).getNumeroFichas();
        }

        return total;
    }//aqui lo unico que hago es contar las fichas que tiene cada jugador y sacar el total del equipo
}
