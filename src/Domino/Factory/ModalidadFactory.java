package Domino.Factory;

import Domino.ENUMS.Modalidad;
import Domino.ENUMS.Pais;
import Domino.Juego.JuegoDomino;
import Domino.Juego.Mesa;
import Domino.Juego.PartidaIndividual;
import Domino.Juego.PartidaParejas;
import Domino.Reglas.ReglasDomino;


public class ModalidadFactory {

    public static JuegoDomino crearPartida(Pais pais, Modalidad modalidad){
        ReglasDomino reglas = PaisFactory.crearReglas(pais);
        Mesa mesa = new Mesa();
        if (modalidad == Modalidad.INDIVIDUAL){
            return new PartidaIndividual(pais, modalidad, reglas, mesa);
        }else {
            return new PartidaParejas(pais, modalidad, reglas, mesa);
        }
    }
}
