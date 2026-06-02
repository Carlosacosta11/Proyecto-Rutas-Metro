package metro.ui;

import metro.servicio.MetroService;
import metro.ui.paneles.PanelMapa;

import javax.swing.*;
import java.awt.*;

public class VistaMapaExpandida extends JFrame {

    private final PanelMapa panelMapa;

    public VistaMapaExpandida(MetroService service) {
        setTitle("Grafo del Metro — Vista Expandida");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Maximizar ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        
        panelMapa = new PanelMapa(service);
        
        // Panel superior con botones
        JPanel toolbar = crearToolbar();
        
        // Panel principal
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(panelMapa, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private JPanel crearToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(EstiloUI.FONDO_PANEL);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, EstiloUI.BORDE));

        JLabel titulo = new JLabel("Vista Expandida del Grafo — Rueda del mouse para zoom, arrastra para navegar");
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titulo.setForeground(EstiloUI.TEXTO);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setForeground(EstiloUI.ACENTO2);
        btnCerrar.addActionListener(e -> dispose());

        toolbar.add(titulo);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(btnCerrar);

        return toolbar;
    }

    public PanelMapa obtenerPanelMapa() {
        return panelMapa;
    }
}
