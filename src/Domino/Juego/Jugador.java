package Domino.Juego;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<FichaDomino> fichas;
    private ArrayList<FichaDomino> trenPersonal; // atributo para el tren personal que usaremos para el juego mexicano

    public Jugador(String nombre){
        this.nombre = nombre;
        this.fichas = new ArrayList<FichaDomino>();
        this.trenPersonal = new ArrayList<FichaDomino>();
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

    //metodos para el tren personal: get, set, añadire imprimir
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


    public static void main(String[] args) {
        FichaDomino ficha1 = new FichaDomino(6,6);
        FichaDomino ficha2 = new FichaDomino(3,4);
        FichaDomino ficha3 = new FichaDomino(2,5);
        FichaDomino ficha4 = new FichaDomino(0,1);

        Jugador jugador1 = new Jugador("Alexandra");

        Jugador jugador2 = new Jugador("Jaume");


        jugador1.agregarFicha(ficha1);
        jugador1.agregarFicha(ficha2);
        jugador2.agregarFicha(ficha3);
        jugador2.agregarFicha(ficha4);

        Equipo equipo1 = new Equipo("Equipo 1");
        equipo1.agregarJugador(jugador1);
        equipo1.agregarJugador(jugador2);

        equipo1.imprimirFichasEquipo();

        int totalFichas = equipo1.contarFichasEquipo();
        System.out.println("total fichas equipo: " + totalFichas);

    }

}
