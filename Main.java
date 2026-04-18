import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        
        Map<String, Integer> config = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] partes = linea.split("=");
                config.put(partes[0].trim(), Integer.parseInt(partes[1].trim()));
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo");
            return;
        }

        // Valores
        int ni = config.get("ni");
        int base = config.get("base");
        int nc = config.get("nc");
        int ns = config.get("ns");
        int tam1 = config.get("tam1");
        int tam2 = config.get("tam2");

        System.out.println("Configuración cargada correctamente\n");

        // Verficar los valores cargados

        System.out.println("=== CONFIGURACIÓN ===");
        System.out.println("Sensores: " + ni);
        System.out.println("Base eventos: " + base);
        System.out.println("Clasificadores: " + nc);
        System.out.println("Servidores: " + ns);
        System.out.println();

        // Crear buzones
        Buzon buzonEntrada = new Buzon(Integer.MAX_VALUE); // ilimitado
        Buzon buzonAlertas = new Buzon(Integer.MAX_VALUE); // ilimitado
        Buzon buzonClasificacion = new Buzon(tam1);

        List<Buzon> buzonesServidores = new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            buzonesServidores.add(new Buzon(tam2));
        }

        // Calculo para los eventos eventos
        int totalEventos = 0;
        for (int i = 1; i <= ni; i++) {
            totalEventos += base * i;
        }

        //Crear hilos

        // Sensores
        List<Sensor> sensores = new ArrayList<>();
        for (int i = 1; i <= ni; i++) {
            int eventos = base * i;
            sensores.add(new Sensor(i, eventos, buzonEntrada));
        }

        // Broker
        Broker broker = new Broker(
                buzonEntrada,
                buzonAlertas,
                buzonClasificacion,
                totalEventos
        );

        // Administrador
        Administrador admin = new Administrador(
                buzonAlertas,
                buzonClasificacion,
                nc
        );

        // Clasificadores
        List<Clasificador> clasificadores = new ArrayList<>();
        for (int i = 1; i <= nc; i++) {
            clasificadores.add(new Clasificador(
                    i,
                    buzonClasificacion,
                    buzonesServidores,
                    ns,
                    nc
            ));
        }

        // Servidores
        List<Servidor> servidores = new ArrayList<>();
        for (int i = 1; i <= ns; i++) {
            servidores.add(new Servidor(i, buzonesServidores.get(i - 1)));
        }

        //Iniciar hilos

        sensores.forEach(Thread::start);
        broker.start();
        admin.start();
        clasificadores.forEach(Thread::start);
        servidores.forEach(Thread::start);

        //Esperar sensores (opcional pero limpio)
        try {
            for (Sensor s : sensores) {
                s.join();
            }

            broker.join();
            admin.join();

            for (Clasificador c : clasificadores) {
                c.join();
            }

            for (Servidor s : servidores) {
                s.join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fin del sistema
        System.out.println("\n=== SISTEMA FINALIZADO ===");
    }
}
    
