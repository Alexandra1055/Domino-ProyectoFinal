package Domino.ENUMS;

public enum Pais {
    ESPANOL("Dominó español", 28, 4, 100, "Comienza con el doble 6. Si nadie puede jugar, gana quien tiene menos puntos."),
    MEXICANO("Dominó mexicano", 55, 8, 0, "Usa tren personal y tren común. El doble debe cerrarse antes de jugar otra ficha."),
    LATINO("Dominó latino", 28, 4, 0, "Empieza quien tenga el doble mayor. Se permiten comunicaciones en pareja."),
    COLOMBIANO("Dominó colombiano", 28, 4, 0, "Se reparten 7 fichas. Gana la pareja con menos puntos en caso de cierre."),
    CHILENO("Dominó chileno", 28, 4, 200, "Si nadie tiene el doble más alto, se pasa al siguiente. Énfasis en bloqueos."),
    VENEZOLANO("Dominó venezolano", 28, 4, 100, "Sólo por parejas. Se juega siempre el doble más alto disponible."),
    PONCE("Dominó Ponce (Puerto Rico)", 55, 4, 200, "Juego por equipos. Estilo agresivo y centrado en bloqueos frecuentes.");


    private final String titulo;
    private final int totalFichas;
    private final int jugadoresMaximo;
    private final int puntuacionGanadora; // las que pongo 0 sera porque no tiene una especifica, se suman los puntos por ronda
    private final String reglaEspecial;


    Pais(String titulo, int totalFichas, int jugadoresMaximo, int puntuacionGanadora, String reglaEspecial) {
        this.titulo = titulo;
        this.totalFichas = totalFichas;
        this.jugadoresMaximo = jugadoresMaximo;
        this.puntuacionGanadora = puntuacionGanadora;
        this.reglaEspecial = reglaEspecial;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getTotalFichas() {
        return totalFichas;
    }

    public int getJugadoresMaximo() {
        return jugadoresMaximo;
    }

    public int getPuntuacionGanadora() {
        return puntuacionGanadora;
    }

    public String getReglaEspecial() {
        return reglaEspecial;
    }
}
