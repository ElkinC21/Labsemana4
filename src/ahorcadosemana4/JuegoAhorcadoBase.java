
package ahorcadosemana4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author saidn
 */
public abstract class JuegoAhorcadoBase implements JuegoAhorcado {

    public String palabraSecreta;
    public String palabraActual;
    public int intentos;
    public int limiteIntentos;
    public static ArrayList<Character> letrasUsadas = new ArrayList<>();
    public static ArrayList<String> figuraAhorcado = new ArrayList<>();

    public JuegoAhorcadoBase() {

    }

    public String actualizarPalabraActual(String palabraSecreta) {return palabraActual;}
    
    public boolean verificarLetra() {return true;}
    
    public boolean hasGanado(){return true;}

}
