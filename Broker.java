import java.util.Random;

public class Broker extends Thread {

    private Buzon_alertas buzonEntrada;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;
    private int totalEventos;
    private Random rand = new Random();

    public Broker(Buzon_alertas entrada,
                  Buzon alertas,
                  Buzon clasificacion,
                  int totalEventos) {
        this.buzonEntrada = entrada;
        this.buzonAlertas = alertas;
        this.buzonClasificacion = clasificacion;
        this.totalEventos = totalEventos;
    }

    @Override
    public void run() {
        int procesados = 0;

        try {
            while (procesados < totalEventos) {

                Evento e = buzonEntrada.salir(); // espera si está vacío

                //generar número 0–200
                int num = rand.nextInt(201);

                if (num % 8 == 0) {
                    //anómalo
                    buzon_alertas.entrar(e);
                    System.out.println("Broker envió evento " +
                            e.getId() + " a ALERTAS");
                } else {
                    //normal
                    buzonClasificacion.entrar(e);
                    System.out.println("Broker envió evento " +
                            e.getId() + " a CLASIFICACION");
                }

                procesados++;
            }

            //enviar evento FIN al administrador
            Evento fin = new Evento("FIN", -1, true);
            buzonAlertas.entrar(fin);

            System.out.println("Broker terminó y envió FIN al Administrador");

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}