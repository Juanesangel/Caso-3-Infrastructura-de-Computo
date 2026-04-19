import java.util.Random;

public class Administrador extends Thread {

    private BuzonAlertas alertas;
    private BuzonClasificacion clasificacion;
    private int nc;
    private Random rand = new Random();

    public Administrador(BuzonAlertas a, BuzonClasificacion c, int nc) {
        this.alertas = a;
        this.clasificacion = c;
        this.nc = nc;
    }

    public void run() {
        try {
            while (true) {

                Evento e = null;

                //Semi Activa
                while (e == null) {
                    e = alertas.salir();
                    if (e == null) {
                        Thread.yield();
                    }
                }

                if (e.esFin()) break;

                System.out.println("Admin recibe: " + e.getId());

                //
                if (rand.nextInt(21) % 4 == 0) {

                    boolean insertado = false;

                    //
                    while (!insertado) {
                        insertado = clasificacion.entrar(e);
                        if (!insertado) {
                            Thread.yield();
                        }
                    }

                    System.out.println("Admin reenvía a clasificación: " + e.getId());
                } else {
                    System.out.println("Admin descarta: " + e.getId());
                }
            }

            // Smi 
            for (int i = 0; i < nc; i++) {

                boolean insertado = false;

                while (!insertado) {
                    insertado = clasificacion.entrar(new Evento("FIN", -1, true));
                    if (!insertado) {
                        Thread.yield();
                    }
                }

                System.out.println("Admin envía FIN a clasificador " + (i + 1));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}