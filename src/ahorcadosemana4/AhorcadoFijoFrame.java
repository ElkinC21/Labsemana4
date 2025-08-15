package ahorcadosemana4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AhorcadoFijoFrame extends JFrame {

    private static JTextArea txtFigura;
    private static JLabel lblTituloPalabra, lblIntentos, lblLetras, lblMensaje;
    private static JTextArea taPalabra;
    private static JTextField txtEntrada;
    private static JButton btnEnviar, btnRendirse, btnVolver;
    private static JPanel panelJuego;

    private static BlockingQueue<String> inputQueue;
    private static JFrame menuAnterior;

    public AhorcadoFijoFrame(JFrame menu) {
        super("Ahorcado (Palabra Fija)");
        menuAnterior = menu;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (inputQueue != null) {
                    inputQueue.offer("__SALIR__");
                }
                menuAnterior.setVisible(true);
            }
        });

        // Primero, pedir al usuario que elija una palabra
        ArrayList<String> palabras = PalabrasFijas.leerPalabras();
        JComboBox<String> comboPalabras = new JComboBox<>(palabras.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(null, comboPalabras, "Elige una palabra para jugar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int indiceSeleccionado = comboPalabras.getSelectedIndex();
            // Si el usuario eligió una palabra, construir la GUI del juego
            construirUI();
            // Iniciar el juego en un nuevo hilo
            new Thread(() -> {
                var juego = new AhorcadoFijo(indiceSeleccionado);
                juego.jugar();
            }, "hilo-juego-fijo").start();
        } else {
            // Si el usuario cancela, simplemente cierra esta ventana y muestra el menú
            dispose();
            menuAnterior.setVisible(true);
        }
    }

    private void construirUI() {
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 520));
        setLocationRelativeTo(null);

        var root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        var titulo = new JLabel("Ahorcado (Palabra Fija)", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        root.add(titulo, BorderLayout.NORTH);

        var centro = new JPanel(new GridLayout(1, 2, 16, 16));
        root.add(centro, BorderLayout.CENTER);

        txtFigura = new JTextArea(24, 44);
        txtFigura.setEditable(false);
        txtFigura.setFont(new Font("Monospaced", Font.PLAIN, 22));
        txtFigura.setLineWrap(false);
        txtFigura.setBackground(Color.WHITE);
        txtFigura.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(14, 14, 14, 14)
        ));
        var scFigura = new JScrollPane(txtFigura);
        scFigura.setBorder(BorderFactory.createEmptyBorder());
        centro.add(scFigura);

        var derecha = new JPanel();
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));
        derecha.setBorder(new EmptyBorder(6, 6, 6, 6));
        centro.add(derecha);

        lblTituloPalabra = new JLabel("Palabra:");
        lblTituloPalabra.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloPalabra.setAlignmentX(Component.LEFT_ALIGNMENT);

        taPalabra = new JTextArea(1, 40);
        taPalabra.setFont(new Font("Monospaced", Font.BOLD, 30));
        taPalabra.setEditable(false);
        taPalabra.setLineWrap(false);
        taPalabra.setWrapStyleWord(false);
        taPalabra.setBackground(new Color(248, 248, 248));
        taPalabra.setBorder(new EmptyBorder(6, 8, 6, 8));
        var scPalabra = new JScrollPane(taPalabra,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scPalabra.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblIntentos = new JLabel("Intentos: 0 / 6");
        lblIntentos.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblIntentos.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblLetras = new JLabel("Letras usadas: —");
        lblLetras.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLetras.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblMensaje.setForeground(new Color(50, 100, 185));
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelJuego = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelJuego.setAlignmentX(Component.LEFT_ALIGNMENT);
        var lbl = new JLabel("Letra o palabra:");
        txtEntrada = new JTextField(20);
        txtEntrada.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnEnviar = new JButton("Probar");
        btnRendirse = new JButton("Rendirse");
        panelJuego.add(lbl);
        panelJuego.add(txtEntrada);
        panelJuego.add(btnEnviar);
        panelJuego.add(btnRendirse);

        derecha.add(lblTituloPalabra);
        derecha.add(Box.createVerticalStrut(6));
        derecha.add(scPalabra);
        derecha.add(Box.createVerticalStrut(12));
        derecha.add(lblIntentos);
        derecha.add(Box.createVerticalStrut(4));
        derecha.add(lblLetras);
        derecha.add(Box.createVerticalStrut(10));
        derecha.add(lblMensaje);
        derecha.add(Box.createVerticalStrut(12));
        derecha.add(panelJuego);

        var abajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        btnVolver = new JButton("Volver al Menú");
        abajo.add(btnVolver);
        root.add(abajo, BorderLayout.SOUTH);

        btnEnviar.addActionListener(e -> {
            if (inputQueue == null) return;
            var t = txtEntrada.getText().trim();
            if (!t.isEmpty()) {
                inputQueue.offer(t);
                txtEntrada.setText("");
                txtEntrada.requestFocusInWindow();
            }
        });
        txtEntrada.addActionListener(e -> btnEnviar.doClick());

        btnRendirse.addActionListener(e -> {
            if (inputQueue != null) {
                inputQueue.offer("__SALIR__");
            }
        });

        btnVolver.addActionListener(e -> {
            if (inputQueue != null) {
                inputQueue.offer("__SALIR__");
            }
            dispose();
            menuAnterior.setVisible(true);
        });
    }

    public static void bindInputQueue(BlockingQueue<String> q) {
        inputQueue = q;
        SwingUtilities.invokeLater(() -> {
            txtEntrada.setText("");
            txtEntrada.requestFocusInWindow();
        });
    }

    public static void updateView(String figura, String palabraBonita, int intentos, int limite,
                                  List<Character> usadas, String mensaje) {
        SwingUtilities.invokeLater(() -> {
            txtFigura.setText(figura);
            taPalabra.setText(palabraBonita);
            taPalabra.setCaretPosition(0);
            lblIntentos.setText("Intentos: " + intentos + " / " + limite);
            lblLetras.setText("Letras usadas: " + (usadas.isEmpty() ? "—" : usadas.toString()));
            lblMensaje.setText(mensaje == null ? " " : mensaje);
        });
    }

    public static void juegoTerminado() {
        SwingUtilities.invokeLater(() -> {
            txtEntrada.setEnabled(false);
            btnEnviar.setEnabled(false);
            btnRendirse.setEnabled(false);
        });
    }
}
