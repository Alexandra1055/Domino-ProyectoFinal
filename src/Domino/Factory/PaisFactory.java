package Domino.Factory;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Juego.JuegoDomino;
import Domino.Juego.Mesa;
import Domino.Juego.PartidaIndividual;
import Domino.Juego.PartidaParejas;
import Domino.Reglas.*;

public class PaisFactory {
    public static ReglasDomino crearReglas(Pais pais){
        switch (pais){
            case ESPANOL: return new ReglasEspanol();
            case MEXICANO: return new ReglasMexicano();
            case LATINO: return new ReglasLatino();
            case COLOMBIANO: return new ReglasColombiano();
            case CHILENO: return new ReglasChileno();
            case VENEZOLANO: return new ReglasVenezolano();
            case PONCE: return new ReglasPonce();
            default: throw new IllegalArgumentException("Pais no encontrado: " + pais);
        }
    }

}
