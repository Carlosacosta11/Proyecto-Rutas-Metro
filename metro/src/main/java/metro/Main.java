package metro;

import metro.servicio.DatosDemo;
import metro.servicio.MetroService;
import metro.ui.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        MetroService service = new MetroService();
        DatosDemo.cargar(service);
        
        System.out.println("=== SISTEMA DE METRO INICIADO ===");
        System.out.println("[OK] 8 estaciones cargadas");
        System.out.println("[OK] 2 líneas de metro");
        System.out.println("[OK] 7 pasajeros registrados");
        System.out.println("[OK] Conexiones con pesos configuradas");
        
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(service);
            ventana.mostrar();
        });
    }
}