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

    public void guardarPartida(String nombreUsuario, int indicePais, int indiceModalidad, JuegoDomino partida) {
        String nombreFichero = nombreUsuario + "_" + indicePais + "_" + indiceModalidad + ".ser";
        File archivo = new File(RUTA_PARTIDAS, nombreFichero);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(partida);
            System.out.println("Partida guardada para: " + nombreUsuario + "\n País " + indicePais
                    + " / Modalidad " + indiceModalidad);
        } catch (IOException e) {
            System.err.println("Error al guardar partida: " + e.getMessage());
        }
    } // serializa

    public JuegoDomino cargarPartida(String nombreUsuario, int indicePais, int indiceModalidad) {
        String nombreFichero = nombreUsuario + "_" + indicePais + "_" + indiceModalidad + ".ser";
        File archivo = new File(RUTA_PARTIDAS, nombreFichero);
        if (!archivo.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (JuegoDomino) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar partida: " + e.getMessage());
            return null;
        }
    } //deserializa

    public List<Integer> listaPartidasUsuario(String nombreUsuario) {
        File carpeta = new File(RUTA_PARTIDAS);
        File[] archivos = carpeta.listFiles();
        List<Integer> codigos = new ArrayList<Integer>();
        if (archivos != null) {
            for (int i = 0; i < archivos.length; i++) {
                String nombre = archivos[i].getName();
                if (nombre.startsWith(nombreUsuario + "_") && nombre.endsWith(".ser")) {
                    String parte = nombre.substring(
                            (nombreUsuario + "_").length(),
                            nombre.length() - 4
                    );
                    String[] trozos = parte.split("_");
                    if (trozos.length == 2) {
                        try {
                            int pais = Integer.parseInt(trozos[0]);
                            int mod = Integer.parseInt(trozos[1]);
                            codigos.add(pais * 10 + mod);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }
        return codigos;
    }// listamos las partidas guardadas

}
