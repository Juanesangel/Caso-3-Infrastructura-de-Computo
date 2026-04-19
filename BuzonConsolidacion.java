import java.util.*;
//Clase
public class BuzonConsolidacion {


    //Parametros
    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;


    //Constructor
    public BuzonConsolidacion(int capacidad) {
        this.capacidad = capacidad;
    }


    //Entrar
    public synchronized void entrar(Evento e) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(e);
        notifyAll();
    }


    //Salir
    public synchronized Evento salir() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        Evento e = cola.poll();
        notifyAll();
        return e;
    }
    public synchronized boolean estaVacio() {
    return cola.isEmpty();
}
}