package Domino.Juego;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Domino.ENUMS.Pais;
import Domino.IO.Output;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private Map<Pais,Integer> puntuacionesMaximas;
    //probare un Map en lugar de una lista, cuya clave sea la variable Pais y la puntuacion maxima obtenida, asi el usuario podra consultar sus puntos al iniciar sesion


    public Usuario(String nombre){
        this.nombre = nombre;
        this.puntuacionesMaximas = new HashMap<Pais,Integer>();
    }

    public String getNombre() {
        return nombre;
    }

    //este metodo lo hago como idea, que si el usuario ha superado la puntuacion que tenia siempre le salga la maxima
    public void actualizarPuntuacion(Pais pais, int puntuacion){
        if (!puntuacionesMaximas.containsKey(pais) || puntuacion > puntuacionesMaximas.get(pais)){
            puntuacionesMaximas.put(pais,puntuacion);
        }
    }

    public int getPuntuacionMaxima(Pais pais){
        return puntuacionesMaximas.getOrDefault(pais,0);
    }

    public void imprimirPuntuaciones(){
        Output.mostrarConSalto("La puntuacion maxima de " + nombre + " es: ");
        ArrayList<Pais> claves = new ArrayList<Pais>(puntuacionesMaximas.keySet());

        for (int i = 0; i < claves.size(); i++) {
            Pais pais = claves.get(i);
            int puntuacion = puntuacionesMaximas.get(pais);

            Output.mostrarConSalto(pais.getTitulo() + ": " + puntuacion + " puntos");
        }
    }

}
