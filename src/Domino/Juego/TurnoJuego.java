package Domino.Juego;

import Domino.Reglas.ReglasConStock;
import java.util.Scanner;

import java.util.ArrayList;

public class TurnoJuego {
    private Mesa mesa;

    public TurnoJuego(Mesa mesa){
        this.mesa = mesa;
    }

    public void turnoJugador (Jugador jugador, ReglasConStock reglas) {

        Scanner imprimir = new Scanner(System.in);
        System.out.println("Turno jugador " + jugador.getNombre());
        System.out.println("Tu mano actual:");
        jugador.imprimirFichas();
        boolean jugarFichaRobada = false;

        while (!mesa.esJugadaValida(jugador)) {
            try {
                System.out.println("El jugador no tiene fichas en la mano para colocar. Intenta robar del stock: ");
                boolean robarFicha = reglas.puedeRobarFicha(jugador);

                if (!robarFicha) {
                    System.out.println("No quedan fichas en el stock por lo que pasa el turno");
                    return;
                } else {
                    System.out.println("Roba una ficha del stock y mira si puede colocarla.");
                }

                    int index = -1;
                    do {
                        System.out.println("¿Que ficha desea colocar? Introduce un numero, teniendo en cuenta que va de (0 a " + (jugador.getFichas().size()-1) + " )");

                        while (!imprimir.hasNextInt()) {
                            System.out.println("Error: El valor introducido no es un numero, por favor vuelve a intentarlo");
                            imprimir.next();
                        }
                        index = imprimir.nextInt();
                        imprimir.nextLine();
                        if (index < 0 || index >= jugador.getFichas().size()) {
                            System.out.println("Error: el numero introducido esta fuera de los margenes. Vuelve a intentarlo.");
                        }
                    }while (index < 0 || index >= jugador.getFichas().size());



                FichaDomino ficha = jugador.getFichas().get(index);
                char opcion;
                char opcionTren;

                do {
                    System.out.println("¿Dónde deseas colocar la ficha " + ficha.toString() + "?");
                    System.out.println("Escribe 'P' para tu tren personal o 'C' para el tren común:");
                    opcionTren = imprimir.nextLine().trim().toUpperCase().charAt(0);
                    if (opcionTren != 'P' && opcionTren != 'C') {
                        System.out.println("Opción inválida. Debe ser una 'P' o una 'C'");
                    }
                } while (opcionTren != 'P' && opcionTren != 'C');
                if (opcionTren == 'P') {
                    jugador.agregarAlTrenPersonal(ficha);
                    jugador.getFichas().remove(index);
                    System.out.println("Ficha " + ficha.toString() + " colocada en tu tren personal.");
                    jugarFichaRobada = true;
                } else if (opcionTren == 'C') {
                    mesa.agregarFichaDerecha(ficha);
                    jugador.getFichas().remove(index);
                    System.out.println("Ficha " + ficha.toString() + " colocada en el tren común.");
                    jugarFichaRobada = true;
                } else {
                    System.out.println("No se ha podido colocar la ficha. Pasas turno.");
                }

                do {
                        System.out.println("¿Donde quieres colocar la ficha: " + ficha.toString() + " ? (I) Izquierda o (D) Derecha");
                        opcion = imprimir.next().toUpperCase().charAt(0);
                        if (opcion != 'D' && opcion != 'I') {
                            System.out.println("Error: Debes introducir un 'D' o una 'I'");
                        }

                    } while (opcion != 'D' && opcion != 'I');

                    if (opcion == 'I' && mesa.puedeColocarseIzquierda(ficha)) {
                        System.out.println("Coloca la ficha robada a la izquieda");
                        mesa.agregarFichaIzquierda(ficha);
                        jugador.getFichas().remove(index);
                        jugarFichaRobada = true;
                        break;
                    } else if (opcion == 'D' && mesa.puedeColocarseDerecha(ficha)) {
                        System.out.println("Coloca la ficha robada a la derecha");
                        mesa.agregarFichaDerecha(ficha);
                        jugador.getFichas().remove(index);
                        jugarFichaRobada = true;
                        break;
                    } else {
                        System.out.println("No puedes colocar la ficha en ese lado. Intenta otra.");
                    }
            } catch (Exception e) {
                System.out.println("A ocurrido un error: " + e);
                System.out.println("Opción no válida. Inténtalo de nuevo.");
                continue;
            }

            if (!jugarFichaRobada) {
                System.out.println("El jugador " + jugador.getNombre() + " no tiene fichas para colocar por lo que pasa el turno");
            }

        }
    }


}
