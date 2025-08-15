package ahorcadosemana4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JuegoAhorcadoAzar extends JuegoAhorcadoBase {

    private String ultimaEntrada = null;    
    private String ultimoMensaje = " ";
    
    @Override
    public ArrayList<String> getPalabrasSecretas() {
        return new ArrayList<>(Arrays.asList(
            "ingeniero","computadora","pantalla","recursividad","abstractos",
            "interfaz","aventuras","sistemas","software","madrid"
        ));
    }

    @Override
    public String inicializarPalabraSecreta() {
        ArrayList<String> posibles = getPalabrasSecretas();
        Collections.shuffle(posibles);
        this.palabraSecreta = posibles.get(0);
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
            if (letrasUsadas.contains(Character.toLowerCase(ch))) sb.append(ch);
            else sb.append('_');
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
        JuegoAzarFrame.bindInputQueue(queue);

        JuegoAzarFrame.updateView(figuraAhorcado.get(0),
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

                int etapa = Math.min(intentos, figuraAhorcado.size()-1);
                JuegoAzarFrame.updateView(
                        figuraAhorcado.get(etapa),
                        formatearConEspacios(palabraActual),
                        intentos, limiteIntentos, letrasUsadas,
                        ultimoMensaje
                );
            } catch (InterruptedException ignored) { }
        }

        int etapa = Math.min(intentos, figuraAhorcado.size()-1);
        if (hasGanado()) {
            JuegoAzarFrame.updateView(figuraAhorcado.get(etapa),
                    formatearConEspacios(palabraActual),
                    intentos, limiteIntentos, letrasUsadas,
                    "¡Has ganado! La palabra era: " + palabraSecreta);
        } else if (abandonar || intentos >= limiteIntentos) {
            JuegoAzarFrame.updateView(figuraAhorcado.get(etapa),
                    formatearConEspacios(palabraActual),
                    intentos, limiteIntentos, letrasUsadas,
                    "Fin de la partida. La palabra era: " + palabraSecreta);
        }
    }

    private void cargarFigurasAhorcado() {
    figuraAhorcado.clear();

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

    // 4 fallos: + ambos brazos
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

