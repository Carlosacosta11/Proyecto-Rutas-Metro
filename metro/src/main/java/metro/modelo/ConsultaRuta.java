package metro.modelo;

/**
 * Representa una consulta de ruta realizada por un usuario.
 * Se almacena en la pila de historial.
 */
public class ConsultaRuta {

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Código de la estación de origen. */
    private final String origen;

    /** Código de la estación de destino. */
    private final String destino;

    /** Tiempo total de la ruta en minutos. */
    private final int minutos;

    /** Marca de tiempo de la consulta (System.currentTimeMillis). */
    private final long timestamp;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Crea una consulta de ruta registrada.
     *
     * @param origen   Estación de origen.
     * @param destino  Estación de destino.
     * @param minutos  Tiempo total calculado por Dijkstra.
     */
    public ConsultaRuta(String origen, String destino, int minutos) {
        this.origen = origen;
        this.destino = destino;
        this.minutos = minutos;
        this.timestamp = System.currentTimeMillis();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public int getMinutos() {
        return minutos;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ConsultaRuta{" + origen + " → " + destino + ", " + minutos + " min}";
    }
}
