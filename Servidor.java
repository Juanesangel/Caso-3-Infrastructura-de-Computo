import java.util.Random;

public class Servidor extends Thread {

    private BuzonConsolidacion buzon;
    private Random rand = new Random();

    public Servidor(BuzonConsolidacion b) {
        this.buzon = b;
    }

    public void run() {
        try {
            while (true) {

                Evento e = buzon.salir();

                if (e.esFin()) break;

                Thread.sleep(rand.nextInt(900) + 100);
                System.out.println("Servidor procesa " + e.getId());
            }

        } catch (InterruptedException ex) {}
    }
}