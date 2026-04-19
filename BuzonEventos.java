import java.util.*;

public class BuzonEventos {

    private Queue<Evento> cola = new LinkedList<>();
    //FALTA LA ESPERA
    public synchronized void entrar(Evento e) {
        cola.add(e);
        notifyAll();
    }
    //Salir synchronized para evitar 
    public synchronized Evento salir() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); // Espera pasiva
        }
        return cola.poll();
    }
}