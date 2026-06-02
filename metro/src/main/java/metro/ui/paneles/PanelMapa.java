package metro.ui.paneles;

import metro.estructuras.ListaEnlazada;
import metro.modelo.Estacion;
import metro.modelo.LineaMetro;
import metro.servicio.MetroService;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

public class PanelMapa extends JPanel {

    private final MetroService service;
    private final Map<Estacion, Point> posiciones;
    private ListaEnlazada<Estacion> rutaActiva;
    private Estacion estacionHover;
    
    // Zoom y arrastre
    private double escala = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;
    private Point puntoArrastrePrevio = null;

    public PanelMapa(MetroService service) {
        this.service = service;
        this.posiciones = new HashMap<>();
        setOpaque(true);
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        actualizarMapa();
        crearEventosMouse();
    }

    private void crearEventosMouse() {
        ManejadorMouse manejador = new ManejadorMouse();
        addMouseListener(manejador);
        addMouseMotionListener(manejador);
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                double escalaPrev = escala;
                escala *= (1 - notches * 0.1);
                escala = Math.max(0.5, Math.min(3.0, escala));
                
                // Zoom en el punto del cursor
                offsetX -= (int) (e.getX() * (escala - escalaPrev) / escalaPrev);
                offsetY -= (int) (e.getY() * (escala - escalaPrev) / escalaPrev);
                
                repaint();
            }
        });
    }
    
    private class ManejadorMouse extends MouseInputAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = transformarCoordenadas(e.getPoint());
            estacionHover = null;
            for (Map.Entry<Estacion, Point> entry : posiciones.entrySet()) {
                Point pos = entry.getValue();
                if (pos.distance(p) < 24 / escala) {
                    estacionHover = entry.getKey();
                    break;
                }
            }
            repaint();
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            puntoArrastrePrevio = e.getPoint();
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (puntoArrastrePrevio != null) {
                int dx = e.getX() - puntoArrastrePrevio.x;
                int dy = e.getY() - puntoArrastrePrevio.y;
                offsetX += dx;
                offsetY += dy;
                puntoArrastrePrevio = e.getPoint();
                repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            puntoArrastrePrevio = null;
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private Point transformarCoordenadas(Point p) {
        return new Point(
            (int) ((p.x - offsetX) / escala),
            (int) ((p.y - offsetY) / escala)
        );
    }
    
    private Point invertirTransformacion(Point p) {
        return new Point(
            (int) (p.x * escala + offsetX),
            (int) (p.y * escala + offsetY)
        );
    }

    public void setRutaActiva(ListaEnlazada<Estacion> ruta) {
        this.rutaActiva = ruta;
        actualizarMapa();
        repaint();
    }

    public void limpiarRuta() {
        this.rutaActiva = null;
        repaint();
    }

    private void actualizarMapa() {
        posiciones.clear();
        int ancho = getWidth() - 40; // Considerando bordes
        int alto = getHeight() - 40;
        if (ancho <= 0 || alto <= 0) {
            ancho = 560;
            alto = 700;
        }

        // Calcular posiciones relativas sin considerar el tamaño del panel
        Map<Estacion, Point> posRelativas = new HashMap<>();
        ListaEnlazada<LineaMetro> lineas = service.obtenerLineas();
        for (int i = 0; i < lineas.tamanio(); i++) {
            LineaMetro linea = lineas.obtener(i);
            for (int j = 0; j < linea.cantidadEstaciones(); j++) {
                Estacion estacion = linea.getEstaciones().obtener(j);
                if (posRelativas.containsKey(estacion)) continue;
                posRelativas.put(estacion, calcularPosicionRelativa(linea, i, j));
            }
        }

        ListaEnlazada<Estacion> todas = service.obtenerEstaciones();
        for (int i = 0; i < todas.tamanio(); i++) {
            Estacion estacion = todas.obtener(i);
            posRelativas.putIfAbsent(estacion, new Point(140 + i * 90, 100 + (i % 4) * 110));
        }
        
        // Escalar posiciones según el espacio disponible
        escalarYCentrarPosiciones(posRelativas, ancho, alto);
    }

    private void escalarYCentrarPosiciones(Map<Estacion, Point> posRelativas, int ancho, int alto) {
        if (posRelativas.isEmpty()) return;
        
        // Encontrar los límites
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        
        for (Point p : posRelativas.values()) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
        
        int rangoX = maxX - minX + 60;
        int rangoY = maxY - minY + 60;
        
        double escalaX = ancho / (double) rangoX;
        double escalaY = alto / (double) rangoY;
        double escalaFinal = Math.min(escalaX, escalaY);
        escalaFinal = Math.min(escalaFinal, 1.2); // No escalar más allá de 120%
        
        // Aplicar escala y centrar
        for (Map.Entry<Estacion, Point> entry : posRelativas.entrySet()) {
            Point p = entry.getValue();
            int x = (int) ((p.x - minX) * escalaFinal + 30);
            int y = (int) ((p.y - minY) * escalaFinal + 30);
            posiciones.put(entry.getKey(), new Point(x, y));
        }
    }

    private Point calcularPosicionRelativa(LineaMetro linea, int lineaIndex, int estacionIndex) {
        String codigo = linea.getCodigoLinea();
        switch (codigo) {
            case "L1":
                return new Point(180, 100 + estacionIndex * 120);
            case "L2":
                return new Point(420 + estacionIndex * 140, 300);
            default:
                return new Point(120 + estacionIndex * 100, 100 + lineaIndex * 140);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        actualizarMapa();
        g2.setColor(new Color(245, 247, 250));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Aplicar transformaciones de zoom y arrastre
        g2.translate(offsetX, offsetY);
        g2.scale(escala, escala);

        dibujarFondo(g2);
        dibujarConexiones(g2);
        dibujarRutaActiva(g2);
        dibujarEstaciones(g2);

        // Revertir transformaciones para dibujar leyenda fija
        g2.scale(1.0 / escala, 1.0 / escala);
        g2.translate(-offsetX / escala, -offsetY / escala);
        dibujarLeyenda(g2);

        g2.dispose();
    }

    private void dibujarFondo(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(10, 10, (int) ((getWidth() - 20) / escala), (int) ((getHeight() - 20) / escala), 30, 30);
    }

    private void dibujarConexiones(Graphics2D g2) {
        service.recorrerConexiones((origen, destino, peso) -> {
            if (origen.getCodigo().compareTo(destino.getCodigo()) >= 0) {
                return;
            }
            Point p1 = posiciones.get(origen);
            Point p2 = posiciones.get(destino);
            if (p1 == null || p2 == null) return;

            boolean mismaLinea = origen.getCodigoLinea().equals(destino.getCodigoLinea());
            if (mismaLinea) {
                g2.setColor(new Color(25, 118, 210));
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            } else {
                g2.setColor(new Color(66, 165, 245, 200));
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{8, 6}, 0));
            }
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            dibujarEtiquetaTiempo(g2, peso + " min", (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        });
    }

    private void dibujarEtiquetaTiempo(Graphics2D g2, String texto, int x, int y) {
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        int ancho = fm.stringWidth(texto) + 10;
        int alto = fm.getHeight();
        g2.setColor(new Color(32, 33, 36, 180));
        g2.fillRoundRect(x - ancho / 2, y - alto - 6, ancho, alto + 4, 8, 8);
        g2.setColor(Color.WHITE);
        g2.drawString(texto, x - ancho / 2 + 5, y - 2);
    }

    private void dibujarRutaActiva(Graphics2D g2) {
        if (rutaActiva == null || rutaActiva.tamanio() < 2) return;

        g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 152, 0, 170));
        for (int i = 0; i < rutaActiva.tamanio() - 1; i++) {
            Point p1 = posiciones.get(rutaActiva.obtener(i));
            Point p2 = posiciones.get(rutaActiva.obtener(i + 1));
            if (p1 == null || p2 == null) continue;
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void dibujarEstaciones(Graphics2D g2) {
        int radio = 20;
        for (Map.Entry<Estacion, Point> entry : posiciones.entrySet()) {
            Estacion estacion = entry.getKey();
            Point p = entry.getValue();
            boolean enRuta = estaEnRuta(estacion);
            boolean hover = estacion.equals(estacionHover);

            g2.setColor(new Color(0, 0, 0, 28));
            g2.fillOval(p.x - radio + 3, p.y - radio + 3, radio * 2, radio * 2);

            if (enRuta) {
                g2.setColor(new Color(255, 152, 0));
            } else if ("L1".equals(estacion.getCodigoLinea())) {
                g2.setColor(new Color(25, 118, 210));
            } else if ("L2".equals(estacion.getCodigoLinea())) {
                g2.setColor(new Color(76, 175, 80));
            } else {
                g2.setColor(new Color(66, 133, 244));
            }
            g2.fillOval(p.x - radio, p.y - radio, radio * 2, radio * 2);

            g2.setColor(new Color(255, 255, 255, 140));
            g2.fillOval(p.x - radio + 4, p.y - radio + 4, radio * 2 - 8, radio * 2 - 8);

            if (hover) {
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(255, 155, 0));
                g2.drawOval(p.x - radio - 3, p.y - radio - 3, radio * 2 + 6, radio * 2 + 6);
            }

            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(255, 255, 255));
            String codigo = estacion.getCodigo();
            int anchoCodigo = g2.getFontMetrics().stringWidth(codigo);
            g2.drawString(codigo, p.x - anchoCodigo / 2, p.y + 5);

            dibujarNombre(g2, estacion, p);
        }
    }

    private void dibujarNombre(Graphics2D g2, Estacion estacion, Point p) {
        String nombre = estacion.getNombre();
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();
        int ancho = fm.stringWidth(nombre);
        int x = p.x + 26;
        int y = p.y + 5;

        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(x - 6, y - fm.getAscent() + 2, ancho + 12, fm.getHeight() + 4, 8, 8);
        g2.setColor(new Color(33, 33, 33));
        g2.drawString(nombre, x, y);
    }

    private void dibujarLeyenda(Graphics2D g2) {
        int x = getWidth() - 220;
        int y = 30;
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(x - 10, y - 10, 210, 110, 14, 14);
        g2.setColor(new Color(189, 189, 189));
        g2.drawRoundRect(x - 10, y - 10, 210, 110, 14, 14);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.setColor(new Color(33, 33, 33));
        g2.drawString("Leyenda", x, y + 8);

        g2.setColor(new Color(25, 118, 210));
        g2.fillRect(x, y + 24, 18, 5);
        g2.setColor(new Color(33, 33, 33));
        g2.drawString("Línea Norte-Sur", x + 28, y + 30);

        g2.setColor(new Color(76, 175, 80));
        g2.fillRect(x, y + 42, 18, 5);
        g2.drawString("Línea Este-Oeste", x + 28, y + 48);

        g2.setColor(new Color(255, 152, 0));
        g2.fillRect(x, y + 60, 18, 5);
        g2.drawString("Ruta activa", x + 28, y + 66);

        g2.setColor(new Color(66, 133, 244));
        g2.fillRect(x, y + 78, 18, 5);
        g2.drawString("Conexión transversal", x + 28, y + 84);
    }

    private boolean estaEnRuta(Estacion estacion) {
        if (rutaActiva == null) return false;
        for (int i = 0; i < rutaActiva.tamanio(); i++) {
            if (rutaActiva.obtener(i).equals(estacion)) return true;
        }
        return false;
    }
}
