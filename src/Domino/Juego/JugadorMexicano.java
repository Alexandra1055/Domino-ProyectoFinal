package Domino.Juego;
import java.io.Serializable;
import java.util.ArrayList;
public class JugadorMexicano extends Jugador implements Serializable {
    private ArrayList<FichaDomino> trenPersonal;
    public JugadorMexicano(String nombre) {
        super(nombre);
        this.trenPersonal = new ArrayList<FichaDomino>();
    }
    public ArrayList<FichaDomino> getTrenPersonal() {
        return trenPersonal;
    }
    public void setTrenPersonal(ArrayList<FichaDomino> trenPersonal) {
        this.trenPersonal = trenPersonal;
    }
    public void agregarAlTrenPersonal(FichaDomino ficha){
        trenPersonal.add(ficha);
    }
    public void imprimirTrenPersonal(){
        for (int i = 0; i < trenPersonal.size(); i++) {
            System.out.print(trenPersonal.get(i).toString() + " ");
        }
        System.out.println();
    }
}
