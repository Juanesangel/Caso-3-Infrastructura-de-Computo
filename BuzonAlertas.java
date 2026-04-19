import java.util.*;
//Clase
public class BuzonAlertas {


    //Parametros
    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;


    //Constructor
    public BuzonAlertas(int capacidad) {
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
    public synchronized Evento salir() {
    if (cola.isEmpty()) {
        return null;
    }
    Evento e = cola.poll();
    notifyAll();
    return e;
}
public synchronized boolean estaVacio() {
    return cola.isEmpty();
}
}