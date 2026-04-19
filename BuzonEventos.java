import java.util.*;
//Clase
public class BuzonEventos {


    //Parametros
    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad;


    //Constructor
    public BuzonEventos(int capacidad) {
    this.capacidad = capacidad;
    }


    //Entrar
    public synchronized boolean entrar(Evento e) {
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
            wait(); // Espera pasiva
        }
        return cola.poll();
    }
    public synchronized boolean estaVacio() {
    return cola.isEmpty();
}
}