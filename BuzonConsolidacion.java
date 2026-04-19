import java.util.*;

public class BuzonConsolidacion {

    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;

    public BuzonConsolidacion(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void entrar(Evento e) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(e);
        notifyAll();
    }

    public synchronized Evento salir() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        Evento e = cola.poll();
        notifyAll();
        return e;
    }
}