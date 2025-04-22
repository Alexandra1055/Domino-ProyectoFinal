package Domino.IO;

import java.util.Scanner;

public class Input {
    private static final Scanner imprimir = new Scanner(System.in);

    public static String leerLinea (String mensaje){
        System.out.print(mensaje);
        return imprimir.nextLine();
    }

    public static int leerNumeroEntero(String mensaje){
        System.out.print(mensaje);
        while (!imprimir.hasNextInt()){
            System.out.print("Por favor, introduzca un numero entero");
            imprimir.next();
        }
        int valor = imprimir.nextInt();
        imprimir.nextLine();
        return valor;
    }
}
