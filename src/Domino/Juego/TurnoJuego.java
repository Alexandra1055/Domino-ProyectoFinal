package Domino.Juego;

import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasMexicano;

import java.util.Scanner;

public class TurnoJuego {
    private Mesa mesa;
    private Scanner imprimir;

    public TurnoJuego(Mesa mesa) {
        this.mesa = mesa;
        this.imprimir = new Scanner(System.in);
    }

    public void turnoJugador(Jugador jugador, ReglasConStock reglas) {
        System.out.println("Turno jugador " + jugador.getNombre());
        System.out.println("Tu mano actual:");
        jugador.imprimirFichas();

        while (!mesa.esJugadaValida(jugador)) {
            System.out.println("No tienes fichas jugables.");
            if (reglas != null && reglas.puedeRobarFicha(jugador)) {
                System.out.println("Ficha robada. Tu mano ahora es:");
                jugador.imprimirFichas();
            } else {
                System.out.println("No hay fichas en el stock (o no se permite robar). Pasas turno.");
                return;
            }
        }

        boolean jugadaHecha = false;

        while (!jugadaHecha) {
            int index = pedirIndexFicha(jugador);
            FichaDomino ficha = jugador.getFichas().get(index);
            boolean intentoExitoso = false;

            boolean esMexicano = false;
            if (reglas != null && reglas instanceof ReglasMexicano) {
                esMexicano = true;
            }

            if (esMexicano) {
                char movimiento = pedirOpcion("¿Dónde colocar la ficha? (M=mesa, T=tren): ", "MT");
                if (movimiento == 'M') {
                    char lado = pedirOpcion("¿En qué lado de la mesa? (I=izquierda, D=derecha): ", "ID");
                    if (lado == 'I') {
                        if (mesa.puedeColocarseIzquierda(ficha)) {
                            mesa.agregarFichaIzquierda(ficha);
                            System.out.println("Ficha colocada a la izquierda de la mesa.");
                            jugadaHecha = true;
                        } else {
                            System.out.println("La ficha no se puede colocar a la izquierda.");
                        }
                    } else {
                        if (mesa.puedeColocarseDerecha(ficha)) {
                            mesa.agregarFichaDerecha(ficha);
                            System.out.println("Ficha colocada a la derecha de la mesa.");
                            jugadaHecha = true;
                        } else {
                            System.out.println("La ficha no se puede colocar a la derecha.");
                        }
                    }
                } else {
                    jugador.agregarAlTrenPersonal(ficha);
                    System.out.println("Ficha colocada en tu tren personal.");
                    jugadaHecha = true;
                }
            } else {
                char lado = pedirOpcion("¿En qué lado de la mesa deseas colocar la ficha " + ficha.toString() + "? (I = izquierda, D = derecha): ", "ID");
                if (lado == 'I') {
                    if (mesa.puedeColocarseIzquierda(ficha)) {
                        mesa.agregarFichaIzquierda(ficha);
                        System.out.println("Ficha colocada a la izquierda de la mesa.");
                        intentoExitoso = true;
                    } else {
                        System.out.println("La ficha no se puede colocar a la izquierda.");
                    }
                } else {
                    if (mesa.puedeColocarseDerecha(ficha)) {
                        mesa.agregarFichaDerecha(ficha);
                        System.out.println("Ficha colocada a la derecha de la mesa.");
                        intentoExitoso = true;
                    } else {
                        System.out.println("La ficha no se puede colocar a la derecha.");
                    }
                }
            }

            if (intentoExitoso) {
                jugador.getFichas().remove(index);
                jugadaHecha = true;
            } else {
                char reintentar = pedirOpcion("No se pudo realizar la jugada. ¿Quieres intentar de nuevo? (S=si, N=no): ", "SN");
                if (reintentar == 'N') {
                    System.out.println("Pasas turno.");
                    break;
                }
            }
        }
    }

    private int pedirIndexFicha(Jugador jugador) {
        int total = jugador.getFichas().size();
        int index = -1;
        do {
            System.out.println("¿Qué ficha deseas colocar? Introduce un número (0 a " + (total - 1) + "): ");
            while (!imprimir.hasNextInt()) {
                System.out.println("Error: Ingresa un número válido:");
                imprimir.next();
            }
            index = imprimir.nextInt();
            imprimir.nextLine();
            if (index < 0 || index >= total) {
                System.out.println("Error: número fuera de rango.");
            }
        } while (index < 0 || index >= total);
        return index;
    }

    private char pedirOpcion(String mensaje, String opcionesValidas) {
        char opcion = ' ';
        boolean valido = false;
        while (!valido) {
            System.out.println(mensaje);
            String entrada = imprimir.nextLine().trim().toUpperCase();
            if (!entrada.isEmpty()) {
                opcion = entrada.charAt(0);
                if (opcionesValidas.indexOf(opcion) != -1) {
                    valido = true;
                }
            }
            if (!valido) {
                System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
        return opcion;
    }
}
