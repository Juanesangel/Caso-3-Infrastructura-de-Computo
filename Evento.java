public class Evento {
    private String id;
    private int tipo;
    private boolean fin;

    public Evento(String id, int tipo, boolean fin) {
        this.id = id;
        this.tipo = tipo;
        this.fin = fin;
    }

    public String getId() { return id; }
    public int getTipo() { return tipo; }
    public boolean esFin() { return fin; }
}