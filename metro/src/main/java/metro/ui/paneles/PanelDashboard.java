package metro.ui.paneles;

import metro.modelo.ConsultaRuta;
import metro.servicio.MetroService;
import metro.ui.EstiloUI;

import javax.swing.*;
import java.awt.*;

public class PanelDashboard extends JPanel {

    private final MetroService service;
    private final JLabel lblEstaciones;
    private final JLabel lblPasajeros;
    private final JLabel lblAnden;
    private final JLabel lblLineas;
    private final JLabel lblUltimaRuta;

    public PanelDashboard(MetroService service) {
        this.service = service;
        setLayout(new BorderLayout(16, 16));
        setBackground(EstiloUI.FONDO_APP);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titulo = new JLabel("Dashboard de Transito");
        titulo.setFont(EstiloUI.FUENTE_SUBTITULO);
        titulo.setForeground(EstiloUI.TEXTO);
        add(titulo, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 14, 14));
        cards.setOpaque(false);

        lblEstaciones = new JLabel();
        lblPasajeros = new JLabel();
        lblAnden = new JLabel();
        lblLineas = new JLabel();
        lblUltimaRuta = new JLabel();

        cards.add(crearTarjeta("Estaciones", "0", "Total de estaciones registradas", lblEstaciones));
        cards.add(crearTarjeta("Pasajeros", "0", "Pasajeros registrados", lblPasajeros));
        cards.add(crearTarjeta("Andén", "0", "Pasajeros en espera", lblAnden));
        cards.add(crearTarjeta("Líneas", "0", "Líneas activas en el sistema", lblLineas));
        cards.add(crearTarjeta("Última ruta", "N/A", "Última búsqueda de ruta realizada", lblUltimaRuta));

        add(cards, BorderLayout.CENTER);

        actualizarIndicadores();
    }

    private JPanel crearTarjeta(String titulo, String valor, String subtitulo, JLabel valorEtiqueta) {
        JPanel tarjeta = new JPanel(new BorderLayout(8, 8));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstiloUI.BORDE, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(EstiloUI.FUENTE_LABEL_BOLD);
        lblTitulo.setForeground(EstiloUI.ACENTO);

        valorEtiqueta.setText(valor);
        valorEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 34));
        valorEtiqueta.setForeground(EstiloUI.TEXTO);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(EstiloUI.FUENTE_LABEL);
        lblSub.setForeground(EstiloUI.TEXTO_DIMM);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(valorEtiqueta, BorderLayout.CENTER);
        tarjeta.add(lblSub, BorderLayout.SOUTH);

        return tarjeta;
    }

    public void actualizarIndicadores() {
        lblEstaciones.setText(String.valueOf(service.getTotalEstaciones()));
        lblPasajeros.setText(String.valueOf(service.getTotalPasajeros()));
        lblAnden.setText(String.valueOf(service.getPasajerosEnAnden()));
        lblLineas.setText(String.valueOf(service.getLineasRegistradas()));

        ConsultaRuta ultima = service.verUltimaConsulta();
        lblUltimaRuta.setText(ultima != null ? ultima.toString() : "N/A");
    }
}
