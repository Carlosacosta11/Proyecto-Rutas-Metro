package metro.ui.paneles;

import metro.estructuras.Grafo;
import metro.estructuras.ListaEnlazada;
import metro.modelo.*;
import metro.servicio.MetroService;
import metro.ui.EstiloUI;

import javax.swing.*;
import java.awt.*;

public class PanelBSTGrafo extends JPanel {

    private final MetroService service;
    private final PanelMapa panelMapa;
    private final JTextArea areaBST;
    private final JTextField txtBuscarLinea, txtNuevaCod, txtNuevoNom;
    private final JTextField txtOrigen, txtDestino;
    private JComboBox<String> cmbConOrigen;
    private JComboBox<String> cmbConDestino;
    private final JTextField txtConMin;
    private final JTextArea areaResultado;
    private final JTextArea areaLog;

    public PanelBSTGrafo(MetroService service, PanelMapa panelMapa) {
        this.service = service;
        this.panelMapa = panelMapa;
        setBackground(EstiloUI.FONDO_APP);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel titulo = new JLabel("BST & Grafo — Líneas y Rutas");
        titulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        titulo.setForeground(EstiloUI.TEXTO);
        add(titulo, BorderLayout.NORTH);

        // Panel principal dividido - usar JSplitPane para mejor control visual
        // Los subpaneles mantienen sus bordes y contenido, el split permite redimensionar

        // ========== PANEL IZQUIERDO - BST ==========
        JPanel panelIzq = new JPanel(new BorderLayout(10, 10));
        panelIzq.setBackground(EstiloUI.FONDO_PANEL);
        panelIzq.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstiloUI.BORDE),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        panelIzq.add(EstiloUI.crearEncabezado("Árbol BST — Catálogo de líneas"), BorderLayout.NORTH);

        areaBST = EstiloUI.crearAreaLog();
        areaBST.setFont(EstiloUI.FUENTE_MONO_SM);
        panelIzq.add(EstiloUI.crearScroll(areaBST), BorderLayout.CENTER);

        JPanel accionesIzq = new JPanel();
        accionesIzq.setLayout(new BoxLayout(accionesIzq, BoxLayout.Y_AXIS));
        accionesIzq.setBackground(EstiloUI.FONDO_APP);

        accionesIzq.add(EstiloUI.crearLabel("Buscar línea:"));
        txtBuscarLinea = EstiloUI.crearCampo(8);
        txtBuscarLinea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        accionesIzq.add(txtBuscarLinea);
        accionesIzq.add(Box.createVerticalStrut(5));
        
        JButton btnBuscar = EstiloUI.crearBotonPrimario("Buscar");
        btnBuscar.addActionListener(e -> buscarLinea());
        accionesIzq.add(btnBuscar);
        accionesIzq.add(Box.createVerticalStrut(15));

        accionesIzq.add(EstiloUI.crearLabel("Nueva línea:"));
        txtNuevaCod = EstiloUI.crearCampo(6);
        txtNuevaCod.setMaximumSize(new Dimension(120, 32));
        accionesIzq.add(txtNuevaCod);
        txtNuevoNom = EstiloUI.crearCampo(12);
        txtNuevoNom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        accionesIzq.add(txtNuevoNom);
        
        JButton btnInsertar = EstiloUI.crearBoton("Insertar línea");
        btnInsertar.addActionListener(e -> insertarLinea());
        accionesIzq.add(btnInsertar);
        accionesIzq.add(Box.createVerticalStrut(15));

        JButton btnInorden = EstiloUI.crearBoton("Mostrar todas");
        btnInorden.addActionListener(e -> mostrarInorden());
        accionesIzq.add(btnInorden);
        
        JButton btnBorde = EstiloUI.crearBoton("Caso borde: L99");
        btnBorde.setForeground(EstiloUI.ACENTO2);
        btnBorde.addActionListener(e -> casoBordeBST());
        accionesIzq.add(btnBorde);
        
        accionesIzq.add(Box.createVerticalGlue());

        JScrollPane scrollAccionesIzq = EstiloUI.crearScroll(accionesIzq);
        scrollAccionesIzq.setPreferredSize(new Dimension(160, 0));
        panelIzq.add(scrollAccionesIzq, BorderLayout.SOUTH);

