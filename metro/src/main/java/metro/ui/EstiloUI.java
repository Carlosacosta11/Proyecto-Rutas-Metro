package metro.ui;

import javax.swing.*;
import java.awt.*;

public final class EstiloUI {

    // ========== CONSTANTES PARA COMPATIBILIDAD CON PANELES EXISTENTES ==========
    // Colores de fondo y texto (usados en paneles como PanelListaEnlazada, PanelTablaHash, etc.)
    public static final Color FONDO_APP = new Color(248, 249, 250);      // Fondo principal claro
    public static final Color FONDO_PANEL = new Color(255, 255, 255);    // Fondo de paneles (blanco)
    public static final Color FONDO_CLARO = new Color(250, 251, 252);    // Fondo ligeramente gris
    public static final Color TEXTO = new Color(32, 33, 36);             // Texto principal oscuro
    public static final Color TEXTO_DIMM = new Color(95, 99, 104);       // Texto secundario
    public static final Color TEXTO_HEADER = new Color(0, 112, 243);      // Color para encabezados de tabla (azul)
    public static final Color BORDE = new Color(218, 220, 224);           // Color de bordes
    // Unificar color primario a #42A5F5 para botones y acentos
    public static final Color ACENTO = new Color(0x42, 0xA5, 0xF5);            // Azul primario
    public static final Color ACENTO2 = new Color(234, 67, 53);           // Rojo para advertencias/borde
    public static final Color EXITO = new Color(52, 168, 83);             // Verde éxito
    public static final Color ERROR = new Color(234, 67, 53);             // Rojo error
    public static final Color INFO = new Color(66, 133, 244);             // Azul info
    
    // Fuentes
    public static final Font FUENTE_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FUENTE_MONO_SM = new Font("Consolas", Font.PLAIN, 11);
    public static final Font FUENTE_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_LABEL_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    
    // ========== MÉTODOS MODERNOS ==========
    
    public static void aplicarTema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Configuración básica de colores para componentes Swing
        UIManager.put("Panel.background", FONDO_APP);
        UIManager.put("Label.font", FUENTE_LABEL);
        UIManager.put("Button.font", FUENTE_LABEL_BOLD);
        UIManager.put("TabbedPane.font", FUENTE_LABEL_BOLD);
        UIManager.put("Table.font", FUENTE_MONO);
        UIManager.put("TableHeader.font", FUENTE_LABEL_BOLD);
        UIManager.put("TextField.font", FUENTE_LABEL);
        UIManager.put("ComboBox.font", FUENTE_LABEL);
    }
    
    // Botón estándar
    public static JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_LABEL_BOLD);
        btn.setForeground(Color.WHITE);
        // Azul claro consistente solicitado (#42A5F5)
        Color azulClaro = new Color(0x42, 0xA5, 0xF5);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(azulClaro);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Hover y focus visual
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(azulClaro.darker()); // ligero oscurecimiento en hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(azulClaro);
            }
        });
        btn.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACENTO, 2),
                    BorderFactory.createEmptyBorder(7, 15, 7, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE, 1),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });

        return btn;
    }
    
    // Botón primario (azul)
    public static JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_LABEL_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(ACENTO);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACENTO.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACENTO);
            }
        });
        // Focus visual similar al botón estándar
        btn.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACENTO.darker(), 2),
                    BorderFactory.createEmptyBorder(7, 15, 7, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE, 1),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });
        return btn;
    }
    
    // Botón de peligro (rojo)
    public static JButton crearBotonPeligro(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_LABEL_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(ACENTO);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACENTO.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACENTO);
            }
        });
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    // Campo de texto
    public static JTextField crearCampo(int columnas) {
        JTextField tf = new JTextField(columnas);
        tf.setFont(FUENTE_LABEL);
        tf.setBackground(FONDO_PANEL);
        tf.setForeground(TEXTO);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }
    
    // Área de texto para logs
    public static JTextArea crearAreaLog() {
        JTextArea ta = new JTextArea();
        ta.setFont(FUENTE_MONO);
        ta.setBackground(FONDO_CLARO);
        ta.setForeground(TEXTO);
        ta.setEditable(false);
        ta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return ta;
    }
    
    // Scroll pane
    public static JScrollPane crearScroll(Component componente) {
        JScrollPane sp = new JScrollPane(componente);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setBackground(FONDO_PANEL);
        return sp;
    }
    
    // Etiqueta de encabezado
    public static JLabel crearEncabezado(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_SUBTITULO);
        lbl.setForeground(ACENTO);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return lbl;
    }
    
    // Etiqueta normal
    public static JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_LABEL);
        lbl.setForeground(TEXTO_DIMM);
        return lbl;
    }
    
    // Panel con gradiente (usado en VentanaPrincipal)
    public static JPanel crearPanelGradiente(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }
    
    // Método para log con emoji
    public static void log(JTextArea area, String mensaje, String tipo) {
        String prefix = switch (tipo) {
            case "ok" -> "[OK] ";
            case "error" -> "[ERROR] ";
            case "info" -> "[INFO] ";
            case "warn" -> "[WARN] ";
            default -> "[LOG] ";
        };
        area.append(prefix + mensaje + "\n");
        area.setCaretPosition(area.getDocument().getLength());
    }
}