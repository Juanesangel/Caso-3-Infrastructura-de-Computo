public class Evento {

    private String id;
    private int tipo;
    private boolean esFin;

    public Evento(String id, int tipo, boolean esFin) {
        this.id = id;
        this.tipo = tipo;
        this.esFin = esFin;
    }

    public String getId() {
        return id;
    }

    public int getTipo() {
        return tipo;
    }

    public boolean esFin() {
        return esFin;
    }
}