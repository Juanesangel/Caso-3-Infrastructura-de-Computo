import java.util.*;

public class BuzonAlertas {

    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;

    public BuzonAlertas(int capacidad) {
        this.capacidad = capacidad;
    }
    public synchronized void entrar(Evento e) throws InterruptedException {
        while (cola.size() == capacidad) {
            Thread.yield(); //Espera Semi activa
        }
        cola.add(e);
        notifyAll();
    }

    public synchronized Evento salir() throws InterruptedException {
        while (cola.isEmpty()) {
            Thread.yield();//Espera Semi activa

        }
        Evento e = cola.poll();
        notifyAll();
        return e;
    }
}