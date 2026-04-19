import java.util.*;
//Clase
public class BuzonClasificacion {


    //Parametros
    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;


    //Constructor
    public BuzonClasificacion(int capacidad) {
        this.capacidad = capacidad;
    }


    //Entrar
    public synchronized boolean entrar(Evento e) throws InterruptedException {
        if (cola.size() == capacidad) {
        return false;
        }
        cola.add(e);
        notifyAll();
        return true;
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