import java.util.Random;

//Clase
public class Broker extends Thread {


    //Parametros
    private BuzonEventos entrada;
    private BuzonAlertas alertas;
    private BuzonClasificacion clasificacion;
    private int total;
    private Random rand = new Random();


    //Constructor
    public Broker(BuzonEventos e, BuzonAlertas a, BuzonClasificacion c, int totalEventos) {
        this.entrada = e;
        this.alertas = a;
        this.clasificacion = c;
        this.total = totalEventos;
    }


    //Metodo Run
    public void run() {
    int procesados = 0;

    try {
        while (procesados < total) {
            Evento e = entrada.salir();
            boolean insertado = false;
            if (rand.nextInt(201) % 8 == 0) {
                //Semi activa
                while (!insertado) {
                    insertado = alertas.entrar(e);
                    if (!insertado) Thread.yield();
                }


            } else {
                //Semi activa
                while (!insertado) {
                    insertado = clasificacion.entrar(e);
                    if (!insertado) Thread.yield();
                }
            }
            procesados++;
            System.out.println("Broker procesa " + e.getId());
        }
        // FIN solo para admin
        alertas.entrar(new Evento("FIN", -1, true));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}