package Domino.IO;

public class Output {
    public static void mostrarConSalto(String mensaje){
        System.out.println(mensaje);
    }

    public static void mostrarSinSalto(String mensaje){
        System.out.print(mensaje);
    }

    public static void error(String mensaje){
        System.err.println("Ha ocurrido un error: " + mensaje);
    }
}
