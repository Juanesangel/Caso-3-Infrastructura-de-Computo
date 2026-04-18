import java.util.Random;

public class Sensores extends Thread {

    private int id;
    private int numEventos;
    private Buzon buzonEntrada;
    private int ns; // número de servidores 
    private Random rand = new Random();

    public Sensores(int id, int numEventos, Buzon buzonEntrada, int ns) {
        this.id = id;
        this.numEventos = numEventos;
        this.buzonEntrada = buzonEntrada;
        this.ns = ns;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= numEventos; i++) {

                //tipo entre 1 y ns
                int tipo = rand.nextInt(ns) + 1;

                //id único (sensorId-evento)
                String idEvento = id + "-" + i;

                Evento evento = new Evento(idEvento, tipo, false);

                buzonEntrada.entrar(evento);

                System.out.println("Sensor " + id +
                        " produjo evento " + idEvento +
                        " (tipo " + tipo + ")");

                // pequeña pausa (opcional pero ayuda a ver concurrencia)
                Thread.sleep(rand.nextInt(200));

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}