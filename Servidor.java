import java.util.Random;
//Clase
public class Servidor extends Thread {


    //Parametros
    private BuzonConsolidacion buzon;
    private Random rand = new Random();


    //Constructores 
    public Servidor(BuzonConsolidacion b) {
        this.buzon = b;
    }


    //Metodo run
    public void run() {
        try {
            while (true) {

                Evento e = buzon.salir();

                if (e.esFin()) break;

                Thread.sleep(rand.nextInt(900) + 100);
                System.out.println("Servidor procesa " + e.getId());
            }
        System.out.println("Servidor termina");
        } catch (InterruptedException ex) {}
    }
}