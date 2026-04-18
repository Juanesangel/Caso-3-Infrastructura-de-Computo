import java.util.LinkedList;
import java.util.Queue;

public class Buzon_eventos {

    private Queue<Evento> cola = new LinkedList<>();

    // Productores
    public synchronized void entrar(Evento e) {
        cola.add(e);
        notifyAll(); // despierta al broker si estaba esperando
    }

    // Consumidor
    public synchronized Evento salir() throws InterruptedException {

        while (cola.isEmpty()) {
            Thread.yield(); // espera si no hay eventos
        }

        return cola.poll();
    }
}

