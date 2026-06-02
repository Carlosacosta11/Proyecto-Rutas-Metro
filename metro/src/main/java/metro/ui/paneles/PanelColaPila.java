package metro.ui.paneles;

import metro.estructuras.ListaEnlazada;
import metro.modelo.*;
import metro.servicio.MetroService;
import metro.ui.EstiloUI;

import javax.swing.*;
import java.awt.*;

public class PanelColaPila extends JPanel {

    private final MetroService service;

    // Cola
    private final JTextArea areaCola;
    private final JTextField txtIdPas, txtNombrePas, txtEstPas;

    // Pila
    private final JTextArea areaPila;
    private final JTextField txtOrigen, txtDestino, txtMinutos;

    // Log
    private final JTextArea areaLog;

    public PanelColaPila(MetroService service) {
        this.service = service;
        setBackground(EstiloUI.FONDO_APP);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel titulo = new JLabel("Cola (FIFO) - Andén | Pila (LIFO) - Historial");
        titulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        titulo.setForeground(EstiloUI.TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(4, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Panel dividido (usar JSplitPane para mejor distribución)
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(EstiloUI.FONDO_APP);

        // ========== PANEL COLA ==========
        JPanel panelCola = new JPanel(new BorderLayout(6, 6));
        panelCola.setBackground(EstiloUI.FONDO_PANEL);
        panelCola.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstiloUI.BORDE),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panelCola.add(EstiloUI.crearEncabezado("COLA FIFO - Pasajeros en Andén"), BorderLayout.NORTH);

        areaCola = EstiloUI.crearAreaLog();
        areaCola.setFont(EstiloUI.FUENTE_MONO_SM);
        JScrollPane scrollCola = EstiloUI.crearScroll(areaCola);
        scrollCola.setPreferredSize(new Dimension(0, 200));
        panelCola.add(scrollCola, BorderLayout.CENTER);

        JPanel accionesCola = new JPanel();
        accionesCola.setLayout(new BoxLayout(accionesCola, BoxLayout.Y_AXIS));
        accionesCola.setBackground(EstiloUI.FONDO_APP);
        accionesCola.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        accionesCola.add(EstiloUI.crearLabel("ID Pasajero:"));
        txtIdPas = EstiloUI.crearCampo(10);
        txtIdPas.setMaximumSize(new Dimension(180, 30));
        accionesCola.add(txtIdPas);
        accionesCola.add(Box.createVerticalStrut(5));

        accionesCola.add(EstiloUI.crearLabel("Nombre:"));
        txtNombrePas = EstiloUI.crearCampo(10);
        txtNombrePas.setMaximumSize(new Dimension(180, 30));
        accionesCola.add(txtNombrePas);
        accionesCola.add(Box.createVerticalStrut(5));

        accionesCola.add(EstiloUI.crearLabel("Estación actual:"));
        txtEstPas = EstiloUI.crearCampo(10);
        txtEstPas.setMaximumSize(new Dimension(180, 30));
        accionesCola.add(txtEstPas);
        accionesCola.add(Box.createVerticalStrut(10));

        JButton btnEncolar = EstiloUI.crearBotonPrimario("Encolar");
        btnEncolar.addActionListener(e -> encolar());
        accionesCola.add(btnEncolar);
        JButton btnDesencolar = EstiloUI.crearBoton("Desencolar");
        btnDesencolar.addActionListener(e -> desencolar());
        accionesCola.add(btnDesencolar);
        JButton btnFrente = EstiloUI.crearBoton("Ver frente");
        btnFrente.addActionListener(e -> verFrente());
        accionesCola.add(btnFrente);
        JButton btnBordeCola = EstiloUI.crearBoton("Caso borde: cola vacía");
        btnBordeCola.setForeground(EstiloUI.ACENTO2);
        btnBordeCola.addActionListener(e -> bordeColaVacia());
        accionesCola.add(btnBordeCola);
        
        accionesCola.add(Box.createVerticalGlue());

        // Envolver acciones en scroll si crecen
        JScrollPane scrollAccionesCola = EstiloUI.crearScroll(accionesCola);
        scrollAccionesCola.setPreferredSize(new Dimension(200, 0));
        panelCola.add(scrollAccionesCola, BorderLayout.EAST);

        // ========== PANEL PILA ==========
        JPanel panelPila = new JPanel(new BorderLayout(6, 6));
        panelPila.setBackground(EstiloUI.FONDO_PANEL);
        panelPila.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstiloUI.BORDE),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panelPila.add(EstiloUI.crearEncabezado("PILA LIFO - Historial de Rutas"), BorderLayout.NORTH);

        areaPila = EstiloUI.crearAreaLog();
        areaPila.setFont(EstiloUI.FUENTE_MONO_SM);
        JScrollPane scrollPila = EstiloUI.crearScroll(areaPila);
        scrollPila.setPreferredSize(new Dimension(0, 200));
        panelPila.add(scrollPila, BorderLayout.CENTER);

        JPanel accionesPila = new JPanel();
        accionesPila.setLayout(new BoxLayout(accionesPila, BoxLayout.Y_AXIS));
        accionesPila.setBackground(EstiloUI.FONDO_APP);
        accionesPila.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        accionesPila.add(EstiloUI.crearLabel("Origen:"));
        txtOrigen = EstiloUI.crearCampo(6);
        txtOrigen.setMaximumSize(new Dimension(180, 30));
        accionesPila.add(txtOrigen);
        accionesPila.add(Box.createVerticalStrut(5));

        accionesPila.add(EstiloUI.crearLabel("Destino:"));
        txtDestino = EstiloUI.crearCampo(6);
        txtDestino.setMaximumSize(new Dimension(180, 30));
        accionesPila.add(txtDestino);
        accionesPila.add(Box.createVerticalStrut(5));

        accionesPila.add(EstiloUI.crearLabel("Minutos:"));
        txtMinutos = EstiloUI.crearCampo(4);
        txtMinutos.setMaximumSize(new Dimension(180, 30));
        accionesPila.add(txtMinutos);
        accionesPila.add(Box.createVerticalStrut(10));

        JButton btnApilar = EstiloUI.crearBotonPrimario("Apilar consulta");
        btnApilar.addActionListener(e -> apilar());
        accionesPila.add(btnApilar);
        JButton btnCima = EstiloUI.crearBoton("Ver cima");
        btnCima.addActionListener(e -> verCima());
        accionesPila.add(btnCima);
        JButton btnDesapilar = EstiloUI.crearBoton("Desapilar");
        btnDesapilar.addActionListener(e -> desapilar());
        accionesPila.add(btnDesapilar);
        JButton btnBordePila = EstiloUI.crearBoton("Caso borde: pila vacía");
        btnBordePila.setForeground(EstiloUI.ACENTO2);
        btnBordePila.addActionListener(e -> bordePilaVacia());
        accionesPila.add(btnBordePila);
        
        accionesPila.add(Box.createVerticalGlue());

        // Envolver acciones en scroll si crecen
        JScrollPane scrollAccionesPila = EstiloUI.crearScroll(accionesPila);
        scrollAccionesPila.setPreferredSize(new Dimension(200, 0));
        panelPila.add(scrollAccionesPila, BorderLayout.EAST);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCola, panelPila);
        split.setDividerLocation(520);
        split.setResizeWeight(0.5);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);

        // Log con altura mínima flexible
        areaLog = EstiloUI.crearAreaLog();
        JScrollPane scrollLog = EstiloUI.crearScroll(areaLog);
        scrollLog.setPreferredSize(new Dimension(0, 100));
        scrollLog.setMinimumSize(new Dimension(0, 60));
        add(scrollLog, BorderLayout.SOUTH);

        refrescar();
    }

    // ========== ACCIONES COLA ==========
    private void encolar() {
        String id = txtIdPas.getText().trim();
        String nom = txtNombrePas.getText().trim();
        String est = txtEstPas.getText().trim().toUpperCase();

        if (id.isEmpty() || nom.isEmpty()) {
            areaLog.append("[WARN] ID y nombre son obligatorios\n");
            return;
        }

        Pasajero p = new Pasajero(id, nom, est.isEmpty() ? "E1" : est);
        service.encolarPasajero(p);
        areaLog.append("[OK] " + nom + " encolado. Tamaño andén: " + service.getPasajerosEnAnden() + "\n");

        txtIdPas.setText("");
        txtNombrePas.setText("");
        txtEstPas.setText("");
        refrescar();
    }

    private void desencolar() {
        try {
            Pasajero p = service.desencolarPasajero();
            areaLog.append("[INFO] Desencolado: " + p.getNombre() + " — andén restante: " + service.getPasajerosEnAnden() + "\n");
            refrescar();
        } catch (IllegalStateException e) {
            areaLog.append("[ERROR] " + e.getMessage() + "\n");
        }
    }

    private void verFrente() {
        try {
            Pasajero p = service.verSiguientePasajero();
            areaLog.append("Frente del andén: " + p.getNombre() + " (" + p.getIdPasajero() + ")\n");
        } catch (IllegalStateException e) {
            areaLog.append("[ERROR] " + e.getMessage() + "\n");
        }
    }

    private void bordeColaVacia() {
        service.limpiarAnden();
        areaLog.append("[WARN] [CASO BORDE] Cola limpiada. Intentando desencolar...\n");
        try {
            service.desencolarPasajero();
        } catch (IllegalStateException e) {
            areaLog.append("[OK] Excepción capturada: " + e.getMessage() + "\n");
        }
        refrescar();
    }

    // ========== ACCIONES PILA ==========
    private void apilar() {
        String org = txtOrigen.getText().trim().toUpperCase();
        String dst = txtDestino.getText().trim().toUpperCase();
        String minStr = txtMinutos.getText().trim();

        if (org.isEmpty() || dst.isEmpty()) {
            areaLog.append("[WARN] Origen y destino son obligatorios\n");
            return;
        }

        int min = 0;
        try {
            min = Integer.parseInt(minStr);
        } catch (NumberFormatException ignored) {}

        service.apilarConsulta(new ConsultaRuta(org, dst, min));
        areaLog.append("[OK] Consulta apilada: " + org + "→" + dst + " (" + min + " min). Total: " + service.getHistorialSize() + "\n");

        txtOrigen.setText("");
        txtDestino.setText("");
        txtMinutos.setText("");
        refrescar();
    }

    private void verCima() {
        try {
            ConsultaRuta cr = service.verUltimaConsulta();
            areaLog.append("Cima: " + cr + "\n");
        } catch (IllegalStateException e) {
            areaLog.append("[ERROR] " + e.getMessage() + "\n");
        }
    }

    private void desapilar() {
        try {
            ConsultaRuta cr = service.deshacerUltimaConsulta();
            areaLog.append("[OK] Desapilado: " + cr + ". Restantes: " + service.getHistorialSize() + "\n");
            refrescar();
        } catch (IllegalStateException e) {
            areaLog.append("[ERROR] " + e.getMessage() + "\n");
        }
    }

    private void bordePilaVacia() {
        service.limpiarHistorial();
        areaLog.append("[WARN] [CASO BORDE] Pila limpiada. Intentando ver cima...\n");
        try {
            service.verUltimaConsulta();
        } catch (IllegalStateException e) {
            areaLog.append("[OK] Excepción capturada: " + e.getMessage() + "\n");
        }
        refrescar();
    }

    // ========== REFRESCAR VISTA ==========
    private void refrescar() {
        // Cola
        areaCola.setText("");
        ListaEnlazada<Pasajero> pasajeros = service.listarPasajerosEnAnden();
        areaCola.append("TAMAÑO: " + service.getPasajerosEnAnden() + " pasajero(s)\n");
        areaCola.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        areaCola.append(pasajeros.toString() + "\n");

        // Pila
        areaPila.setText("");
        ListaEnlazada<ConsultaRuta> consultas = service.listarHistorialRutas();
        areaPila.append("TAMAÑO: " + service.getHistorialSize() + " consulta(s)\n");
        areaPila.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        areaPila.append(consultas.toString() + "\n");
    }
}