package Domino.Juego;
import Domino.IO.Output;

import java.io.Serializable;
import java.util.ArrayList;
public class Jugador implements Serializable {
    private String nombre;
    private ArrayList<FichaDomino> fichas;
    public Jugador(String nombre){
        this.nombre = nombre;
        this.fichas = new ArrayList<FichaDomino>();
    }
    public void agregarFicha(FichaDomino ficha) {
        fichas.add(ficha);
    }

    public boolean eliminarFicha(FichaDomino ficha){
        return fichas.remove(ficha);
    }

    public ArrayList<FichaDomino> getFichas() {
        return fichas;
    }

    public void setFichas(ArrayList<FichaDomino> fichas) {
        this.fichas = fichas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void imprimirFichas(Mesa mesa) {
        String verde = "\u001B[32m";
        String rojo = "\u001B[31m";
        String reset = "\u001B[0m";
        Output.mostrarConSalto("Estado actual de la mesa:");
        for (int i = 0; i < fichas.size(); i++) {
            FichaDomino ficha = fichas.get(i);
            boolean esJugable = mesa.puedeColocarseIzquierda(ficha) || mesa.puedeColocarseDerecha(ficha);
            String color;
            if (esJugable) {
                color = verde;
            } else {
                color = rojo;
            }
            Output.mostrarSinSalto(color + "[" + i + "]" + ficha.toString() + reset + " ");
        }
        Output.mostrarConSalto("");
    }

    public int getNumeroFichas(){
        return fichas.size();
    }

    public boolean tieneFichas(){
        return !fichas.isEmpty();
    }
}
