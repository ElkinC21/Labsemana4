package ahorcadosemana4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PalabrasFijas {

    private static final String NOMBRE_ARCHIVO = "palabras.txt";
    private static final List<String> PALABRAS_DEFAULT = Arrays.asList(
        "casa", "perro", "gato", "arbol", "playa",
        "montana", "rio", "libro", "juego", "codigo"
    );

    public static ArrayList<String> leerPalabras() {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) {
            crearArchivoDefault();
        }

        ArrayList<String> palabras = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    palabras.add(linea.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de palabras: " + e.getMessage());
            return new ArrayList<>(PALABRAS_DEFAULT);
        }
        return palabras;
    }

    public static void guardarPalabras(List<String> palabras) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, StandardCharsets.UTF_8, false))) {
            for (String palabra : palabras) {
                writer.write(palabra);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de palabras: " + e.getMessage());
        }
    }

    private static void crearArchivoDefault() {
        System.out.println("Creando archivo de palabras por defecto...");
        guardarPalabras(PALABRAS_DEFAULT);
    }
}
