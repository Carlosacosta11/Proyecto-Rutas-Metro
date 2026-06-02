package metro.ui;

import metro.servicio.MetroService;
import metro.ui.paneles.*;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final MetroService service;
    private final CardLayout cardsLayout;
    private final JPanel cardsPanel;
    private final PanelMapa panelMapa;
    private final PanelDashboard dashboardPanel;
    private JLabel labelEstado;

    public VentanaPrincipal(MetroService service) {
        this.service = service;
        this.cardsLayout = new CardLayout();
        this.cardsPanel = new JPanel(cardsLayout);
        this.panelMapa = new PanelMapa(service);
        this.dashboardPanel = new PanelDashboard(service);

        EstiloUI.aplicarTema();
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("Metro Transit - Plataforma Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 920);
        setMinimumSize(new Dimension(1200, 760));
        setLocationRelativeTo(null);
        setBackground(EstiloUI.FONDO_APP);
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        add(crearHeader(), BorderLayout.NORTH);
        add(crearContenidoPrincipal(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearContenidoPrincipal() {
        JPanel contenido = new JPanel(new BorderLayout(12, 12));
        contenido.setBackground(EstiloUI.FONDO_APP);
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel izquierdo que contiene sidebar (estrecha) y cards (contenido principal)
        JPanel leftArea = new JPanel(new BorderLayout());
        leftArea.setOpaque(false);
        leftArea.add(crearSidebar(), BorderLayout.WEST);
        leftArea.add(crearCards(), BorderLayout.CENTER);

        // Split entre contenido y mapa: mapa ocupará 50% del ancho (ajustado de 40%)
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftArea, crearMapa());
        mainSplit.setResizeWeight(0.5); // 50% left, 50% right
        mainSplit.setContinuousLayout(true);
        mainSplit.setBorder(null);
        mainSplit.setOneTouchExpandable(true);
        mainSplit.setDividerSize(6);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(mainSplit, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(250, 251, 252));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstiloUI.BORDE, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        sidebar.setPreferredSize(new Dimension(160, 0));

        JLabel titulo = new JLabel("Navegación");
        titulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        titulo.setForeground(EstiloUI.ACENTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(titulo);
        sidebar.add(Box.createVerticalStrut(18));

        sidebar.add(crearBotonSidebar("Dashboard", () -> mostrarCard("dashboard")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(crearBotonSidebar("Estaciones", () -> mostrarCard("lineas")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(crearBotonSidebar("Pasajeros", () -> mostrarCard("pasajeros")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(crearBotonSidebar("Andén y Pila", () -> mostrarCard("anden")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(crearBotonSidebar("Rutas y BST", () -> mostrarCard("rutas")));
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton crearBotonSidebar(String texto, Runnable action) {
        JButton btn = new JButton(texto);
        btn.setFont(EstiloUI.FUENTE_LABEL_BOLD);
        btn.setForeground(EstiloUI.TEXTO);
        btn.setBackground(EstiloUI.FONDO_PANEL);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setFocusable(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel crearCards() {
        cardsPanel.setBackground(EstiloUI.FONDO_APP);
        cardsPanel.add(dashboardPanel, "dashboard");
        cardsPanel.add(new PanelListaEnlazada(service), "lineas");
        cardsPanel.add(new PanelTablaHash(service), "pasajeros");
        cardsPanel.add(new PanelColaPila(service), "anden");
        cardsPanel.add(new PanelBSTGrafo(service, panelMapa), "rutas");
        cardsLayout.show(cardsPanel, "dashboard");
        return cardsPanel;
    }

    private JPanel crearMapa() {
        JPanel contenedor = new JPanel(new BorderLayout(6, 6));
        contenedor.setBackground(EstiloUI.FONDO_APP);
        
        // Toolbar para el mapa
        JPanel toolbarMapa = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        toolbarMapa.setBackground(EstiloUI.FONDO_APP);
        
        JButton btnExpandir = EstiloUI.crearBotonPrimario("📊 Expandir Grafo");
        btnExpandir.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnExpandir.addActionListener(e -> {
            VistaMapaExpandida vista = new VistaMapaExpandida(service);
            // Copiar estado del zoom si es necesario
        });
        
        JLabel labelZoom = new JLabel("🔍 Rueda: zoom  |  Arrastra: mover");
        labelZoom.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        labelZoom.setForeground(EstiloUI.TEXTO_DIMM);
        
        toolbarMapa.add(labelZoom);
        toolbarMapa.add(btnExpandir);
        
        contenedor.add(toolbarMapa, BorderLayout.NORTH);
        contenedor.add(panelMapa, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearHeader() {
        JPanel header = EstiloUI.crearPanelGradiente(new Color(33, 37, 41), new Color(52, 58, 64));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        header.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("METRO TRANSIT");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Sistema Inteligente de Transporte");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(200, 200, 210));

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setOpaque(false);
        panelTexto.add(titulo);
        panelTexto.add(subtitulo);

        JPanel estado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        estado.setOpaque(false);
        JLabel badgeLabel = new JLabel("EN OPERACIÓN");
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badgeLabel.setForeground(Color.WHITE);
        badgeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        estado.add(badgeLabel);

        header.add(panelTexto, BorderLayout.WEST);
        header.add(estado, BorderLayout.EAST);

        return header;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, EstiloUI.BORDE),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        labelEstado = new JLabel("Sistema listo | Datos cargados");
        labelEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelEstado.setForeground(EstiloUI.TEXTO_DIMM);

        JLabel version = new JLabel("v2.0 | Estructuras de Datos");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(EstiloUI.TEXTO_DIMM);

        footer.add(labelEstado, BorderLayout.WEST);
        footer.add(version, BorderLayout.EAST);

        return footer;
    }

    private void mostrarCard(String nombre) {
        cardsLayout.show(cardsPanel, nombre);
        if ("dashboard".equals(nombre)) {
            dashboardPanel.actualizarIndicadores();
        }
        panelMapa.revalidate();
        panelMapa.repaint();
    }

    public void setEstado(String mensaje) {
        labelEstado.setText("[INFO] " + mensaje);
    }

    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
