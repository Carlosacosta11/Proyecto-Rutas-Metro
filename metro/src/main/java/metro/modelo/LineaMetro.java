package metro.modelo;

import metro.estructuras.ListaEnlazada;

public class LineaMetro {

    private final String codigoLinea;
    private final String nombre;
    private final ListaEnlazada<Estacion> estaciones;

    public LineaMetro(String codigoLinea, String nombre) {
        if (codigoLinea == null || codigoLinea.isBlank()) {
            throw new IllegalArgumentException("El código de línea no puede estar vacío.");
        }
        this.codigoLinea = codigoLinea.toUpperCase();
        this.nombre = nombre;
        this.estaciones = new ListaEnlazada<>();
    }

    public void agregarEstacion(Estacion estacion) {
        if (estacion == null) throw new IllegalArgumentException("La estación no puede ser null.");
        estacion.setCodigoLinea(codigoLinea);
        estaciones.agregar(estacion);
    }

    public boolean eliminarEstacion(String codigoEstacion) {
        for (int i = 0; i < estaciones.tamanio(); i++) {
            Estacion e = estaciones.obtener(i);
            if (e.getCodigo().equalsIgnoreCase(codigoEstacion)) {
                return estaciones.eliminar(e);
            }
        }
        return false;
    }

    public Estacion buscarEstacion(String codigoEstacion) {
        for (int i = 0; i < estaciones.tamanio(); i++) {
            Estacion e = estaciones.obtener(i);
            if (e.getCodigo().equalsIgnoreCase(codigoEstacion)) return e;
        }
        return null;
    }

    public String getCodigoLinea() {
        return codigoLinea;
    }

    public String getNombre() {
        return nombre;
    }

    public ListaEnlazada<Estacion> getEstaciones() {
        return estaciones;
    }

    public int cantidadEstaciones() {
        return estaciones.tamanio();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(codigoLinea + " - " + nombre + " (");
        sb.append(estaciones.tamanio()).append(" estaciones): ");
        // CORREGIDO: usar for loop en lugar de forEach
        for (int i = 0; i < estaciones.tamanio(); i++) {
            Estacion e = estaciones.obtener(i);
            sb.append(e.getNombre());
            if (i < estaciones.tamanio() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}