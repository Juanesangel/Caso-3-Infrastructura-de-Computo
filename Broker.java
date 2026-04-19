import java.util.Random;

public class Broker extends Thread {
    //Parametros
    private BuzonEventos entrada;
    private BuzonAlertas alertas;
    private BuzonClasificacion clasificacion;
    private int total;
    private Random rand = new Random();
    //Constructor
    public Broker(BuzonEventos e, BuzonAlertas a, BuzonClasificacion c, int nc) {
        this.entrada = e;
        this.alertas = a;
        this.clasificacion = c;
        this.total = nc;
    }
    //Corre los threads
    public void run() {
        int procesados = 0;

        try {
            while (procesados < total) {

                Evento e = entrada.salir();

                if (rand.nextInt(201) % 8 == 0) {
                    alertas.entrar(e);
                } else {
                    clasificacion.entrar(e);
                }

                procesados++;
                System.out.println("Broker procesa " + e.getId());
            }

            alertas.entrar(new Evento("FIN", -1, true));

        } catch (InterruptedException ex) {}
        
    }
}