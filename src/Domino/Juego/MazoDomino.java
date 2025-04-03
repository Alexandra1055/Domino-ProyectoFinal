package Domino.Juego;

import java.util.ArrayList;
import java.util.Random;

public class MazoDomino {
    ArrayList<FichaDomino> fichas;

    public MazoDomino() {
        fichas = new ArrayList<>();
    }

    public void crearFichas(int max) {
        for (int i = 0; i <= max; i++) {
            for (int j = i; j <= max; j++) {
                fichas.add(new FichaDomino(i, j));
            }
        }
    }
    public void repartirFichas(Jugador jugador, int cantidad) {
        ArrayList<FichaDomino> mano = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < cantidad && !fichas.isEmpty(); i++) {
            int index = random.nextInt(fichas.size());
            mano.add(fichas.remove(index));
        }

        jugador.setFichas(mano);
    }// Asigno las fichas a cada jugador

    public ArrayList<FichaDomino> getFichasRestantes() {
        return fichas;
    }

    public static void main(String[] args) {
        MazoDomino mazo = new MazoDomino();
        mazo.crearFichas(6); // crea 28 fichas

        Jugador jugador1 = new Jugador("Alexandra");
        Jugador jugador2 = new Jugador("Jaume");

        mazo.repartirFichas(jugador1, 7);
        mazo.repartirFichas(jugador2, 7);

        jugador1.imprimirFichas();
        jugador2.imprimirFichas();
    }
}

