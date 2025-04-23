package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.IO.Output;
import Domino.Reglas.ReglasConStock;
import Domino.Reglas.ReglasDomino;

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
    protected transient Scanner imprimir;

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
            Output.mostrarConSalto("Jugador " + (i + 1) + " : " + jugador.getNombre());
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
            Output.mostrarConSalto("No tienes fichas jugables.");
            if (reglas instanceof ReglasConStock
                    && ((ReglasConStock) reglas).puedeRobarFicha(jugadorActual)) {
                Output.mostrarConSalto("Ficha robada. Tu mano ahora es:");
                jugadorActual.imprimirFichas();
            } else {
                Output.mostrarConSalto("No hay fichas en el stock o no se permite robar. Pasas turno.");
                return;
            }
        }

        boolean jugadaHecha = false;
        while (!jugadaHecha) {
            int index = pedirIndexFicha(jugadorActual);
            FichaDomino ficha = jugadorActual.getFichas().get(index);
            boolean intentoExitoso = false;

            char lado = pedirOpcion(
                    "¿En qué lado de la mesa deseas colocar " + ficha.toString() + "? (I = izquierda, D = derecha): ",
                    "ID"
            );

            if (lado == 'I') {
                if (mesa.puedeColocarseIzquierda(ficha)) {
                    mesa.agregarFichaIzquierda(ficha);
                    jugadorActual.eliminarFicha(ficha);
                    Output.mostrarConSalto("Ficha colocada a la izquierda de la mesa.");
                    intentoExitoso = true;
                } else {
                    System.out.println("La ficha no se puede colocar a la izquierda.");
                }
            } else {
                if (mesa.puedeColocarseDerecha(ficha)) {
                    mesa.agregarFichaDerecha(ficha);
                    jugadorActual.eliminarFicha(ficha);
                    Output.mostrarConSalto("Ficha colocada a la derecha de la mesa.");
                    intentoExitoso = true;
                } else {
                    Output.mostrarConSalto("La ficha no se puede colocar a la derecha.");
                }
            }

            if (intentoExitoso) {
                jugadaHecha = true;
            } else {
                char reintentar = pedirOpcion(
                        "No se pudo realizar la jugada. ¿Quieres intentar de nuevo? (S = sí, N = no): ",
                        "SN"
                );
                if (reintentar == 'N') {
                    Output.mostrarConSalto("Pasas turno.");
                    break;
                }
            }
        }
    }

    private int pedirIndexFicha(Jugador jugador) {
        int total = jugador.getFichas().size();
        int index;
        do {
            Output.mostrarConSalto("¿Qué ficha deseas colocar? (0 a " + (total - 1) + "): ");
            while (!imprimir.hasNextInt()) {
                Output.mostrarConSalto("Error: introduce un número válido:");
                imprimir.next();
            }
            index = imprimir.nextInt();
            imprimir.nextLine();
        } while (index < 0 || index >= total);
        return index;
    }

    public char pedirOpcion(String mensaje, String opcionesValidas) {
        char opcion = ' ';
        boolean valido = false;
        while (!valido) {
            Output.mostrarConSalto(mensaje);
            String entrada = imprimir.nextLine().trim().toUpperCase();
            if (!entrada.isEmpty() && opcionesValidas.indexOf(entrada.charAt(0)) != -1) {
                opcion = entrada.charAt(0);
                valido = true;
            }
        }
        return opcion;
    }
}
