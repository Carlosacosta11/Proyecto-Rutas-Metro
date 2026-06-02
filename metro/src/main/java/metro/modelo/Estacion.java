package metro.modelo;

/**
 * Representa una estación del sistema de metro.
 * Almacena su código, nombre y la línea a la que pertenece.
 */
public class Estacion {

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Código único de la estación (ej: "E1", "E2"). */
    private final String codigo;

    /** Nombre descriptivo de la estación. */
    private final String nombre;

    /** Código de la línea a la que pertenece (ej: "L1"). */
    private String codigoLinea;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Crea una estación con código, nombre y línea.
     *
     * @param codigo      Identificador único.
     * @param nombre      Nombre de la estación.
     * @param codigoLinea Código de la línea.
     */
    public Estacion(String codigo, String nombre, String codigoLinea) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la estación no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la estación no puede estar vacío.");
        }
        this.codigo = codigo.toUpperCase();
        this.nombre = nombre;
        this.codigoLinea = codigoLinea != null ? codigoLinea.toUpperCase() : "SIN_LINEA";
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoLinea() {
        return codigoLinea;
    }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setCodigoLinea(String codigoLinea) {
        this.codigoLinea = codigoLinea != null ? codigoLinea.toUpperCase() : "SIN_LINEA";
    }

    // ── Igualdad y hash ───────────────────────────────────────────────────────

    /**
     * Dos estaciones son iguales si tienen el mismo código.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Estacion)) return false;
        Estacion otra = (Estacion) obj;
        return this.codigo.equals(otra.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "/" + codigoLinea + "]";
    }
}
