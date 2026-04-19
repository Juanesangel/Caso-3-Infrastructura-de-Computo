import java.util.Random;

public class Administrador extends Thread {
    //Atributos
    private BuzonAlertas alertas;
    private BuzonClasificacion clasificacion;
    private int nc;
    private Random rand = new Random();
    // Constructor
    public Administrador(BuzonAlertas a, BuzonClasificacion c, int nc) {
        this.alertas = a;
        this.clasificacion = c;
        this.nc = nc;
    }
    //Corre los threads
    public void run() {
        try {
            while (true) {

                Evento e = alertas.salir();

                if (e.esFin()) break;

                if (rand.nextInt(21) % 4 == 0) {
                    clasificacion.entrar(e);
                }
                System.out.println("Admin revisa " + e.getId());
            }

            //pasa a clasificadores
            for (int i = 0; i < nc; i++) {
                clasificacion.entrar(new Evento("FIN", -1, true));
            }

        } catch (InterruptedException ex) {}
    }
}