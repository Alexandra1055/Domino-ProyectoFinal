package Domino.ENUMS;

public enum Modalidad {
    INDIVIDUAL("Partida individual", 1),
    PAREJAS("Partida en pareja", 2);

    private final String titulo;
    private final int jugadoresPorEquipo;

    Modalidad(String titulo, int jugadoresPorEquipo){
        this.titulo = titulo;
        this.jugadoresPorEquipo = jugadoresPorEquipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getJugadoresPorEquipo() {
        return jugadoresPorEquipo;
    }
}
