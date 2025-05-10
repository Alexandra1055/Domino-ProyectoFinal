package Domino.Juego;

import Domino.ENUMS.Pais;
import Domino.IO.Output;
import Domino.Reglas.ReglasConStock;

import java.io.Serializable;
import java.util.ArrayList;

public class Mesa implements Serializable {
    private static final long serialVersionUID = 1L;
    ArrayList<FichaDomino> fichaMesa;

    public Mesa(){
        fichaMesa = new ArrayList<>();
    }

    public int valorFichaLadoIzquierdo() {
        if (!fichaMesa.isEmpty()) {
            return fichaMesa.get(0).getLado1();
        }
        return -1;
    }

    public int valorFichaLadoDerecho() {
        if (!fichaMesa.isEmpty()) {
            return fichaMesa.get(fichaMesa.size() - 1).getLado2();
        }
        return -1;
    }

    public void agregarFichaIzquierda(FichaDomino ficha) {
        if (!fichaMesa.isEmpty()) {
            int valorMesa = valorFichaLadoIzquierdo();
            if (ficha.getLado2() != valorMesa && ficha.getLado1() == valorMesa) {
                ficha = ficha.girarFicha();
            }
            if (!puedeColocarseIzquierda(ficha)) {
                Output.mostrarConSalto("La ficha " + ficha.toString() + " NO se puede colocar a la izquierda.");
                return;
            }
        }
        fichaMesa.add(0, ficha);
    }

    public void agregarFichaDerecha(FichaDomino ficha) {
        if (!fichaMesa.isEmpty()) {
            int valorMesa = valorFichaLadoDerecho();
            if (ficha.getLado1() != valorMesa && ficha.getLado2() == valorMesa) {
                ficha = ficha.girarFicha();
            }
            if (!puedeColocarseDerecha(ficha)) {
                Output.mostrarConSalto("La ficha " + ficha.toString() + " NO se puede colocar a la derecha.");
                return;
            }
        }
        fichaMesa.add(ficha);
    }

    public boolean puedeColocarseDerecha(FichaDomino ficha){
        if (fichaMesa.isEmpty()) {
            return true;
        }
        int valorMesa = valorFichaLadoDerecho();

        return (ficha.getLado1() == valorMesa || ficha.getLado2() == valorMesa);
    }

    public boolean puedeColocarseIzquierda(FichaDomino ficha){
        if (fichaMesa.isEmpty()) {
            return true;
        }
        int valorMesa = valorFichaLadoIzquierdo();

        return (ficha.getLado1() == valorMesa || ficha.getLado2() == valorMesa);
    }

    public boolean esJugadaValida(Jugador jugador){
        ArrayList<FichaDomino> fichas = jugador.getFichas();

        for (int i = 0; i < fichas.size(); i++) {
            FichaDomino ficha = fichas.get(i);

            if (puedeColocarseIzquierda(ficha) || puedeColocarseDerecha(ficha)){
                return true;
            }
        }
        return false;
    }

    public void imprimirMesa() {
        Output.mostrarConSalto("Estado actual de la mesa:");
        for (int i = 0; i < fichaMesa.size(); i++) {
            Output.mostrarSinSalto(fichaMesa.get(i).toString() + " ");
        }
        Output.mostrarConSalto("");
    }

    public boolean estaBloqueado(Pais pais) {
        if (fichaMesa.isEmpty()) {
            return false;
        }
        int extremoIzq = valorFichaLadoIzquierdo();
        int extremoDer = valorFichaLadoDerecho();
        int contadorIzq = 0;
        int contadorDer = 0;

        int totalFichas = pais.getTotalFichas();
        int umbral = (totalFichas == 28) ? 7 : 10;

        for (int i = 0; i < fichaMesa.size(); i++) {
            FichaDomino ficha = fichaMesa.get(i);
            if (ficha.getLado1() == extremoIzq || ficha.getLado2() == extremoIzq) {
                contadorIzq++;
            }
            if (ficha.getLado1() == extremoDer || ficha.getLado2() == extremoDer) {
                contadorDer++;
            }
        }
        return contadorIzq >= umbral && contadorDer >= umbral;
    }

}
