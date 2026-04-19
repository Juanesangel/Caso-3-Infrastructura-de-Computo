import java.io.*;
import java.util.*;
//Clase
public class Main {


    //Metodo main
    public static void main(String[] args) {


        //Ruta archivo
        String ruta = (args.length > 0) ? args[0] : "parametros.txt";


        //Mapa ya que llave valor
        Map<String, Integer> datos = new HashMap<>();

        //Lectura archivo
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("=");
                datos.put(partes[0].trim(), Integer.parseInt(partes[1].trim()));
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo");
            return;
        }
        System.out.println("Archivo leido: " + datos);


        //Datos
        int ni = datos.get("ni");
        int base = datos.get("base");
        int nc = datos.get("nc");
        int ns = datos.get("ns");
        int tam1 = datos.get("tam1");
        int tam2 = datos.get("tam2");

        System.out.println("=== CONFIGURACIÓN CARGADA ===");

        //Crear buzones
        BuzonEventos buzonEntrada = new BuzonEventos(Integer.MAX_VALUE);
        BuzonAlertas buzonAlertas = new BuzonAlertas(Integer.MAX_VALUE);
        BuzonClasificacion buzonClasificacion = new BuzonClasificacion(tam1);
        List<BuzonConsolidacion> buzonesServidores = new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            buzonesServidores.add(new BuzonConsolidacion(tam2));
        }

        //Calculo eventos
        int totalEventos = 0;
        for (int i = 1; i <= ni; i++) {
            totalEventos += base * i;
        }

        System.out.println("TOTAL EVENTOS = " + totalEventos);

        //Sensores
        List<Sensor> sensores = new ArrayList<>();
        for (int i = 1; i <= ni; i++) {
            int eventos = base * i;
            sensores.add(new Sensor(i, eventos, buzonEntrada, ns));
        }

        //Broker
        Broker broker = new Broker(
                buzonEntrada,
                buzonAlertas,
                buzonClasificacion,
                totalEventos
        );

        //Administrador
        Administrador admin = new Administrador(
                buzonAlertas,
                buzonClasificacion,
                nc
        );


        // Clasificadores
        Clasificador.setActivos(nc);
        List<Clasificador> clasificadores = new ArrayList<>();
        for (int i = 0; i < nc; i++) {
            clasificadores.add(new Clasificador(
                    buzonClasificacion,
                    buzonesServidores
            ));
}

        //Servidores
        List<Servidor> servidores = new ArrayList<>();
        for (int i = 0; i < ns; i++) {
            servidores.add(new Servidor(buzonesServidores.get(i)));
        }

        //Iniciar hilos
        sensores.forEach(Thread::start);
        broker.start();
        admin.start();
        clasificadores.forEach(Thread::start);
        servidores.forEach(Thread::start);

        //Esperar finalización
        try {
            for (Sensor s : sensores) s.join();
            broker.join();
            admin.join();
            for (Clasificador c : clasificadores) c.join();
            for (Servidor s : servidores) s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Revisar si buzones quedan vacios
        System.out.println("\n=== VALIDACIÓN DE BUZONES ===");
        System.out.println("BuzonEventos vacío: " + buzonEntrada.estaVacio());
        System.out.println("BuzonAlertas vacío: " + buzonAlertas.estaVacio());
        System.out.println("BuzonClasificacion vacío: " + buzonClasificacion.estaVacio());

        for (int i = 0; i < buzonesServidores.size(); i++) {
            System.out.println("BuzonConsolidacion " + i + " vacío: " +
                buzonesServidores.get(i).estaVacio());
        }


        //Final
        System.out.println("\n=== SISTEMA FINALIZADO ===");
    }
}