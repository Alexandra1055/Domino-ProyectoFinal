package Domino.DAO;

import Domino.Juego.JuegoDomino;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {
    private static final String RUTA_PARTIDAS = "data/partidas";

    public PartidaDAO() {
        File carpeta = new File(RUTA_PARTIDAS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    } // creo una carpeta para guardar las partidas

    public void guardarPartida(String id, JuegoDomino partida){
        File archivo = new File(RUTA_PARTIDAS, id + ".ser");

        try( ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( archivo ))){
            oos.writeObject(partida);
            System.out.println("Partida " + id + " guardada correctamente.");
        }catch (IOException e){
            System.err.println("Error al guardar partida " + id + " : " + e.getMessage());
        }
    }// serializa

    public JuegoDomino cargarPartida(String id){
        File archivo = new File(RUTA_PARTIDAS, id + ".ser");

        if (!archivo.exists()) {
            System.err.println("No existe partida: " + archivo.getPath());
            return null;
        }

        try( ObjectInputStream ois = new ObjectInputStream( new FileInputStream( archivo ))){
            return (JuegoDomino) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Error al cargar partida " + id + " : " + e.getMessage());
            return null;
        }
    } //deserializa

    public List<String> listaPartidas(){
        File carpeta = new File(RUTA_PARTIDAS);
        File[] archivos = carpeta.listFiles();
        List<String> ids = new ArrayList<String>();

        if ( archivos != null ){
            for (int i = 0; i < archivos.length; i++) {
                String name = archivos[i].getName();
                if (name.endsWith(".ser")){
                    ids.add(name.substring( 0, name.length() - 4 ));
                }
            }
        }
        return ids;
    }

}
