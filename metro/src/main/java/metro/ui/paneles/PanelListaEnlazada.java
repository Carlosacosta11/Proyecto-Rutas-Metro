package metro.ui.paneles;

import metro.estructuras.ListaEnlazada;
import metro.modelo.*;
import metro.servicio.MetroService;
import metro.ui.EstiloUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class PanelListaEnlazada extends JPanel {

    private final MetroService service;
    private final JTable tablaEstaciones;
    private final DefaultTableModel modeloTabla;
    private final JComboBox<String> cmbLinea;
    private final JTextArea areaLog;

    public PanelListaEnlazada(MetroService service) {
        this.service = service;
        setBackground(EstiloUI.FONDO_APP);
        setLayout(new BorderLayout(14, 14));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel titulo = new JLabel("Lista Enlazada — Estaciones por Línea");
        titulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        titulo.setForeground(EstiloUI.TEXTO);

        JPanel header = new JPanel(new BorderLayout(0, 8));
        header.setOpaque(false);
        header.add(titulo, BorderLayout.NORTH);

        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelSelector.setOpaque(false);
        panelSelector.add(EstiloUI.crearLabel("Línea:"));

        cmbLinea = new JComboBox<>();
        cmbLinea.setBackground(EstiloUI.FONDO_CLARO);
        cmbLinea.setForeground(EstiloUI.TEXTO);
        cmbLinea.addActionListener(e -> actualizarTabla());
        panelSelector.add(cmbLinea);

        JButton btnActualizar = EstiloUI.crearBoton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarTabla());
        panelSelector.add(btnActualizar);

        header.add(panelSelector, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        String[] columnas = {"#", "Código", "Nombre", "Línea"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaEstaciones = new JTable(modeloTabla);
        tablaEstaciones.setRowHeight(30);
        tablaEstaciones.setFont(EstiloUI.FUENTE_MONO_SM);
        tablaEstaciones.getTableHeader().setFont(EstiloUI.FUENTE_LABEL_BOLD);
        tablaEstaciones.setShowGrid(false);
        tablaEstaciones.setIntercellSpacing(new Dimension(0, 0));
        tablaEstaciones.setSelectionBackground(EstiloUI.ACENTO);
        tablaEstaciones.setSelectionForeground(Color.WHITE);

        JScrollPane scrollTabla = EstiloUI.crearScroll(tablaEstaciones);
        scrollTabla.setPreferredSize(new Dimension(0, 240));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAcciones.setOpaque(false);
        
        JButton btnAgregar = EstiloUI.crearBotonPrimario("Agregar estación");
        btnAgregar.addActionListener(e -> agregarEstacion());
        JButton btnEliminar = EstiloUI.crearBoton("Eliminar estación");
        btnEliminar.addActionListener(e -> eliminarEstacion());
        JButton btnBorde = EstiloUI.crearBoton("Caso borde: L99");
        btnBorde.setForeground(EstiloUI.ACENTO2);
        btnBorde.addActionListener(e -> casoBorde());

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnBorde);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setOpaque(false);
        centro.add(scrollTabla, BorderLayout.CENTER);
        centro.add(panelAcciones, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);

        areaLog = EstiloUI.crearAreaLog();
        JScrollPane scrollLog = EstiloUI.crearScroll(areaLog);
        scrollLog.setPreferredSize(new Dimension(0, 90));
        add(scrollLog, BorderLayout.SOUTH);

        cargarLineasDisponibles();
        actualizarTabla();
    }

    private void cargarLineasDisponibles() {
        cmbLinea.removeAllItems();
        ListaEnlazada<LineaMetro> lineas = service.obtenerLineas();
        for (int i = 0; i < lineas.tamanio(); i++) {
            cmbLinea.addItem(lineas.obtener(i).getCodigoLinea());
        }
        if (cmbLinea.getItemCount() > 0) {
            cmbLinea.setSelectedIndex(0);
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        String codLinea = (String) cmbLinea.getSelectedItem();
        LineaMetro linea = service.buscarLinea(codLinea);
        
        if (linea == null) {
            areaLog.append("[WARN] Línea '" + codLinea + "' no encontrada\n");
            return;
        }
        
        // CORREGIDO: usar for loop en lugar de forEach
        for (int i = 0; i < linea.getEstaciones().tamanio(); i++) {
            Estacion e = linea.getEstaciones().obtener(i);
            modeloTabla.addRow(new Object[]{
                i + 1,
                e.getCodigo(),
                e.getNombre(),
                e.getCodigoLinea()
            });
        }
        
        areaLog.append("[OK] Línea " + codLinea + ": " + linea.cantidadEstaciones() + " estaciones\n");
    }

    private void agregarEstacion() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva estación:");
        if (nombre == null || nombre.trim().isEmpty()) return;
        
        String codLinea = (String) cmbLinea.getSelectedItem();
        String nuevoCodigo = "E" + (service.getTotalEstaciones() + 1);
        
        Estacion nueva = new Estacion(nuevoCodigo, nombre, codLinea);
        boolean ok = service.agregarEstacionALinea(codLinea, nueva);
        
        if (ok) {
            areaLog.append("[OK] Estación '" + nombre + "' agregada a " + codLinea + "\n");
            actualizarTabla();
        } else {
            areaLog.append("[ERROR] Error al agregar estación\n");
        }
    }

    private void eliminarEstacion() {
        int fila = tablaEstaciones.getSelectedRow();
        if (fila == -1) {
            areaLog.append("[WARN] Seleccione una estación para eliminar\n");
            return;
        }
        
        String codigo = (String) modeloTabla.getValueAt(fila, 1);
        String codLinea = (String) cmbLinea.getSelectedItem();
        
        LineaMetro linea = service.buscarLinea(codLinea);
        if (linea != null && linea.eliminarEstacion(codigo)) {
            areaLog.append("[OK] Estación " + codigo + " eliminada\n");
            actualizarTabla();
        } else {
            areaLog.append("[ERROR] No se pudo eliminar la estación\n");
        }
    }

    private void casoBorde() {
        LineaMetro linea = service.buscarLinea("L99");
        areaLog.append("[WARN] [CASO BORDE] Buscar línea 'L99': " + (linea == null ? "null (no existe) [OK]\n" : linea + "\n"));
    }
}