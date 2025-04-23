package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasDomino;
import Domino.Reglas.ReglasMexicano;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class JuegoDomino implements Serializable {
    protected Pais pais;
    protected Modalidad modalidad;
    protected int puntuacionGanadora;
    protected ArrayList<Jugador> jugadores;
    protected int turnoActual;
    protected ReglasDomino reglas;
    protected Mesa mesa;
    protected Scanner imprimir;

    public JuegoDomino(Pais pais, Modalidad modalidad, ReglasDomino reglas, Mesa mesa) {
        this.pais = pais;
        this.modalidad = modalidad;
        this.puntuacionGanadora = pais.getPuntuacionGanadora();
        this.jugadores = new ArrayList<Jugador>();
        this.turnoActual = 0;
        this.reglas = reglas;
        this.mesa = mesa;
        this.imprimir = new Scanner(System.in);
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public abstract void iniciarPartida();

    public void mostrarEstado() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            System.out.println("Jugador " + (i + 1) + " : " + jugador.getNombre());
            jugador.imprimirFichas();
        }
    }

    public void proximoTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        System.out.println("Turno de : " + jugadores.get(turnoActual).getNombre());
    }

    public ReglasDomino getReglas() {
        return reglas;
    }

    public void setReglas(ReglasDomino reglas) {
        this.reglas = reglas;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    public void setTurnoActual(int turno) {
        if (turno >= 0 && turno < jugadores.size()) {
            this.turnoActual = turno;
        }
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public void jugarTurno() {
        Jugador jugadorActual = jugadores.get(turnoActual);
        jugadorActual.imprimirFichas();
        while (!mesa.esJugadaValida(jugadorActual)) {
            System.out.println("No tienes fichas jugables.");
            if (reglas != null && reglas instanceof ReglasConStock && ((ReglasConStock) reglas).puedeRobarFicha(jugadorActual)) {
                System.out.println("Ficha robada. Tu mano ahora es:");
                jugadorActual.imprimirFichas();
            } else {
                System.out.println("No hay fichas en el stock (o no se permite robar). Pasas turno.");
                return;
            }
        }
        boolean jugadaHecha = false;
        while (!jugadaHecha) {
            int index = pedirIndexFicha(jugadorActual);
            FichaDomino ficha = jugadorActual.getFichas().get(index);
            boolean intentoExitoso = false;
            if (reglas != null && reglas instanceof ReglasMexicano) {
                JugadorMexicano jugadorMexicano = (JugadorMexicano) jugadorActual;
                char movimiento = pedirOpcion("¿Dónde colocar la ficha? (M=mesa, T=tren): ", "MT");
                if (movimiento == 'M') {
                    char lado = pedirOpcion("¿En qué lado de la mesa? (I=izquierda, D=derecha): ", "ID");
                    if (lado == 'I') {
                        if (mesa.puedeColocarseIzquierda(ficha)) {
                            mesa.agregarFichaIzquierda(ficha);
                            jugadorActual.eliminarFicha(ficha);
                            System.out.println("Ficha colocada a la izquierda de la mesa.");
                            intentoExitoso = true;
                        } else {
                            System.out.println("La ficha no se puede colocar a la izquierda.");
                        }
                    } else {
                        if (mesa.puedeColocarseDerecha(ficha)) {
                            mesa.agregarFichaDerecha(ficha);
                            jugadorActual.eliminarFicha(ficha);
                            System.out.println("Ficha colocada a la derecha de la mesa.");
                            intentoExitoso = true;
                        } else {
                            System.out.println("La ficha no se puede colocar a la derecha.");
                        }
                    }
                } else {
                    jugadorMexicano.agregarAlTrenPersonal(ficha);
                    System.out.println("Ficha colocada en tu tren personal.");
                    intentoExitoso = true;
                }
            } else {
                char lado = pedirOpcion("¿En qué lado de la mesa deseas colocar la ficha " + ficha.toString() + "? (I = izquierda, D = derecha): ", "ID");
                if (lado == 'I') {
                    if (mesa.puedeColocarseIzquierda(ficha)) {
                        mesa.agregarFichaIzquierda(ficha);
                        jugadorActual.eliminarFicha(ficha);
                        System.out.println("Ficha colocada a la izquierda de la mesa.");
                        intentoExitoso = true;
                    } else {
                        System.out.println("La ficha no se puede colocar a la izquierda.");
                    }
                } else {
                    if (mesa.puedeColocarseDerecha(ficha)) {
                        mesa.agregarFichaDerecha(ficha);
                        jugadorActual.eliminarFicha(ficha);
                        System.out.println("Ficha colocada a la derecha de la mesa.");
                        intentoExitoso = true;
                    } else {
                        System.out.println("La ficha no se puede colocar a la derecha.");
                    }
                }
            }
            if (intentoExitoso) {
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

    public char pedirOpcion(String mensaje, String opcionesValidas) {
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
