package Domino.Juego;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MazoDomino implements Serializable {
    private static final long serialVersionUID = 1L;
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

        int fichasRepartidas = 0;

        while (fichasRepartidas < cantidad && !fichas.isEmpty()){
            int index = random.nextInt(fichas.size());
            mano.add(fichas.remove(index));
            fichasRepartidas++;
        }

        jugador.setFichas(mano);
    }// Asigno las fichas a cada jugador

    public ArrayList<FichaDomino> getFichasRestantes() {
        return fichas;
    }

    public ArrayList<FichaDomino> getStock() {
        return fichas;
    }

}

