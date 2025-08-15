package ahorcadosemana4;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AhorcadoFijo extends JuegoAhorcadoBase {

    private String ultimaEntrada = null;
    private String ultimoMensaje = " ";
    private final int indicePalabra;

    public AhorcadoFijo(int indicePalabra) {
        super();
        this.indicePalabra = indicePalabra;
    }

    @Override
    public ArrayList<String> getPalabrasSecretas() {
        return PalabrasFijas.leerPalabras();
    }

    @Override
    public String inicializarPalabraSecreta() {
        ArrayList<String> palabras = getPalabrasSecretas();
        if (indicePalabra >= 0 && indicePalabra < palabras.size()) {
            this.palabraSecreta = palabras.get(indicePalabra);
        } else {
            this.palabraSecreta = "defecto"; // Palabra de respaldo
        }
        letrasUsadas.clear();
        limiteIntentos = 6;
        intentos = 0;
        cargarFigurasAhorcado();
        this.palabraActual = actualizarPalabraActual(this.palabraSecreta);
        return this.palabraSecreta;
    }

    @Override
    public String actualizarPalabraActual(String palabraSecreta) {
        StringBuilder sb = new StringBuilder();
        for (char ch : palabraSecreta.toCharArray()) {
            if (ch == ' ' || ch == '-') { sb.append(ch); continue; }
            if (letrasUsadas.contains(Character.toLowerCase(ch))) {
                sb.append(ch);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    @Override
    public boolean verificarLetra() {
        if (ultimaEntrada == null || ultimaEntrada.isEmpty()) return false;

        String entrada = ultimaEntrada.trim();
        ultimaEntrada = null;

        if (entrada.length() > 1) {
            if (entrada.equalsIgnoreCase(palabraSecreta)) {
                palabraActual = palabraSecreta;
                ultimoMensaje = "¡Adivinaste la palabra!";
                return true;
            } else {
                intentos++;
                ultimoMensaje = "No es la palabra.";
                return false;
            }
        }

        char c = Character.toLowerCase(entrada.charAt(0));
        if (!Character.isLetter(c)) {
            ultimoMensaje = "Introduce una letra válida.";
            return false;
        }
        if (letrasUsadas.contains(c)) {
            ultimoMensaje = "Ya usaste esa letra.";
            return false;
        }

        letrasUsadas.add(c);
        boolean acierto = palabraSecreta.toLowerCase().indexOf(c) >= 0;
        if (acierto) {
            ultimoMensaje = "¡Acierto!";
        } else {
            intentos++;
            ultimoMensaje = "La letra '" + c + "' no está.";
        }
        palabraActual = actualizarPalabraActual(palabraSecreta);
        return acierto;
    }

    @Override
    public boolean hasGanado() {
        return palabraSecreta != null && palabraSecreta.equals(palabraActual);
    }

    @Override
    public void jugar() {
        inicializarPalabraSecreta();

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        AhorcadoFijoFrame.bindInputQueue(queue);

        AhorcadoFijoFrame.updateView(figuraAhorcado.get(0),
                formatearConEspacios(palabraActual),
                intentos, limiteIntentos, letrasUsadas,
                "Introduce una letra o intenta la palabra completa");

        boolean abandonar = false;

        while (!hasGanado() && intentos < limiteIntentos && !abandonar) {
            try {
                String jugada = queue.take();
                if ("__SALIR__".equals(jugada)) {
                    abandonar = true;
                    break;
                }
                this.ultimaEntrada = jugada;
                verificarLetra();

                int etapa = Math.min(intentos, figuraAhorcado.size() - 1);
                AhorcadoFijoFrame.updateView(
                        figuraAhorcado.get(etapa),
                        formatearConEspacios(palabraActual),
                        intentos, limiteIntentos, letrasUsadas,
                        ultimoMensaje
                );
            } catch (InterruptedException ignored) {
            }
        }

        int etapa = Math.min(intentos, figuraAhorcado.size() - 1);
        String mensajeFinal;
        if (hasGanado()) {
            mensajeFinal = "¡Has ganado! La palabra era: " + palabraSecreta;
        } else {
            mensajeFinal = "Fin de la partida. La palabra era: " + palabraSecreta;
        }
        
        AhorcadoFijoFrame.updateView(figuraAhorcado.get(etapa),
                formatearConEspacios(palabraActual),
                intentos, limiteIntentos, letrasUsadas,
                mensajeFinal);
        
        AhorcadoFijoFrame.juegoTerminado();
    }

    private void cargarFigurasAhorcado() {
        if (!figuraAhorcado.isEmpty()) return; // Cargar solo una vez
        
        figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));

    figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |              |             |",
        "  |              |             |",
        "  |             _______        |",
        "  |            /       \\       |",
        "  |            |  0 0  |       |",
        "  |            |   ^   |       |",
        "  |            |  ---  |       |",
        "  |            \\_______/       |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));

    figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |              |             |",
        "  |              |             |",
        "  |             _______        |",
        "  |            /       \\       |",
        "  |            |  0 0  |       |",
        "  |            |   ^   |       |",
        "  |            |  ---  |       |",
        "  |            \\_______/       |",
        "  |               |            |",
        "  |               |            |",
        "  |               |            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));

    figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |              |             |",
        "  |              |             |",
        "  |             _______        |",
        "  |            /       \\       |",
        "  |            |  0 0  |       |",
        "  |            |   ^   |       |",
        "  |            |  ---  |       |",
        "  |            \\_______/       |",
        "  |               |            |",
        "  |              /|            |",
        "  |             / |            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));

    figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |              |             |",
        "  |              |             |",
        "  |             _______        |",
        "  |            /       \\       |",
        "  |            |  0 0  |       |",
        "  |            |   ^   |       |",
        "  |            |  ---  |       |",
        "  |            \\_______/       |",
        "  |               |            |",
        "  |              /|\\           |",
        "  |             / | \\          |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));

    figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |              |             |",
        "  |              |             |",
        "  |             _______        |",
        "  |            /       \\       |",
        "  |            |  0 0  |       |",
        "  |            |   ^   |       |",
        "  |            |  ---  |       |",
        "  |            \\_______/       |",
        "  |               |            |",
        "  |              /|\\           |",
        "  |             / | \\          |",
        "  |              /             |",
        "  |             /              |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));

    figuraAhorcado.add(String.join("\n", new String[]{
        "  +----------------------------+",
        "  |                            |",
        "  |              |             |",
        "  |              |             |",
        "  |             _______        |",
        "  |            /       \\       |",
        "  |            |  x x  |       |",
        "  |            |   ^   |       |",
        "  |            |  ---  |       |",
        "  |            \\_______/       |",
        "  |               |            |",
        "  |              /|\\           |",
        "  |             / | \\          |",
        "  |              / \\           |",
        "  |             /   \\          |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "  |                            |",
        "==+============================+"
    }));
    }

    private String formatearConEspacios(String s) {
        return s == null ? "" : s.replace("", " ").trim();
    }
}
