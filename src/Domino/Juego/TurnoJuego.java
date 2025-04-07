package Domino.Juego;

import Domino.Reglas.ReglasConStock;
import java.util.Scanner;

import java.util.ArrayList;

public class TurnoJuego {
    private Mesa mesa;

    public TurnoJuego(Mesa mesa){
        this.mesa = mesa;
    }

    public void turnoJugador (Jugador jugador, ReglasConStock reglas){
        Scanner imprimir = new Scanner(System.in);
        System.out.println("Turno jugador " + jugador.getNombre());
        System.out.println("Tu mano actual:");
        jugador.imprimirFichas();

        while (!mesa.esJugadaValida(jugador)){
            System.out.println("El jugador no tiene fichas en la mano para colocar. Intenta robar del stock: ");
            boolean robarFicha = reglas.puedeRobarFicha(jugador);

            if (!robarFicha){
                System.out.println("No quedan fichas en el stock por lo que pasa el turno");
                return;
            }else {
                System.out.println("Roba una ficha del stock y mira si puede colocarla.");
            }
        }

        ArrayList<FichaDomino> mano = jugador.getFichas();
        boolean jugarFichaRobada = false;

        for (int i = 0; i < mano.size(); i++) {
            FichaDomino ficha = mano.get(i);

            if (mesa.puedeColocarseIzquierda(ficha)){
                System.out.println("Coloca la ficha robada a la izquieda");
                mesa.agregarFichaIzquierda(ficha);
                mano.remove(i);
                jugarFichaRobada = true;
                break;
            } else if (mesa.puedeColocarseDerecha(ficha)){
                System.out.println("Coloca la ficha robada a la derecha");
                mesa.agregarFichaDerecha(ficha);
                mano.remove(i);
                jugarFichaRobada = true;
                break;
            }
        }

        if (!jugarFichaRobada){
            System.out.println("El jugador " + jugador.getNombre() + " no tiene fichas para colocar pot lo que pasa el turno");
        }


    }
}
