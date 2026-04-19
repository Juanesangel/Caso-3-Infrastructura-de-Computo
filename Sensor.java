import java.util.Random;

public class Sensor extends Thread {
    // Parametros
    private int id, numEventos, ns;
    private BuzonEventos buzon;
    private Random rand = new Random();
    // Constructor
    public Sensor(int id, int numEventos, BuzonEventos buzon, int ns) {
        this.id = id;
        this.numEventos = numEventos;
        this.buzon = buzon;
        this.ns = ns;
    }
    //Corre los threads 
    public void run() {
        for (int i = 1; i <= numEventos; i++) {

            int tipo = rand.nextInt(ns) + 1;
            Evento e = new Evento(id + "-" + i, tipo, false);

            buzon.entrar(e);
            System.out.println("Sensor " + id + " produjo " + e.getId());
        }
    }
}