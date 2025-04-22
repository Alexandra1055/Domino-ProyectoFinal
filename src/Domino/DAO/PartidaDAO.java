package Domino.DAO;

import Domino.Juego.JuegoDomino;

import java.io.*;

public class PartidaDAO {

    public void guardarPartida(String id, JuegoDomino partida){
        try( ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( id + ".ser"))){
            oos.writeObject(partida);
            System.out.println("Partida " + id + " guardada correctamente.");
        }catch (IOException e){
            System.err.println("Error al guardar partida " + id + " : " + e.getMessage());
        }
    }

    public JuegoDomino cargarPartida(String id){
        try( ObjectInputStream ois = new ObjectInputStream( new FileInputStream( id + ".ser"))){
            return (JuegoDomino) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Error al cargar partida " + id + " : " + e.getMessage());
            return null;
        }
    }

}
