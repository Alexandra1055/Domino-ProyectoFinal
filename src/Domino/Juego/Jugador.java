package Domino.Juego;
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
    public void imprimirFichas(){
        System.out.println(nombre + " tiene las fichas: ");
        for (int i = 0; i < fichas.size(); i++) {
            System.out.print(fichas.get(i).toString() + " ");
        }
        System.out.println();
    }
    public int getNumeroFichas(){
        return fichas.size();
    }
    public boolean tieneFichas(){
        return !fichas.isEmpty();
    }
}
