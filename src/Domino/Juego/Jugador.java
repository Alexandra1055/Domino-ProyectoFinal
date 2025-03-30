package Domino.Juego;
import java.util.ArrayList;

public class Jugador {
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
            System.out.println(fichas.get(i).toString());
        }
    }//aqui lo que quiero es imprimir las dichas del jugador

    public int getNumeroFichas(){
        return fichas.size(); //para saber cuantas tiene
    }

    public boolean tieneFichas(){
        return !fichas.isEmpty(); //para cuando se quede sin
    }

    public static void main(String[] args) {
        FichaDomino ficha1 = new FichaDomino(6,6);
        FichaDomino ficha2 = new FichaDomino(3,4);
        FichaDomino ficha3 = new FichaDomino(2,5);

        Jugador jugador = new Jugador("Alexandra");


        jugador.agregarFicha(ficha1);
        jugador.agregarFicha(ficha2);
        jugador.agregarFicha(ficha3);

        System.out.println("Estado inicial:");
        jugador.imprimirFichas();
        boolean eliminada = jugador.eliminarFicha(ficha2);
        System.out.println("\n¿Se eliminó la ficha [3|5]? " + eliminada);

        System.out.println("\nEstado después de eliminar una ficha:");
        jugador.imprimirFichas();

        System.out.println("\nNúmero total de fichas: " + jugador.getNumeroFichas());
    }

}
