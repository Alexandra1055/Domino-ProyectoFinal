package Domino.Reglas;

import Domino.Juego.FichaDomino;
import Domino.Juego.Jugador;
import Domino.Juego.JugadorMexicano;

import java.util.ArrayList;
import java.util.List;

public abstract class ReglasConStock implements ReglasDomino{
    protected ArrayList<FichaDomino> stock;

    public ReglasConStock(){
        stock = new ArrayList<>();
    }

    public boolean puedeRobarFicha(Jugador jugador){
        if (stock.isEmpty()){
            System.out.println("Ya no quedan fichas para robar");
            return false;
        }

        FichaDomino fichaRobada = stock.remove(0);
        jugador.agregarFicha(fichaRobada);

        System.out.println("El jugador " + jugador.getNombre() + " ha robado la ficha " + fichaRobada.toString() + " del stock");
        return true;
    }

    public ArrayList<FichaDomino> getStock() {
        return stock;
    }

    public abstract void iniciarMano(List<Jugador> jugadores);


}
