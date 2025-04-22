package Domino.DAO;

import Domino.Juego.Usuario;

import java.io.*;

public class UsuarioDAO {
    private static final String RUTA_USUARIOS = "data/usuarios";

    public UsuarioDAO() {
        File carpeta = new File(RUTA_USUARIOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }// creo una carpeta para guardar los usuarios

    public void guardarUsuario(Usuario usuario){
        String nombre = usuario.getNombre();
        File archivo = new File(RUTA_USUARIOS, nombre + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( archivo ))){
            oos.writeObject(usuario);
            System.out.println("Usuario " + nombre + " guardado correctamente");
        }catch ( IOException e ){
            System.err.println("Error al guardar el usuario " + nombre + " : " + e.getMessage());
        }
    }// serializa

    public Usuario cargarUsuario(String nombre){
        File archivo = new File(RUTA_USUARIOS, nombre + ".ser");

        if (!archivo.exists()) {
            System.err.println("No existen usuarios: " + archivo.getPath());
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream( new FileInputStream( archivo ))){
            return (Usuario) ois.readObject();
        }catch ( IOException | ClassNotFoundException e ){
            System.err.println("Error al cargar el usuario " + nombre + " : " + e.getMessage());
            return null;
        }
    }// deserializa
}
