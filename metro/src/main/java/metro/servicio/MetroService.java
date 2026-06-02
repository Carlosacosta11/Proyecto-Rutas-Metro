package metro.servicio;

import metro.estructuras.*;
import metro.modelo.*;

public class MetroService {

    private final Cola<Pasajero> anden;
    private final Pila<ConsultaRuta> historialRutas;
    private final TablaHash<String, Pasajero> registroPasajeros;
    private final ArbolBinarioBusqueda<String, LineaMetro> catalogoLineas;
    private final Grafo<Estacion> redMetro;

    public MetroService() {
        this.anden = new Cola<>();
        this.historialRutas = new Pila<>();
        this.registroPasajeros = new TablaHash<>(10);
        this.catalogoLineas = new ArbolBinarioBusqueda<>();
        this.redMetro = new Grafo<>();
    }

    public void registrarLinea(LineaMetro linea) {
        if (linea == null) return;
        catalogoLineas.insertar(linea.getCodigoLinea(), linea);
        for (int i = 0; i < linea.getEstaciones().tamanio(); i++) {
            Estacion estacion = linea.getEstaciones().obtener(i);
            redMetro.agregarNodo(estacion);
        }
    }

    public LineaMetro buscarLinea(String codigoLinea) {
        if (codigoLinea == null) return null;
        return catalogoLineas.buscar(codigoLinea.toUpperCase());
    }

    public boolean agregarEstacionALinea(String codigoLinea, Estacion estacion) {
        if (codigoLinea == null || estacion == null) return false;
        LineaMetro linea = buscarLinea(codigoLinea);
        if (linea == null) return false;
        linea.agregarEstacion(estacion);
        redMetro.agregarNodo(estacion);
        return true;
    }

    public void conectarEstaciones(Estacion origen, Estacion destino, int minutos) {
        if (origen == null || destino == null || minutos <= 0) return;
        redMetro.agregarArista(origen, destino, minutos);
    }

    public void registrarPasajero(Pasajero pasajero) {
        if (pasajero == null) return;
        registroPasajeros.insertar(pasajero.getIdPasajero(), pasajero);
        anden.encolar(pasajero);
    }

    public Pasajero buscarPasajero(String idPasajero) {
        if (idPasajero == null) return null;
        return registroPasajeros.buscar(idPasajero);
    }

    public Pasajero simularEmbarque(String estacionDestino) {
        if (estacionDestino == null || estacionDestino.isBlank()) {
            throw new IllegalArgumentException("Destino inválido para simulación de embarque.");
        }
        Pasajero pasajero = anden.desencolar();
        pasajero.abordarTren(estacionDestino);
        registroPasajeros.insertar(pasajero.getIdPasajero(), pasajero);
        return pasajero;
    }

    public Pasajero verSiguientePasajero() {
        return anden.verFrente();
    }

    public Grafo.ResultadoRuta<Estacion> calcularRuta(String codigoOrigen, String codigoDestino) {
        Estacion origen = buscarEstacionPorCodigo(codigoOrigen);
        Estacion destino = buscarEstacionPorCodigo(codigoDestino);

        if (origen == null) {
            throw new IllegalArgumentException("Estación origen '" + codigoOrigen + "' no encontrada.");
        }
        if (destino == null) {
            throw new IllegalArgumentException("Estación destino '" + codigoDestino + "' no encontrada.");
        }

        Grafo.ResultadoRuta<Estacion> resultado = redMetro.dijkstra(origen, destino);
        int tiempo = (resultado != null) ? resultado.getTiempoTotal() : -1;
        historialRutas.apilar(new ConsultaRuta(codigoOrigen, codigoDestino, tiempo));
        return resultado;
    }

    public ConsultaRuta verUltimaConsulta() {
    return historialRutas.estaVacia()
            ? null
            : historialRutas.verCima();
}

    public ConsultaRuta deshacerUltimaConsulta() {
        return historialRutas.desapilar();
    }

    public void apilarConsulta(ConsultaRuta consulta) {
        if (consulta == null) return;
        historialRutas.apilar(consulta);
    }

    public ListaEnlazada<ConsultaRuta> listarHistorialRutas() {
        return historialRutas.listarElementos();
    }

    public ListaEnlazada<Estacion> obtenerEstaciones() {
        return redMetro.obtenerNodos();
    }

    public ListaEnlazada<LineaMetro> obtenerLineas() {
        ListaEnlazada<LineaMetro> lineas = new ListaEnlazada<>();
        catalogoLineas.inorden((codigo, linea) -> lineas.agregar(linea));
        return lineas;
    }

    public ListaEnlazada<Estacion> listarEstacionesRegistradas() {
        return obtenerEstaciones();
    }

    public ListaEnlazada<Pasajero> listarPasajerosRegistrados() {
        return registroPasajeros.listarValores();
    }

    public ListaEnlazada<Pasajero> listarPasajerosEnAnden() {
        return anden.listarElementos();
    }

    public int obtenerBucket(String clave) {
        return registroPasajeros.obtenerBucket(clave);
    }

    public boolean eliminarPasajero(String idPasajero) {
        if (idPasajero == null || idPasajero.isBlank()) return false;
        return registroPasajeros.eliminar(idPasajero);
    }

    public String verTablaHash() {
        return registroPasajeros.toString();
    }

    public int getTotalEstaciones() {
        return redMetro.cantidadNodos();
    }

    public int getTotalPasajeros() {
        return registroPasajeros.tamanio();
    }

    public int getPasajerosEnAnden() {
        return anden.tamanio();
    }

    public void recorrerConexiones(Grafo.AccionAdyacencia<Estacion> accion) {
        if (accion == null) return;
        redMetro.recorrerAdyacencias(accion);
    }

    public int getLineasRegistradas() {
        return catalogoLineas.tamanio();
    }

    public void encolarPasajero(Pasajero pasajero) {
        registrarPasajero(pasajero);
    }

    public Pasajero desencolarPasajero() {
        return anden.desencolar();
    }

    public void limpiarAnden() {
        anden.limpiar();
    }

    public void limpiarHistorial() {
        historialRutas.limpiar();
    }

    public int getAndenSize() {
        return anden.tamanio();
    }

    public int getHistorialSize() {
        return historialRutas.tamanio();
    }

    private Estacion buscarEstacionPorCodigo(String codigo) {
        if (codigo == null) return null;
        final Estacion[] encontrada = {null};
        catalogoLineas.inorden((cod, linea) -> {
            if (encontrada[0] == null) {
                for (int i = 0; i < linea.getEstaciones().tamanio(); i++) {
                    Estacion e = linea.getEstaciones().obtener(i);
                    if (e.getCodigo().equalsIgnoreCase(codigo)) {
                        encontrada[0] = e;
                        return;
                    }
                }
            }
        });
        return encontrada[0];
    }
}
