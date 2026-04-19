import java.util.*;

public class Clasificador extends Thread {

    private BuzonClasificacion clasificacion;
    private List<BuzonConsolidacion> servidores;

    private static int activos;
    private static final Object lock = new Object();

    public Clasificador(BuzonClasificacion c, List<BuzonConsolidacion> s, int nc) {
        this.clasificacion = c;
        this.servidores = s;

        synchronized (lock) {
            activos = nc;
        }
    }

    public void run() {
        try {
            while (true) {

                Evento e = clasificacion.salir();

                if (e.esFin()) break;

                int destino = e.getTipo() - 1;
                servidores.get(destino).entrar(e);
                System.out.println("Clasificador envía " + e.getId() + " a servidor " + (destino + 1));
            }

            synchronized (lock) {
                activos--;

                if (activos == 0) {
                    // último clasificador
                    for (BuzonConsolidacion b : servidores) {
                        b.entrar(new Evento("FIN", -1, true));
                    }
                }
            }
        } catch (InterruptedException ex) {}
    }
}