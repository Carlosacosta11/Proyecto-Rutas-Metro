package metro.ui.paneles;

import metro.estructuras.ListaEnlazada;
import metro.modelo.Pasajero;
import metro.servicio.MetroService;
import metro.ui.EstiloUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class PanelTablaHash extends JPanel {

    private final MetroService service;

    private final JTable tablaPasajeros;
    private final DefaultTableModel modeloTabla;
    private final JTextArea areaBuckets;
    private final JTextField txtBuscarId;
    private final JTextArea areaLog;

    public PanelTablaHash(MetroService service) {
        this.service = service;
        setBackground(EstiloUI.FONDO_APP);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel titulo = new JLabel("Tabla Hash — Registro de Pasajeros (con encadenamiento)");
        titulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        titulo.setForeground(EstiloUI.TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(4, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Panel central usando JSplitPane para mejor uso de espacio
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(EstiloUI.FONDO_APP);

        // ========== TABLA DE PASAJEROS ==========
        String[] columnas = {"ID", "Nombre", "Estación", "Viajes", "Bucket"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaPasajeros = new JTable(modeloTabla);
        estilizarTabla(tablaPasajeros);

        JPanel panelTabla = new JPanel(new BorderLayout(4, 4));
        panelTabla.setBackground(EstiloUI.FONDO_APP);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JScrollPane scrollTabla = EstiloUI.crearScroll(tablaPasajeros);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // Botones debajo de la tabla
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        panelBotones.setBackground(EstiloUI.FONDO_APP);

        panelBotones.add(EstiloUI.crearLabel("Buscar ID:"));
        txtBuscarId = EstiloUI.crearCampo(8);
        txtBuscarId.setPreferredSize(new Dimension(100, 30));
        panelBotones.add(txtBuscarId);

        JButton btnBuscar = EstiloUI.crearBotonPrimario("Buscar");
        btnBuscar.addActionListener(e -> buscarPasajero());
        panelBotones.add(btnBuscar);

        JButton btnEliminar = EstiloUI.crearBoton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarPasajero());
        panelBotones.add(btnEliminar);

        JButton btnBorde = EstiloUI.crearBoton("Caso borde: ID inexistente");
        btnBorde.setForeground(EstiloUI.ACENTO2);
        btnBorde.addActionListener(e -> casoBorde());
        panelBotones.add(btnBorde);

        panelTabla.add(panelBotones, BorderLayout.SOUTH);

        // ========== VISTA DE BUCKETS ==========
        areaBuckets = EstiloUI.crearAreaLog();
        areaBuckets.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        JPanel panelBuckets = new JPanel(new BorderLayout(4, 4));
        panelBuckets.setBackground(EstiloUI.FONDO_APP);
        panelBuckets.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JScrollPane scrollBuckets = EstiloUI.crearScroll(areaBuckets);
        panelBuckets.add(scrollBuckets, BorderLayout.CENTER);

        JButton btnColisiones = EstiloUI.crearBoton("Demostrar colisiones");
        btnColisiones.addActionListener(e -> demostrarColisiones());
        JPanel panelBotonColisiones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonColisiones.setBackground(EstiloUI.FONDO_APP);
        panelBotonColisiones.add(btnColisiones);
        panelBuckets.add(panelBotonColisiones, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTabla, panelBuckets);
        split.setDividerLocation(620);
        split.setResizeWeight(0.7);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);

        // Log
        areaLog = EstiloUI.crearAreaLog();
        JScrollPane scrollLog = EstiloUI.crearScroll(areaLog);
        scrollLog.setPreferredSize(new Dimension(0, 80));
        add(scrollLog, BorderLayout.SOUTH);

        refrescar();
    }

    private void buscarPasajero() {
        String id = txtBuscarId.getText().trim();
        if (id.isEmpty()) {
            areaLog.append("[WARN] Ingrese un ID\n");
            return;
        }
        Pasajero p = service.buscarPasajero(id);
        if (p != null) {
            int bucket = service.obtenerBucket(id);
            areaLog.append("[OK] Encontrado en bucket[" + bucket + "]: " + p.getNombre() + "\n");
        } else {
            areaLog.append("[ERROR] Pasajero '" + id + "' no encontrado → null\n");
        }
    }

    private void eliminarPasajero() {
        String id = txtBuscarId.getText().trim();
        if (id.isEmpty()) {
            areaLog.append("[WARN] Ingrese un ID\n");
            return;
        }
        boolean ok = service.eliminarPasajero(id);
        if (ok) {
            areaLog.append("[OK] Eliminado: " + id + "\n");
            refrescar();
        } else {
            areaLog.append("[ERROR] ID '" + id + "' no encontrado\n");
        }
    }

    private void casoBorde() {
        areaLog.append("[WARN] [CASO BORDE] Buscar 'INEXISTENTE': ");
        Pasajero p = service.buscarPasajero("INEXISTENTE");
        areaLog.append((p == null ? "null [OK]\n" : p + "\n"));
    }

    private void demostrarColisiones() {
        areaBuckets.setText("");
        areaBuckets.append("=== DEMOSTRACIÓN DE COLISIONES ===\n\n");
        
        String[] ids = {"P001", "P005", "P016", "P032"};
        areaBuckets.append("Buckets para diferentes IDs:\n");
        for (String id : ids) {
            Pasajero p = service.buscarPasajero(id);
            if (p != null) {
                int bucket = service.obtenerBucket(id);
                areaBuckets.append("   • " + id + " → bucket[" + bucket + "] | " + p.getNombre() + "\n");
            }
        }
        
        areaBuckets.append("\nSi dos IDs comparten el mismo bucket → COLISIÓN\n");
        areaBuckets.append("   La colisión se resuelve con LISTA ENLAZADA dentro del bucket.\n\n");
        areaBuckets.append("=== TABLA HASH COMPLETA ===\n");
        areaBuckets.append(service.verTablaHash());
        
        areaLog.append("[INFO] Demostración de colisiones actualizada\n");
    }

    private void refrescar() {
        modeloTabla.setRowCount(0);

        ListaEnlazada<Pasajero> pasajeros = service.listarPasajerosRegistrados();
        for (int i = 0; i < pasajeros.tamanio(); i++) {
            Pasajero p = pasajeros.obtener(i);
            int bucket = service.obtenerBucket(p.getIdPasajero());
            modeloTabla.addRow(new Object[]{
                p.getIdPasajero(),
                p.getNombre(),
                p.getEstacionActual(),
                p.getViajes(),
                bucket
            });
        }

        // Mostrar buckets
        mostrarBuckets();
    }

    private void mostrarBuckets() {
        areaBuckets.setText(service.verTablaHash());
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(EstiloUI.FONDO_CLARO);
        tabla.setForeground(EstiloUI.TEXTO);
        tabla.setFont(EstiloUI.FUENTE_MONO_SM);
        tabla.setRowHeight(24);
        tabla.setGridColor(EstiloUI.BORDE);
        tabla.setSelectionBackground(EstiloUI.ACENTO);
        tabla.setSelectionForeground(EstiloUI.FONDO_APP);
        tabla.getTableHeader().setBackground(EstiloUI.FONDO_PANEL);
        tabla.getTableHeader().setForeground(EstiloUI.TEXTO_HEADER);
        tabla.getTableHeader().setFont(EstiloUI.FUENTE_LABEL_BOLD);
        
        // Ajustar ancho de columnas
        if (tabla.getColumnModel().getColumnCount() >= 5) {
            tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(80);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(50);
        }
    }
}