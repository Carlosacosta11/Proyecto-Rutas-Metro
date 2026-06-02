package metro.modelo;

/**
 * Representa un pasajero del sistema de metro.
 * Almacena su ID, nombre, estación actual y número de viajes realizados.
 */
public class Pasajero {

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Identificador único del pasajero. */
    private final String idPasajero;

    /** Nombre completo del pasajero. */
    private String nombre;

    /** Código de la estación donde se encuentra actualmente. */
    private String estacionActual;

    /** Número de viajes realizados. */
    private int viajes;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Crea un pasajero con su ID, nombre y estación inicial.
     *
     * @param idPasajero    Identificador único.
     * @param nombre        Nombre completo.
     * @param estacionActual Código de la estación donde espera.
     */
    public Pasajero(String idPasajero, String nombre, String estacionActual) {
        if (idPasajero == null || idPasajero.isBlank()) {
            throw new IllegalArgumentException("El ID del pasajero no puede estar vacío.");
        }
        this.idPasajero = idPasajero;
        this.nombre = nombre;
        this.estacionActual = estacionActual;
        this.viajes = 0;
    }

    // ── Comportamiento ────────────────────────────────────────────────────────

    /**
     * Registra que el pasajero abordó el tren y actualiza su estación.
     *
     * @param nuevaEstacion Estación de destino tras el viaje.
     */
    public void abordarTren(String nuevaEstacion) {
        this.estacionActual = nuevaEstacion;
        this.viajes++;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────

    public String getIdPasajero() {
        return idPasajero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstacionActual() {
        return estacionActual;
    }

    public void setEstacionActual(String estacionActual) {
        this.estacionActual = estacionActual;
    }

    public int getViajes() {
        return viajes;
    }

    @Override
    public String toString() {
        return "Pasajero{id='" + idPasajero + "', nombre='" + nombre
                + "', estacion='" + estacionActual + "', viajes=" + viajes + "}";
    }
}
