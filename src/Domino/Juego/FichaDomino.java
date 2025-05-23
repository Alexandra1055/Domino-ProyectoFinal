package Domino.Juego;

import java.io.Serializable;

public class FichaDomino implements Serializable {
    private static final long serialVersionUID = 1L;
    private int lado1;
    private int lado2;

    public FichaDomino(int lado1, int lado2){
        this.lado1 = lado1;
        this.lado2 = lado2;
    }// creo la clase ficha con los dos valores, el superior e inferior (sugun se mire)

    public int getLado1(){
        return lado1;
    }

    public int getLado2() {
        return lado2;
    }

    public FichaDomino girarFicha() {
        return new FichaDomino(lado2, lado1);
    }

    @Override
    public String toString(){
        return "\u001B[30m\u001B[47m[" + lado1 + "|" + lado2 + "]\u001B[0m";
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof FichaDomino)){
            return false;
        }

        FichaDomino otraFicha = (FichaDomino) obj;

        return ((lado1 == otraFicha.lado2 && lado2 == otraFicha.lado1) ||
                (lado1 == otraFicha.lado1 && lado2 == otraFicha.lado2));
    }// Aqui solo es para comprobar que son iguales tanto si esta de un lado o de otro la ficha
}
