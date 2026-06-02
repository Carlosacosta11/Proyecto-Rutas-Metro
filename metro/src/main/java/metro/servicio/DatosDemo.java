package metro.servicio;

import metro.modelo.*;

public class DatosDemo {

    private static Estacion E1, E2, E3, E4, E5, E6, E7, E8;

    public static void cargar(MetroService service) {
        crearEstaciones();
        registrarLineas(service);
        conectarEstaciones(service);
        registrarPasajeros(service);
    }

    private static void crearEstaciones() {
        E1 = new Estacion("E1", "Terminal Norte", "L1");
        E2 = new Estacion("E2", "Plaza Central", "L1");
        E3 = new Estacion("E3", "Universidad", "L1");
        E4 = new Estacion("E4", "Parque Sur", "L1");
        E5 = new Estacion("E5", "Terminal Sur", "L1");
        E6 = new Estacion("E6", "Aeropuerto", "L2");
        E7 = new Estacion("E7", "Centro Comercial", "L2");
        E8 = new Estacion("E8", "Puerto", "L2");
    }

    private static void registrarLineas(MetroService service) {
        LineaMetro l1 = new LineaMetro("L1", "Línea Norte-Sur");
        l1.agregarEstacion(E1);
        l1.agregarEstacion(E2);
        l1.agregarEstacion(E3);
        l1.agregarEstacion(E4);
        l1.agregarEstacion(E5);
        service.registrarLinea(l1);

        LineaMetro l2 = new LineaMetro("L2", "Línea Este-Oeste");
        l2.agregarEstacion(E6);
        l2.agregarEstacion(E7);
        l2.agregarEstacion(E8);
        service.registrarLinea(l2);

        LineaMetro l3 = new LineaMetro("L3", "Línea Exprés");
        service.registrarLinea(l3);
    }

    private static void conectarEstaciones(MetroService service) {
        // Línea 1
        service.conectarEstaciones(E1, E2, 5);
        service.conectarEstaciones(E2, E3, 4);
        service.conectarEstaciones(E3, E4, 6);
        service.conectarEstaciones(E4, E5, 3);

        // Línea 2
        service.conectarEstaciones(E6, E7, 8);
        service.conectarEstaciones(E7, E8, 7);

        // Conexiones transversales
        service.conectarEstaciones(E2, E7, 10);
        service.conectarEstaciones(E3, E6, 12);
    }

    private static void registrarPasajeros(MetroService service) {
        service.registrarPasajero(new Pasajero("P001", "Ana García", "E1"));
        service.registrarPasajero(new Pasajero("P002", "Luis Pérez", "E1"));
        service.registrarPasajero(new Pasajero("P003", "María López", "E2"));
        service.registrarPasajero(new Pasajero("P004", "Carlos Ruiz", "E3"));
        service.registrarPasajero(new Pasajero("P005", "Elena Torres", "E6"));
        service.registrarPasajero(new Pasajero("P016", "Roberto Cano", "E2"));
        service.registrarPasajero(new Pasajero("P032", "Sandra Gil", "E4"));
    }
}