        // ========== PANEL DERECHO - DIJKSTRA ==========
        JPanel panelDer = new JPanel(new BorderLayout(10, 10));
        panelDer.setBackground(EstiloUI.FONDO_PANEL);
        panelDer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstiloUI.BORDE),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        panelDer.add(EstiloUI.crearEncabezado("Dijkstra — Ruta más corta"), BorderLayout.NORTH);

        areaResultado = EstiloUI.crearAreaLog();
        areaResultado.setFont(EstiloUI.FUENTE_MONO_SM);
        panelDer.add(EstiloUI.crearScroll(areaResultado), BorderLayout.CENTER);

        JPanel accionesDer = new JPanel();
        accionesDer.setLayout(new BoxLayout(accionesDer, BoxLayout.Y_AXIS));
        accionesDer.setBackground(EstiloUI.FONDO_APP);

        accionesDer.add(EstiloUI.crearLabel("Origen (E1-E8):"));
        txtOrigen = EstiloUI.crearCampo(6);
        accionesDer.add(txtOrigen);
        accionesDer.add(Box.createVerticalStrut(5));
        
        accionesDer.add(EstiloUI.crearLabel("Destino (E1-E8):"));
        txtDestino = EstiloUI.crearCampo(6);
        accionesDer.add(txtDestino);
        accionesDer.add(Box.createVerticalStrut(10));
        
        JButton btnCalcular = EstiloUI.crearBotonPrimario("Calcular ruta");
        btnCalcular.addActionListener(e -> calcularRuta());
        accionesDer.add(btnCalcular);
        accionesDer.add(Box.createVerticalStrut(5));
        
        JButton btnLimpiar = EstiloUI.crearBoton("Limpiar mapa");
        btnLimpiar.addActionListener(e -> {
            panelMapa.limpiarRuta();
            areaResultado.setText("");
        });
        accionesDer.add(btnLimpiar);
        accionesDer.add(Box.createVerticalStrut(15));
        
        JButton btnBordeRuta = EstiloUI.crearBoton("Caso borde: E99");
        btnBordeRuta.setForeground(EstiloUI.ACENTO2);
        btnBordeRuta.addActionListener(e -> casoBordeRuta());
        accionesDer.add(btnBordeRuta);
        
        JButton btnSimular = EstiloUI.crearBoton("Simular embarque");
        btnSimular.addActionListener(e -> simularEmbarque());
        accionesDer.add(btnSimular);

        // --- UI para conectar estaciones (origén, destino, minutos)
        accionesDer.add(Box.createVerticalStrut(12));
        accionesDer.add(EstiloUI.crearLabel("Conectar estaciones:"));
        cmbConOrigen = new JComboBox<>();
        cmbConDestino = new JComboBox<>();
        txtConMin = EstiloUI.crearCampo(4);
        txtConMin.setMaximumSize(new Dimension(80, 28));
        accionesDer.add(cmbConOrigen);
        accionesDer.add(Box.createVerticalStrut(4));
        accionesDer.add(cmbConDestino);
        accionesDer.add(Box.createVerticalStrut(4));
        accionesDer.add(EstiloUI.crearLabel("Minutos (peso):"));
        accionesDer.add(txtConMin);
        JButton btnConectar = EstiloUI.crearBotonPrimario("Conectar estaciones");
        btnConectar.addActionListener(e -> conectarEstacionesUI());
        accionesDer.add(Box.createVerticalStrut(6));
        accionesDer.add(btnConectar);

        panelDer.add(accionesDer, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzq, panelDer);
        split.setDividerLocation(480);
        split.setResizeWeight(0.45);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);

        // Log
        areaLog = EstiloUI.crearAreaLog();
        JScrollPane scrollLog = EstiloUI.crearScroll(areaLog);
        scrollLog.setPreferredSize(new Dimension(0, 100));
        add(scrollLog, BorderLayout.SOUTH);

        // Inicializar
        mostrarInorden();
        poblarCombosConEstaciones();
    }

    private void poblarCombosConEstaciones() {
        cmbConOrigen.removeAllItems();
        cmbConDestino.removeAllItems();
        ListaEnlazada<Estacion> est = service.obtenerEstaciones();
        for (int i = 0; i < est.tamanio(); i++) {
            Estacion e = est.obtener(i);
            cmbConOrigen.addItem(e.getCodigo());
            cmbConDestino.addItem(e.getCodigo());
        }
    }

    private void buscarLinea() {
        String cod = txtBuscarLinea.getText().trim().toUpperCase();
        LineaMetro l = service.buscarLinea(cod);
        if (l != null) {
            areaLog.append("[OK] Encontrada: " + l + "\n");
        } else {
            areaLog.append("[ERROR] Línea '" + cod + "' no encontrada\n");
        }
    }

    private void insertarLinea() {
        String cod = txtNuevaCod.getText().trim().toUpperCase();
        String nom = txtNuevoNom.getText().trim();
        if (cod.isEmpty() || nom.isEmpty()) {
            areaLog.append("[WARN] Código y nombre requeridos\n");
            return;
        }

        service.registrarLinea(new LineaMetro(cod, nom));
        areaLog.append("[OK] Línea '" + cod + "' insertada\n");
        txtNuevaCod.setText("");
        txtNuevoNom.setText("");
        mostrarInorden();
    }

    private void mostrarInorden() {
        areaBST.setText("── LÍNEAS REGISTRADAS (ordenadas por código) ──\n\n");
        ListaEnlazada<LineaMetro> lineas = service.obtenerLineas();
        if (lineas.tamanio() == 0) {
            areaBST.append("(No hay líneas registradas)\n");
            return;
        }

        for (int i = 0; i < lineas.tamanio(); i++) {
            LineaMetro linea = lineas.obtener(i);
            areaBST.append((i + 1) + ". " + linea.getCodigoLinea() + " → " + linea.getNombre() +
                          " (" + linea.cantidadEstaciones() + " estaciones)\n");
            areaBST.append("   Estaciones: ");
            for (int j = 0; j < linea.getEstaciones().tamanio(); j++) {
                if (j > 0) areaBST.append(", ");
                areaBST.append(linea.getEstaciones().obtener(j).getNombre());
            }
            areaBST.append("\n\n");
        }
    }

    // ========== MÉTODO CORREGIDO - CALCULAR RUTA ==========
    private void calcularRuta() {
        String org = txtOrigen.getText().trim().toUpperCase();
        String dst = txtDestino.getText().trim().toUpperCase();
        
        if (org.isEmpty() || dst.isEmpty()) {
            areaLog.append("[WARN] Ingrese origen y destino\n");
            return;
        }
        
        try {
            Grafo.ResultadoRuta<Estacion> resultado = service.calcularRuta(org, dst);
            areaResultado.setText("");
            
            if (resultado == null) {
                areaResultado.append("No existe ruta entre " + org + " y " + dst + "\n");
                panelMapa.limpiarRuta();
                areaLog.append("[ERROR] Sin ruta entre " + org + " y " + dst + "\n");
            } else {
                // OBTENER EL CAMINO - Versión CORREGIDA
                ListaEnlazada<Estacion> camino = resultado.getCamino();
                
                areaResultado.append("RUTA ENCONTRADA\n\n");
                areaResultado.append("Tiempo total: " + resultado.getTiempoTotal() + " minutos\n\n");
                areaResultado.append("Camino:\n");
                
                // Verificar que camino no sea null
                if (camino != null && !camino.estaVacia()) {
                    for (int i = 0; i < camino.tamanio(); i++) {
                        Estacion e = camino.obtener(i);
                        areaResultado.append("   " + (i+1) + ". " + e.getNombre() + " [" + e.getCodigo() + "]\n");
                        if (i < camino.tamanio() - 1) {
                            areaResultado.append("       ↓\n");
                        }
                    }
                    panelMapa.setRutaActiva(camino);
                } else {
                    areaResultado.append("   (Camino vacío)\n");
                }
                
                areaLog.append("[OK] Ruta " + org + " → " + dst + ": " + resultado.getTiempoTotal() + " min\n");
            }
        } catch (IllegalArgumentException e) {
            areaLog.append("[ERROR] " + e.getMessage() + "\n");
            panelMapa.limpiarRuta();
        } catch (Exception e) {
            areaLog.append("[ERROR] Error inesperado: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void casoBordeBST() {
        LineaMetro l = service.buscarLinea("L99");
        areaLog.append("[INFO] [CASO BORDE] Buscar 'L99': " + (l == null ? "null (no existe) [OK]" : l) + "\n");
    }

    private void casoBordeRuta() {
        txtOrigen.setText("E99");
        txtDestino.setText("E1");
        areaLog.append("[WARN] [CASO BORDE] Calculando ruta con estación inexistente...\n");
        calcularRuta();
        txtOrigen.setText("");
        txtDestino.setText("");
    }

    private void simularEmbarque() {
        String dst = txtDestino.getText().trim().toUpperCase();
        if (dst.isEmpty()) {
            areaLog.append("[WARN] Ingrese destino para simular embarque\n");
            return;
        }
        try {
            Pasajero p = service.simularEmbarque(dst);
            areaLog.append("[INFO] " + p.getNombre() + " ha abordado hacia " + dst + 
                          " | Viajes: " + p.getViajes() + "\n");
        } catch (IllegalStateException e) {
            areaLog.append("[ERROR] " + e.getMessage() + "\n");
        }
    }

    // Buscar estación por código dentro del servicio
    private Estacion buscarEstacionPorCodigo(String codigo) {
        ListaEnlazada<Estacion> est = service.obtenerEstaciones();
        for (int i = 0; i < est.tamanio(); i++) {
            Estacion e = est.obtener(i);
            if (e.getCodigo().equalsIgnoreCase(codigo)) return e;
        }
        return null;
    }

    private void conectarEstacionesUI() {
        String o = (String) cmbConOrigen.getSelectedItem();
        String d = (String) cmbConDestino.getSelectedItem();
        String minTxt = txtConMin.getText().trim();
        if (o == null || d == null || o.isEmpty() || d.isEmpty() || minTxt.isEmpty()) {
            areaLog.append("[WARN] Seleccione origen, destino y minutos\n");
            return;
        }
        try {
            int minutos = Integer.parseInt(minTxt);
            Estacion origen = buscarEstacionPorCodigo(o);
            Estacion destino = buscarEstacionPorCodigo(d);
            if (origen == null || destino == null) {
                areaLog.append("[ERROR] Estación origen o destino no encontrada\n");
                return;
            }
            service.conectarEstaciones(origen, destino, minutos);
            areaLog.append("[INFO] Conectadas " + o + " - " + d + " (" + minutos + " min)\n");
            panelMapa.revalidate();
            panelMapa.repaint();
        } catch (NumberFormatException ex) {
            areaLog.append("[ERROR] Minutos debe ser un número\n");
        }
    }
}