package ahorcadosemana4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class CambiaPalabrasFrame extends JFrame {

    private final List<JTextField> camposDeTexto = new ArrayList<>();
    private final JFrame menuAnterior;

    public CambiaPalabrasFrame(JFrame menu) {
        super("Cambiar Palabras Fijas");
        this.menuAnterior = menu;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                menuAnterior.setVisible(true);
            }
        });

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(panelPrincipal);

        JLabel titulo = new JLabel("Editor de Palabras", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelGrid = new JPanel(new GridLayout(10, 2, 8, 8));
        panelPrincipal.add(panelGrid, BorderLayout.CENTER);

        ArrayList<String> palabrasActuales = PalabrasFijas.leerPalabras();

        for (int i = 0; i < 10; i++) {
            JLabel label = new JLabel("Palabra " + (i + 1) + ":");
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            JTextField textField = new JTextField(20);
            textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            if (i < palabrasActuales.size()) {
                textField.setText(palabrasActuales.get(i));
            }

            camposDeTexto.add(textField);
            panelGrid.add(label);
            panelGrid.add(textField);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Volver al Menú");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            ArrayList<String> nuevasPalabras = new ArrayList<>();
            boolean todasValidas = true;
            for (JTextField campo : camposDeTexto) {
                String palabra = campo.getText().trim().toLowerCase();
                if (palabra.isEmpty() || !palabra.matches("[a-zñ]+")) {
                    todasValidas = false;
                    break;
                }
                nuevasPalabras.add(palabra);
            }

            if (todasValidas && nuevasPalabras.size() == 10) {
                PalabrasFijas.guardarPalabras(nuevasPalabras);
                JOptionPane.showMessageDialog(this, "Palabras guardadas correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                menuAnterior.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Todas las palabras deben ser ingresadas y contener solo letras (a-z, ñ).", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> {
            dispose();
            menuAnterior.setVisible(true);
        });
    }
}
