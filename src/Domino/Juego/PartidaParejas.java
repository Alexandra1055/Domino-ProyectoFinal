package Domino.Juego;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;

import java.util.ArrayList;

public class PartidaParejas extends JuegoDomino{

    private ArrayList<Equipo> equipos;
    private int turnoEquipo;

    public PartidaParejas(Pais pais, Modalidad modalidad){
        super(pais,modalidad);
        this.equipos = new ArrayList<Equipo>();
        this.turnoEquipo = 0;
    }

    public void agregarEquipo(Equipo equipo){
        equipos.add(equipo);
    }

    @Override
    public void iniciarPartida(){
        System.out.println("Partida por Parejas de Dominó: " + pais.getTitulo());
    }

    @Override
    public void mostrarEstado(){

        for (int i = 0; i < equipos.size(); i++) {
            Equipo equipo = equipos.get(i);

            System.out.println("Equipo " + (i + 1) + " : " + equipo.getNombre());
            equipo.imprimirFichasEquipo();
        }
    }

    @Override
    public void proximoTurno(){
        if (equipos.isEmpty()){
            System.out.println("No se han agregado equipos a la partida");
            return;
        }
        turnoEquipo = (turnoEquipo + 1) % equipos.size(); //va cambiando de equipos en un bucle

        System.out.println("Turno del equipo : " + equipos.get(turnoEquipo).getNombre());
    }

}
