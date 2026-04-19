import java.util.*;
//Clase
public class Clasificador extends Thread {


    //Parametros
    private BuzonClasificacion clasificacion;
    private List<BuzonConsolidacion> servidores;
    private static int activos;
    private static final Object lock = new Object();


    public static void setActivos(int n) {
        synchronized (lock) {
            activos = n;
        }
    }


    //Constructor 
    public Clasificador(BuzonClasificacion c, List<BuzonConsolidacion> s) {
        this.clasificacion = c;
        this.servidores = s;
    }


    //Metodo run
    public void run() {
        try {
            while (true) {

                Evento e = clasificacion.salir();

                if (e.esFin()) break;

                int destino = e.getTipo() - 1;
                servidores.get(destino).entrar(e);

                System.out.println("Clasificador envía " + e.getId() +
                                " a servidor " + (destino + 1));
            }

            synchronized (lock) {
                activos--;

                if (activos == 0) {
                    // ultimo clasificador
                    for (BuzonConsolidacion b : servidores) {
                        b.entrar(new Evento("FIN", -1, true));
                    }
                }
            }

        } catch (InterruptedException ex) {}
    }
}