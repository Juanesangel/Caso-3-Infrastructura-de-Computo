import java.util.*;

public class BuzonClasificacion {

    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;

    public BuzonClasificacion(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void entrar(Evento e) throws InterruptedException {
        while (cola.size() == capacidad) {
            Thread.yield();//Espera Semi Activa
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