import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // 🔹 1. Leer archivo
        String ruta = (args.length > 0)
                ? args[0]
                : "C:\\Users\\juane\\OneDrive\\Desktop\\Universidad\\PRGRAMACION\\INFRATEC\\Caso-3-Infrastructura-de-Computo\\parametros.txt";

        Map<String, Integer> config = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("=");
                config.put(partes[0].trim(), Integer.parseInt(partes[1].trim()));
            }

        } catch (IOException e) {
            System.out.println("Error leyendo archivo de configuración");
            return;
        }

        System.out.println("CONFIG LEIDA: " + config);

        // Usar config (AHORA SÍ funciona)
        int ni = config.get("ni");
        int base = config.get("base");
        int nc = config.get("nc");
        int ns = config.get("ns");
        int tam1 = config.get("tam1");
        int tam2 = config.get("tam2");

        System.out.println("=== CONFIGURACIÓN CARGADA ===");

        // 🔹 resto de tu código sigue aquí...
        // 🔹 2. Crear buzones
        BuzonEventos buzonEntrada = new BuzonEventos();
        BuzonAlertas buzonAlertas = new BuzonAlertas(Integer.MAX_VALUE);
        BuzonClasificacion buzonClasificacion = new BuzonClasificacion(tam1);

        List<BuzonConsolidacion> buzonesServidores = new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            buzonesServidores.add(new BuzonConsolidacion(tam2));
        }

        // 🔹 3. Calcular total de eventos
        int totalEventos = 0;
        for (int i = 1; i <= ni; i++) {
            totalEventos += base * i;
        }

        // 🔹 4. Crear hilos

        // Sensores
        List<Sensor> sensores = new ArrayList<>();
        for (int i = 1; i <= ni; i++) {
            int eventos = base * i;
            sensores.add(new Sensor(i, eventos, buzonEntrada, ns));
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
        for (int i = 0; i < nc; i++) {
            clasificadores.add(new Clasificador(
                    buzonClasificacion,
                    buzonesServidores,
                    nc
            ));
        }

        // Servidores
        List<Servidor> servidores = new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            servidores.add(new Servidor(buzonesServidores.get(i)));
        }

        // 🔹 5. Iniciar hilos
        sensores.forEach(Thread::start);
        broker.start();
        admin.start();
        clasificadores.forEach(Thread::start);
        servidores.forEach(Thread::start);

        // 🔹 6. Esperar terminación
        try {
            for (Sensor s : sensores) s.join();
            broker.join();
            admin.join();
            for (Clasificador c : clasificadores) c.join();
            for (Servidor s : servidores) s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 🔹 7. Fin
        System.out.println("\n=== SISTEMA FINALIZADO ===");
    }
}