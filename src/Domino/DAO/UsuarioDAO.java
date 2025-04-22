package Domino.DAO;

import Domino.Juego.Usuario;

import java.io.*;

public class UsuarioDAO {

    public void guardarUsuario(Usuario usuario){
        String nombre = usuario.getNombre();
        try (ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( nombre + ".ser"))){
            oos.writeObject(usuario);
            System.out.println("Usuario " + nombre + " guardado correctamente");
        }catch ( IOException e ){
            System.err.println("Error al guardar el usuario " + nombre + " : " + e.getMessage());
        }
    }

    public Usuario cargarUsuario(String nombre){
        try (ObjectInputStream ois = new ObjectInputStream( new FileInputStream( nombre + ".ser"))){
            return (Usuario) ois.readObject();
        }catch ( IOException | ClassNotFoundException e ){
            System.err.println("Error al cargar el usuario " + nombre + " : " + e.getMessage());
            return null;
        }
    }
}
